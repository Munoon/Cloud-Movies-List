import React, {useState} from 'react';
import ReactDOM from 'react-dom';
import Application from "./components/Application";
import { useForm } from "react-hook-form";
import {getErrorsCount, getMetaProperty, InputField, updateMetaProperty} from "./components/misc";
import Spinner from "react-bootstrap/Spinner";
import { fetcher } from "./components/api";
import { toast } from "react-toastify";

const ProfilePageBody = () => (
    <div className='jumbotron mt-3'>
        <h3>Настройки профиля</h3>
        <UpdateProfileForm />
        <hr className="my-4" />
        <UpdateEmailForm />
        <hr className="my-4" />
        <UpdatePasswordForm />
    </div>
);

const UpdateProfileForm = () => {
    const [loading, setLoading] = useState(false);
    const { register, handleSubmit, errors } = useForm();

    const getErrorMessage = (messages, name) => messages[name] ? messages[name] : '';
    const getError = (name, messages) => !errors[name] ? null : getErrorMessage(messages, errors[name].type);
    const onSubmit = data => {
        setLoading(true);
        console.log(data);
        // fetcher('/users/profile/update', {
        //     method: 'POST',
        //     body: JSON.stringify(data)
        // }).then(() => {
        //     setLoading(false);
        //     toast.success('Вы успешно обновили профиль!');
        // }).catch(e => {
        //     setLoading(false);
        //     e.useDefaultErrorParser();
        // });
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <h4>Данные профиля</h4>

            <InputField
                id='updateProfileNameInput' type='text'
                name='name' placeholder='Петр' title='Имя' value={getMetaProperty('user:name')}
                ref={register({ minLength: 3, maxLength: 30, required: true })}
                error={getError('name', {
                    minLength: 'Имя должно содержать хотя-бы 3 символа',
                    maxLength: 'Имя должно быть меньше 30 символов'
                })}
            />

            <InputField
                id='updateProfileSurnameInput' type='text'
                name='surname' placeholder='Васильевич' title='Фамилия' value={getMetaProperty('user:surname')}
                ref={register({ minLength: 3, maxLength: 30, required: true })}
                error={getError('surname', {
                    minLength: 'Фамилия должна содержать хотя-бы 3 символа',
                    maxLength: 'Фамилия должна быть меньше 30 символов'
                })}
            />

            <div>
                <button type='submit' className='btn btn-primary' disabled={loading || getErrorsCount(errors) !== 0}>Сохранить</button>
                {loading && <Spinner animation="border" role="status" className='ml-3 spinner-vertical-middle' />}
            </div>
        </form>
    );
};

const UpdateEmailForm = () => {
    let defaultEmailErrorMessage = 'Пользователь с таким Email адресом уже зарегистрирован';
    const [emailErrorMessage, setEmailErrorMessage] = useState(defaultEmailErrorMessage);
    const [loading, setLoading] = useState(false);
    const { register, handleSubmit, errors } = useForm();

    const currentEmail = getMetaProperty('user:email');
    const getErrorMessage = (messages, name) => messages[name] ? messages[name] : '';
    const getError = (name, messages) => !errors[name] ? null : getErrorMessage(messages, errors[name].type);
    const testEmail = async email => email === currentEmail ? true
        : await fetcher(`/users/test/email/${email}`)
            .then(response => {
                setEmailErrorMessage(defaultEmailErrorMessage);
                return response;
            })
            .catch(() => {
                setEmailErrorMessage('Ошибка проверки доступности почты! Пожалуйста, попробуйте позже.');
                return false;
            });

    const onSubmit = data => {
        setLoading(true);
        updateMetaProperty('user:email', data.email);
        console.log(data);
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <h4>Изменение Email адреса</h4>
            <InputField
                id='registerEmailAddressInput' type='email' value={currentEmail}
                name='email' placeholder='name@example.com' title='Email адрес'
                ref={register({
                    pattern: /(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/,
                    validate: testEmail,
                    required: true
                })}
                error={getError('email', {
                    validate: emailErrorMessage,
                    pattern: 'Пожалуйста, введите корректную почту'
                })}
            />

            <div>
                <button type='submit' className='btn btn-primary' disabled={loading || getErrorsCount(errors) !== 0}>Сохранить</button>
                {loading && <Spinner animation="border" role="status" className='ml-3 spinner-vertical-middle' />}
            </div>
        </form>
    );
}

const UpdatePasswordForm = () => {
    const { register, handleSubmit, errors, watch } = useForm();
    const [loading, setLoading] = useState(false);

    const getErrorMessage = (messages, name) => messages[name] ? messages[name] : '';
    const getError = (name, messages) => !errors[name] ? null : getErrorMessage(messages, errors[name].type);
    const checkSamePassword = value => {
        let newPasswordValue = watch('newPassword');
        return value === newPasswordValue;
    };

    const onSubmit = data => {
        delete data['confirmNewPassword'];
        setLoading(true);
        console.log(data)
    };

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <h4>Изменение пароля</h4>
            <InputField
                id='oldPasswordInput' type='password'
                name='oldPassword' title='Старый пароль'
                ref={register({ required: true })}
            />

            <InputField
                id='newPasswordInput' type='password'
                name='newPassword' title='Новый пароль'
                ref={register({ minLength: 8, required: true })}
                error={getError('newPassword', {
                    minLength: 'Пароль должен содержать более 8 символов'
                })}
            />

            <InputField
                id='newPasswordConfirmInput' type='password'
                name='confirmNewPassword' title='Подтвердите новый пароль'
                ref={register({ required: true, validate: checkSamePassword })}
                error={getError('confirmNewPassword', {
                    validate: 'Пароли не совпадают'
                })}
            />

            <div>
                <button type='submit' className='btn btn-primary' disabled={loading || getErrorsCount(errors) !== 0}>Сохранить</button>
                {loading && <Spinner animation="border" role="status" className='ml-3 spinner-vertical-middle' />}
            </div>
        </form>
    );
}

ReactDOM.render(<Application body={ProfilePageBody} />, document.getElementById('root'));
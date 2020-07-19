import './scss/style.scss';
import React, { useState } from 'react';
import HeaderNavBar from './HeaderNavBar';
import { getMetaProperty, InputField } from './misc';
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { useForm } from "react-hook-form";
import { fetcher } from "./api";
import Spinner from "react-bootstrap/Spinner";
import { ToastContainer, toast } from "react-toastify";

class Application extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            userAuthenticated: getMetaProperty('user:authenticated') === 'true'
        }
        APPLICATION_INSTANCE = this;
    }

    logout() {
        let that = this;
        fetcher('/logout', {
            method: 'POST',
            redirect: 'manual'
        }).then(() => {
            that.setState({ userAuthenticated: false  });
        }).catch(() => {
            that.setState({ userAuthenticated: false  });
        });
    }

    render() {
        const Body = this.props.body;
        const settings = { userAuthenticated: this.state.userAuthenticated };
        return (
            <>
                <HeaderNavBar {...settings} />
                <div className='container'>
                    <Body {...settings} />
                </div>
                <RegisterModal ref={REGISTER_MODAL_INSTANCE} />
                <ToastContainer
                    position="bottom-right"
                    autoClose={5000}
                    hideProgressBar={false}
                    newestOnTop={false}
                    closeOnClick
                    rtl={false}
                    pauseOnFocusLoss
                    draggable
                    pauseOnHover
                />
            </>
        );
    }
}

const RegisterModal = React.forwardRef((props, ref) => {
    let defaultEmailErrorMessage = 'Пользователь с таким Email адресом уже зарегистрирован';
    const [show, setShow] = useState(false);
    const [loading, setLoading] = useState(false);
    const [emailErrorMessage, setEmailErrorMessage] = useState(defaultEmailErrorMessage);
    const { register, handleSubmit, errors } = useForm();

    ref.current = {
        show: () => setShow(true),
        hide: () => setShow(false)
    };

    const submit = formData => {
        setLoading(true);
        fetcher('/users/register', {
            method: 'POST',
            body: JSON.stringify(formData)
        }).then(() => {
            setLoading(false);
            setShow(false);
            toast.success('Вы успешно зарегистрировались!');
        }).catch(e => {
            setLoading(false);
            e.useDefaultErrorParser();
        });
    };

    const getErrorMessage = (messages, name) => messages[name] ? messages[name] : '';
    const getError = (name, messages) => !errors[name] ? null : getErrorMessage(messages, errors[name].type);
    const testEmail = email => fetcher(`/users/test/email/${email}`)
        .then(response => {
            setEmailErrorMessage(defaultEmailErrorMessage);
            return response;
        })
        .catch(() => {
            setEmailErrorMessage('Ошибка проверки доступности почты! Пожалуйста, попробуйте позже.');
            return false;
        });

    let errorsCount = 0;
    for (let error in errors) {
        if (errors.hasOwnProperty(error)) {
            ++errorsCount;
        }
    }

    return (
        <Modal show={show} onHide={() => setShow(false)}>
            <Modal.Header closeButton>
                <Modal.Title>Регистрация</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <form onSubmit={() => null}>
                    <InputField
                        id='registerNameInput' type='text'
                        name='name' placeholder='Петр' title='Имя'
                        ref={register({ minLength: 3, maxLength: 30, required: true })}
                        error={getError('name', {
                            minLength: 'Имя должно содержать хотя-бы 3 символа',
                            maxLength: 'Имя должно быть меньше 30 символов'
                        })}
                        />

                    <InputField
                        id='registerSurnameInput' type='text'
                        name='surname' placeholder='Васильевич' title='Фамилия'
                        ref={register({ minLength: 3, maxLength: 30, required: true })}
                        error={getError('surname', {
                            minLength: 'Фамилия должна содержать хотя-бы 3 символа',
                            maxLength: 'Фамилия должна быть меньше 30 символов'
                        })}
                        />

                    <InputField
                        id='registerEmailAddressInput' type='email'
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

                    <InputField
                        id='registerPasswordInput' type='password' title='Пароль'
                        name='password'
                        ref={register({ minLength: 8, required: true })}
                        error={getError('password', {
                            minLength: 'Пароль должен содержать хотя-бы 8 символов'
                        })}
                        />
                </form>
            </Modal.Body>
            <Modal.Footer>
                {loading && <Spinner animation="border" role="status" />}
                <Button variant="secondary" onClick={() => setShow(false)}>Закрыть</Button>
                <Button variant="primary" disabled={loading || errorsCount !== 0} onClick={handleSubmit(submit)}>Зарегистрироваться</Button>
            </Modal.Footer>
        </Modal>
    );
});

export let REGISTER_MODAL_INSTANCE = React.createRef();
export let APPLICATION_INSTANCE;

export default Application;
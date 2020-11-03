import React, {useState} from 'react';
import ReactDOM from 'react-dom';
import Application from "./components/Application";
import {getErrorsCount, getMetaProperty, InputField} from "./components/misc";
import {useForm} from "react-hook-form";
import Spinner from "react-bootstrap/Spinner";
import {Button} from 'react-bootstrap';
import Alert from "react-bootstrap/Alert";
import {Variant} from "react-bootstrap/types";

const LoginPage = () => {
    const gatewayServiceUrl = getMetaProperty('service:gateway:url');
    return (
        <Application miniApplication={true} indexPage={gatewayServiceUrl}>
            <LoginPanel gatewayServiceUrl={gatewayServiceUrl} />
        </Application>
    );
}

const LoginPanel = (props: { gatewayServiceUrl: string }) => (
    <div className='jumbotron mt-3'>
        {location.search.includes('logout') && <SuccessLoggedOutMessage />}
        {location.search.includes('error') && <LoginErrorMessage />}
        <h3>Вход</h3>
        <LoginForm gatewayServiceUrl={props.gatewayServiceUrl} />
    </div>
);

const LoginForm = (props: { gatewayServiceUrl: string }) => {
    const [loading, setLoading] = useState(false);
    const { register, errors, handleSubmit } = useForm({ mode: 'onChange' });

    const getErrorMessage = (messages: Record<string, string>, name: string) => messages[name] ? messages[name] : '';
    const getError = (name: string, messages: Record<string, string>) => !errors[name] ? null : getErrorMessage(messages, errors[name].type);

    const onSubmit = (data: any, e: React.BaseSyntheticEvent) => {
        setLoading(true);
        e.target.submit();
    };

    return (
        <form action={getMetaProperty('forms:login:action')} method='post' onSubmit={handleSubmit(onSubmit)}>
            <input type='hidden' name={getMetaProperty('_csrf_parameter')} value={getMetaProperty('_csrf')} />

            <InputField
                title='Логин'
                name='username'
                type='text'
                ref={register({ required: true })} />

            <InputField
                title='Пароль'
                name='password'
                type='password'
                ref={register({ required: true, minLength: 8 })}
                error={getError('password', {
                    minLength: 'Пароль должен содержать хотя-бы 8 символов',
                })} />

            <div className='mb-3'>
                <a href={props.gatewayServiceUrl + '#registration'}>Регистрация</a>
            </div>

            <div>
                <Button type='submit' disabled={getErrorsCount(errors) !== 0 || loading}>Войти</Button>
                {loading && <Spinner animation="border" role="status" className='ml-3 spinner-vertical-middle' />}
            </div>
        </form>
    );
}

const SuccessLoggedOutMessage = () => <LoginMessage variant='success'
                                                    text='Вы успешно вышли из аккаунта!' />;

const LoginErrorMessage = () => <LoginMessage variant='danger'
                                              text={'Ошибка входа! Возможно, вы ввели неверный логин или пароль.'} />;

interface LoginMessageProps {
    text: string;
    variant: Variant;
}

const LoginMessage = (props: LoginMessageProps) => {
    const [show, setShow] = useState(true);
    const handleClose = () => {
        setShow(false);
        location.search = '';
    };

    return <Alert
                show={show}
                onClose={handleClose}
                variant={props.variant}
                dismissible={true}
                children={props.text} />
}

ReactDOM.render(<LoginPage />, document.getElementById('root'));
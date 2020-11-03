import React, {FunctionComponent, useEffect, useState} from 'react';
import HeaderNavBar from './HeaderNavBar';
import { getErrorsCount, InputField } from './misc';
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { useForm } from "react-hook-form";
import { fetcher } from "./api";
import Spinner from "react-bootstrap/Spinner";
import { Provider } from "react-redux";
import createStore from './store';
import { ToastContainer, toast } from "react-toastify";
import { ValidateResult } from "react-hook-form/dist/types/form";
import { ReducersMapObject, Store } from "redux";
const AUTHENTICATED_USERS_ONLY_PAGES = ['/profile', '/admin/users', '/admin/movies/add'];
const onStoreInitialized = () => {
    store.subscribe(() => {
        let state = store.getState();
        if (state.user === null && AUTHENTICATED_USERS_ONLY_PAGES.includes(location.pathname)) {
            location.href = '/';
        }
    });
};
let store: Store = null;

const Application: FunctionComponent<{ additionalStore?: ReducersMapObject, miniApplication?: boolean, indexPage?: string }> = ({ children, additionalStore, miniApplication  = false, indexPage = '/' }) => {
    if (store === null) {
        initializeStore(additionalStore);
    }
    return (
        <Provider store={store}>
            <HeaderNavBar indexPage={indexPage} miniApplication={miniApplication} />
            <div className='container'>
                {children}
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
        </Provider>
    )
}

function initializeStore(additionalStore?: ReducersMapObject) {
    store = createStore(additionalStore);
    onStoreInitialized();
}

interface RegisterUser {
    name: string;
    surname: string;
    email: string;
    password: string;
}

interface RegisterModalRef {
    show: () => void;
    hide: () => void;
}

const RegisterModal = React.forwardRef((props, ref: { current: RegisterModalRef }) => {
    let defaultEmailErrorMessage = 'Пользователь с таким Email адресом уже зарегистрирован';
    const [show, setShow] = useState(false);
    const [loading, setLoading] = useState(false);
    const [emailErrorMessage, setEmailErrorMessage] = useState(defaultEmailErrorMessage);
    const { register, handleSubmit, errors } = useForm<RegisterUser>();

    useEffect(() => {
        if (location.hash === '#registration') {
            setTimeout(() => {
                setShow(true);
                location.hash = '';
            }, 500);
        }
    }, []);

    ref.current = {
        show: () => setShow(true),
        hide: () => setShow(false)
    };

    const submit = (formData: RegisterUser) => {
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

    const testEmail = (email: string): Promise<ValidateResult> => fetcher(`/users/test/email/${email}`)
        .then((response) => {
            setEmailErrorMessage(defaultEmailErrorMessage);
            return response as unknown as boolean;
        })
        .catch(() => {
            setEmailErrorMessage('Ошибка проверки доступности почты! Пожалуйста, попробуйте позже.');
            return false;
        });

    const getError = (type: string, messages: Record<string, string>) => messages[type];

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
                        error={getError(errors.name?.type, {
                            minLength: 'Имя должно содержать хотя-бы 3 символа',
                            maxLength: 'Имя должно быть меньше 30 символов'
                        })}
                        />

                    <InputField
                        id='registerSurnameInput' type='text'
                        name='surname' placeholder='Васильевич' title='Фамилия'
                        ref={register({ minLength: 3, maxLength: 30, required: true })}
                        error={getError(errors.surname?.type, {
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
                            required: true,
                        })}
                        error={getError(errors.email?.type, {
                            validate: emailErrorMessage,
                            pattern: 'Пожалуйста, введите корректную почту'
                        })}
                        />

                    <InputField
                        id='registerPasswordInput' type='password' title='Пароль'
                        name='password'
                        ref={register({ minLength: 8, required: true })}
                        error={getError(errors.password?.type, {
                            minLength: 'Пароль должен содержать хотя-бы 8 символов'
                        })}
                        />
                </form>
            </Modal.Body>
            <Modal.Footer>
                {loading && <Spinner animation="border" role="status" />}
                <Button variant="secondary" onClick={() => setShow(false)}>Закрыть</Button>
                <Button variant="primary" disabled={loading || getErrorsCount(errors) !== 0} onClick={handleSubmit(submit)}>Зарегистрироваться</Button>
            </Modal.Footer>
        </Modal>
    );
});

export let REGISTER_MODAL_INSTANCE = React.createRef<RegisterModalRef>();
export default Application;
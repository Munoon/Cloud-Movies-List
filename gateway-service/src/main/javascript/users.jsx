import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import ReactDOMServer from 'react-dom/server';
import Application from './components/Application';
import { useTable, usePagination } from "react-table";
import Table from "react-bootstrap/Table";
import { fetcher } from "./components/api";
import Pagination from "react-bootstrap/Pagination";
import Dropdown from "react-bootstrap/Dropdown";
import Spinner from "react-bootstrap/Spinner";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import FormCheck from "react-bootstrap/FormCheck";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPen } from "@fortawesome/free-solid-svg-icons/faPen";
import useSWR from "swr";
import { useForm } from "react-hook-form";
import { InputField } from "./components/misc";
import { toast } from "react-toastify";

const getUsers = (page, size) => fetcher('/users/admin/list', { params: { page, size } });

const UsersPage = () => (
    <Application>
        <UsersTable ref={USERS_TABLE_INSTANCE} />
        <UpdateUser ref={ref => UPDATE_USER_MODAL_INSTANCE = ref} />
        <RegisterUserModal ref={REGISTER_USER_MODAL_INSTANCE} />
    </Application>
);

const UsersTable = React.forwardRef((props, ref) => {
    const columns = React.useMemo(() => [
        { Header: 'ID', accessor: 'id' },
        { Header: 'Имя', accessor: 'name' },
        { Header: 'Фамилия', accessor: 'surname' },
        { Header: 'Email', accessor: 'email' },
        { Header: 'Роли', accessor: 'roles', Cell: data => data.value.join(', ') }
    ], []);

    const [loading, setLoading] = useState(false);
    const [data, setData] = useState({
        users: [],
        pages: 0
    });

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        page,
        prepareRow,

        canPreviousPage,
        canNextPage,
        pageCount,
        gotoPage,
        nextPage,
        previousPage,
        setPageSize,
        state: { pageIndex, pageSize }
    } = useTable({
        columns,
        data: data.users,
        pageCount: data.pages,
        manualPagination: true,
        initialState: { pageIndex: 0, pageSize: 10 }
    }, usePagination);

    useEffect(() => loadData(), [ pageIndex, pageSize ]);

    const loadData = () => {
        setLoading(true);
        getUsers(pageIndex, pageSize)
            .then(response => setData({
                users: response.content,
                pages: response.page.totalPages
            }))
            .then(() => setLoading(false))
            .catch(e => {
                e.useDefaultErrorParser();
                setLoading(false);
            });
    };

    ref.current = {
        refresh: () => loadData()
    };

    return (
        <div className='mt-2'>
            <div className='row'>
                <Pagination>
                    <Pagination.First onClick={() => gotoPage(0)} disabled={!canPreviousPage} />
                    <Pagination.Prev onClick={() => previousPage()} disabled={!canPreviousPage} />

                    <Pagination.Item className='span-default-color' disabled>{pageIndex + 1}</Pagination.Item>

                    <Pagination.Next onClick={() => nextPage()} disabled={!canNextPage} />
                    <Pagination.Last onClick={() => gotoPage(pageCount - 1)} disabled={!canNextPage} />
                </Pagination>

                <Dropdown className='ml-2'>
                    <Dropdown.Toggle variant='secondary' id='users-table-count-dropdown'>Показывать {pageSize}</Dropdown.Toggle>

                    <Dropdown.Menu>
                        <Dropdown.Item active={pageSize === 5} onClick={() => setPageSize(5)}>Показывать 5</Dropdown.Item>
                        <Dropdown.Item active={pageSize === 10} onClick={() => setPageSize(10)}>Показывать 10</Dropdown.Item>
                        <Dropdown.Item active={pageSize === 15} onClick={() => setPageSize(15)}>Показывать 15</Dropdown.Item>
                        <Dropdown.Item active={pageSize === 20} onClick={() => setPageSize(20)}>Показывать 20</Dropdown.Item>
                    </Dropdown.Menu>
                </Dropdown>

                <div>
                    <Button onClick={() => loadData()} disabled={loading} variant='secondary' className='ml-2 mb-3'>Обновить</Button>
                    <Button onClick={() => REGISTER_USER_MODAL_INSTANCE.current.show()} variant='secondary' className='ml-2 mb-3'>Добавить</Button>
                    {loading && <Spinner animation="border" role="status" className='ml-2 mb-3 spinner-vertical-middle' />}
                </div>
            </div>

            <Table {...getTableProps()}>
                <thead>
                {headerGroups.map(headerGroup => (
                    <tr {...headerGroup.getHeaderGroupProps()}>
                        {headerGroup.headers.map(column => (
                            <th {...column.getHeaderProps()}>{column.render('Header')}</th>
                        ))}
                        <th/>
                    </tr>
                ))}
                </thead>
                <tbody {...getTableBodyProps()}>
                {page.map((row) => {
                    prepareRow(row);
                    let userId = row.original.id;
                    let name = row.original.name + ' ' + row.original.surname;
                    return (
                        <tr key={row.original.id} role='row'>
                            {row.cells.map(cell => (
                                <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                            ))}
                            <td>
                                <FontAwesomeIcon icon={faPen} onClick={() => UPDATE_USER_MODAL_INSTANCE.updateUser(userId)} className='custom-button' />
                            </td>
                        </tr>
                    );
                })}
                </tbody>
            </Table>
        </div>
    );
});

class UpdateUser extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            userId: null
        };

        this.updateUser = this.updateUser.bind(this);
    }

    updateUser(userId) {
        this.setState({ userId });
    }

    render() {
        return this.state.userId
            ? <UpdateUserModal userId={this.state.userId} onHide={() => this.setState({ userId: null })} />
            : <></>;
    }
}

function UpdateUserModal({ userId, onHide }) {
    const { data: user, error } = useSWR(`/users/admin/${userId}`, fetcher);

    let modalBody = null;
    let modalTitle = '';
    let loading;

    if (error) {
        loading = false;
        modalBody = <p>Ошибка загрузки пользователя!</p>
        modalTitle = 'Ошибка';
    } else if (!user) {
        loading = true;
        modalBody = <p>Загрузка...</p>;
        modalTitle = 'Загрузка...';
    } else {
        loading = false;
        modalTitle = `Обновить: ${user.name} ${user.surname}`;
    }

    const settings = { user, onHide, modalTitle, body: modalBody, loading, saveAble: !error && user !== null };
    return <UserModal {...settings} />
}

const RegisterUserModal = React.forwardRef((props, ref) => {
    const [show, setShow] = useState(false);
    ref.current = {
        show: () => setShow(true)
    };
    return show ? <UserModal modalTitle='Создать пользователя' onHide={() => setShow(false)} /> : <></>;
});

function UserModal({ user = null, onHide = () => null, modalTitle, body = null, loading = false, saveAble = true }) {
    const defaultEmailErrorMessage = 'Пользователь с таким Email адресом уже зарегистрирован';
    const [emailErrorMessage, setEmailErrorMessage] = useState(defaultEmailErrorMessage);

    const { register, handleSubmit, errors } = useForm();
    const [requestLoading, setRequestLoading] = useState(false);
    const [show, setShow] = useState(true);

    const getErrorMessage = (messages, name) => messages[name] ? messages[name] : '';
    const getError = (name, messages) => !errors[name] ? null : getErrorMessage(messages, errors[name].type);
    const testEmail = async email => email === (user && user.email) ? true
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
        setRequestLoading(true);

        const request = user ? fetcher(`/users/admin/${user.id}`, {
            method: 'PUT',
            body: JSON.stringify(data)
        }) : fetcher('/users/admin/create', {
            method: 'POST',
            body: JSON.stringify(data)
        });

        request.then(() => {
            let action = user ? 'обновили' : 'создали';
            toast.success(`Вы успешно ${action} пользователя!`);
            setShow(false);
            USERS_TABLE_INSTANCE.current.refresh();
        }).catch(e => {
            setRequestLoading(false);
            e.useDefaultErrorParser()
        });
    };

    const handleDeleteUser = () => {
        if (!user || !confirm(`Вы уверены, что хотите удалить пользователя ${user.name}?`)) {
            return;
        }

        fetcher(`/users/admin/${user.id}`, { method: 'DELETE' })
            .then(() => {
                setShow(false);
                toast.success('Вы успешно удалили пользователя!');
                USERS_TABLE_INSTANCE.current.refresh();
            })
            .catch(e => e.useDefaultErrorParser());
    };

    let workUser = user ? user : { roles: ['ROLE_USER'] };
    let modalBody = body ? body : (
        <>
            <InputField
                id='updateUserNameInput' type='text'
                name='name' placeholder='Петр' title='Имя' value={workUser.name}
                ref={register({ minLength: 3, maxLength: 30, required: true })}
                error={getError('name', {
                    minLength: 'Имя должно содержать хотя-бы 3 символа',
                    maxLength: 'Имя должно быть меньше 30 символов'
                })}
            />

            <InputField
                id='updateUsernSurnameInput' type='text'
                name='surname' placeholder='Васильевич' title='Фамилия' value={workUser.surname}
                ref={register({ minLength: 3, maxLength: 30, required: true })}
                error={getError('surname', {
                    minLength: 'Фамилия должна содержать хотя-бы 3 символа',
                    maxLength: 'Фамилия должна быть меньше 30 символов'
                })}
            />

            <InputField
                id='updateUserEmailAddressInput' type='email' value={workUser.email}
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

            {!user && (
                <InputField
                    id='updateUserPasswordInput' type='password' title='Пароль'
                    name='password'
                    ref={register({ minLength: 8, required: true })}
                    error={getError('password', {
                        minLength: 'Пароль должен содержать хотя-бы 8 символов'
                    })}
                />
            )}

            <div className="mb-3">
                <label className="form-label">Роли</label>

                <FormCheck
                    custom type='checkbox' id='updateUserUserRoleForm'
                    label='ROLE_USER' value='ROLE_USER'
                    name='roles' ref={register({
                    validate: data => data.length > 0
                })}
                    defaultChecked={workUser.roles.includes('ROLE_USER')}
                />

                <FormCheck
                    custom type='checkbox'
                    id='updateUserAdminRoleForm'
                    label='ROLE_ADMIN'
                    value='ROLE_ADMIN'
                    name='roles' ref={register()}
                    defaultChecked={workUser.roles.includes('ROLE_ADMIN')}
                />
            </div>
        </>
    );

    return (
        <Modal show={show} onHide={() => setShow(false)} onExited={onHide}>
            <Modal.Header closeButton>
                <Modal.Title>{modalTitle}</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                {modalBody}
            </Modal.Body>

            <Modal.Footer>
                {(loading || requestLoading) && <Spinner animation="border" role="status" className='ml-3 spinner-vertical-middle' />}
                <Button variant='secondary' onClick={() => setShow(false)}>Закрыть</Button>
                {user && <Button variant='danger' onClick={() => handleDeleteUser()}>Удалить</Button>}
                <Button variant='primary' disabled={!saveAble || requestLoading} onClick={handleSubmit(onSubmit)}>Сохранить</Button>
            </Modal.Footer>
        </Modal>
    );
}

let USERS_TABLE_INSTANCE = React.createRef();
let UPDATE_USER_MODAL_INSTANCE = null;
let REGISTER_USER_MODAL_INSTANCE = React.createRef();

if (!window.isServer) {
    ReactDOM.hydrate(<UsersPage/>, document.getElementById('root'));
} else {
    window.renderServer = () => ReactDOMServer.renderToString(<UsersPage />);
}
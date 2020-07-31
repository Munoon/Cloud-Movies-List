import React, { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
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
import { faTimes } from "@fortawesome/free-solid-svg-icons/faTimes";
import useSWR from "swr";
import { useForm } from "react-hook-form";
import { InputField } from "./components/misc";
import { toast } from "react-toastify";

const getUsers = (page, size) => fetcher(`/users/admin/list?page=${page}&size=${size}`);

const body = () => (
    <>
        <UsersTable ref={USERS_TABLE_INSTANCE} />
        <UpdateUser ref={ref => UPDATE_USER_MODAL_INSTANCE = ref} />
    </>
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
        manualPagination: true,
        pageCount: 3,
        initialState: { pageIndex: data.pages, pageSize: 10 }
    }, usePagination);

    useEffect(() => loadData(), [ pageIndex, pageSize ]);

    const loadData = () => {
        setLoading(true);
        getUsers(pageIndex, pageSize)
            .then(response => setData({
                users: response['_embedded'].users,
                pages: response.page.totalPages
            }))
            .then(() => setLoading(false));
    };

    ref.current = {
        refresh: () => loadData()
    };

    const handleDeleteUser = (userId, name) => {
        if (!confirm(`Вы уверены, что хотите удалить пользователя ${name}?`)) {
            return;
        }

        fetcher(`/users/admin/${userId}`, { method: 'DELETE' })
            .then(() => {
                toast.success('Вы успешно удалили пользователя!');
                loadData();
            })
            .catch(e => e.useDefaultErrorParser());
    }

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
                            <td>
                                <FontAwesomeIcon icon={faTimes} onClick={() => handleDeleteUser(userId, name)} className='custom-button' />
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
    const defaultEmailErrorMessage = 'Пользователь с таким Email адресом уже зарегистрирован';
    const [emailErrorMessage, setEmailErrorMessage] = useState(defaultEmailErrorMessage);
    const [show, setShow] = useState(true);
    const [requestLoading, setRequestLoading] = useState(false);
    const { register, handleSubmit, errors } = useForm();

    const { data: user, error } = useSWR(`/users/admin/${userId}`, fetcher);

    const getErrorMessage = (messages, name) => messages[name] ? messages[name] : '';
    const getError = (name, messages) => !errors[name] ? null : getErrorMessage(messages, errors[name].type);
    const testEmail = async email => email === user.email ? true
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
        fetcher(`/users/admin/${userId}`, {
            method: 'PUT',
            body: JSON.stringify(data)
        }).then(() => {
            toast.success('Вы успешно обновили пользователя!');
            setShow(false);
            USERS_TABLE_INSTANCE.current.refresh();
        }).catch(e => e.useDefaultErrorParser());
    }

    let modalBody = <></>;
    let modalTitle = '';
    let loading;

    if (error) {
        loading = false;
        modalBody = <p>Ошибка загрузки пользователя!</p>
        modalTitle = 'Ошибка';
    } else if (!user) {
        loading = true;
        modalTitle = 'Загрузка...';
    } else {
        loading = false;
        modalTitle = `Обновить: ${user.name} ${user.surname}`;
        modalBody = (
            <>
                <InputField
                    id='updateUserNameInput' type='text'
                    name='name' placeholder='Петр' title='Имя' value={user.name}
                    ref={register({ minLength: 3, maxLength: 30, required: true })}
                    error={getError('name', {
                        minLength: 'Имя должно содержать хотя-бы 3 символа',
                        maxLength: 'Имя должно быть меньше 30 символов'
                    })}
                />

                <InputField
                    id='updateUsernSurnameInput' type='text'
                    name='surname' placeholder='Васильевич' title='Фамилия' value={user.surname}
                    ref={register({ minLength: 3, maxLength: 30, required: true })}
                    error={getError('surname', {
                        minLength: 'Фамилия должна содержать хотя-бы 3 символа',
                        maxLength: 'Фамилия должна быть меньше 30 символов'
                    })}
                />

                <InputField
                    id='updateUserEmailAddressInput' type='email' value={user.email}
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

                <div className="mb-3">
                    <label className="form-label">Роли</label>

                    <FormCheck
                        custom type='checkbox' id='updateUserUserRoleForm'
                        label='ROLE_USER' value='ROLE_USER'
                        name='roles' ref={register({
                            validate: data => data.length > 0
                        })}
                        defaultChecked={user.roles.includes('ROLE_USER')}
                    />

                    <FormCheck
                        custom type='checkbox'
                        id='updateUserAdminRoleForm'
                        label='ROLE_ADMIN'
                        value='ROLE_ADMIN'
                        name='roles' ref={register()}
                        defaultChecked={user.roles.includes('ROLE_ADMIN')}
                    />
                </div>
            </>
        )
    }

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
                <Button variant='secondary'>Закрыть</Button>
                <Button variant='primary' disabled={error || !user} onClick={handleSubmit(onSubmit)}>Сохранить</Button>
            </Modal.Footer>
        </Modal>
    );
}

let USERS_TABLE_INSTANCE = React.createRef();
let UPDATE_USER_MODAL_INSTANCE = null;

ReactDOM.render(<Application body={body} />, document.getElementById('root'));
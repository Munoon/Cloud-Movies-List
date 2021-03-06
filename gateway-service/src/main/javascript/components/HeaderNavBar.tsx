import React from 'react';
import {hasRole} from './misc';
import {REGISTER_MODAL_INSTANCE} from './Application';
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import NavDropdown from "react-bootstrap/NavDropdown";
import Button from "react-bootstrap/Button";
import {connect} from "react-redux";
import {removeUser, User} from "./store/user";
import {fetcher} from "./api";
import {DropdownItemProps} from "react-bootstrap/DropdownItem";
import SearchMovieForm from "./SearchMovieForm";

const connectUserProp = (state: { user: User }) => ({ user: state.user });

const HeaderNavBar = connect(connectUserProp)((props: { user: User, miniApplication: boolean, indexPage: string }) => {
    let userAuthenticated = props.user !== null;
    let menuButtons;
    if (props.miniApplication && userAuthenticated) {
        menuButtons = (
            <Navbar.Collapse>
                <Nav className="mr-auto">
                    <SmallUserInformation />
                </Nav>
            </Navbar.Collapse>
        );
    }
    if (!props.miniApplication) {
        menuButtons = (
            <>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse>
                    <Nav className="mr-auto">
                        {userAuthenticated && hasRole('ROLE_ADMIN') && <AdminNavItem />}
                        <UserNavBarItem userAuthenticated={userAuthenticated} />
                    </Nav>
                </Navbar.Collapse>
                <SearchMovieForm />
            </>
        );
    }
    return (
        <Navbar bg="dark" expand="lg" className='navbar-dark'>
            <div className='container'>
                <Navbar.Brand href={props.indexPage} className='text-white'>Movies List</Navbar.Brand>
                {menuButtons}
            </div>
        </Navbar>
    );
});

const UserNavBarItem = ({ userAuthenticated }: { userAuthenticated: boolean }) => userAuthenticated
    ? <ProfileUserNavBarItem />
    : <LoginUserNavBarItem />;

const ProfileUserNavBarItem = connect(connectUserProp, { removeUser })((props: { user: User, removeUser: typeof removeUser }) => {
    const onLogout = (e: React.MouseEvent<DropdownItemProps>) => {
        e.preventDefault();

        fetcher('/logout', {
            method: 'POST',
            redirect: 'manual'
        })
            .then(() => props.removeUser())
            .catch(() => props.removeUser());
    };

    return (
        <NavDropdown title={`${props.user.name} ${props.user.surname}`}
                     id="userNavbarDropdown" className='text-white'>
            <NavDropdown.Item disabled>{props.user.email}</NavDropdown.Item>
            <NavDropdown.Item href='/profile'>Настройки профиля</NavDropdown.Item>
            <NavDropdown.Divider />
            <NavDropdown.Item onClick={onLogout}>Выйти</NavDropdown.Item>
        </NavDropdown>
    )
});

const SmallUserInformation = connect(connectUserProp, null)((props: { user: User }) => (
    <NavDropdown title={`${props.user.name} ${props.user.surname}`}
                 id="userNavbarDropdown" className='text-white'>
        <NavDropdown.Item disabled>{props.user.email}</NavDropdown.Item>
    </NavDropdown>
));

const LoginUserNavBarItem = () => (
    <>
        <Nav.Link onClick={() => REGISTER_MODAL_INSTANCE.current.show()}>
            <Button variant="outline-success">Зарегистрироваться</Button>
        </Nav.Link>
        <Nav.Link href="/login">
            <Button variant="outline-primary">Войти</Button>
        </Nav.Link>
    </>
);

const AdminNavItem = () => (
    <NavDropdown title='Админ панель' id="adminNavbarDropdown" className='text-white'>
        <NavDropdown.Item href='/admin/users'>Пользователи</NavDropdown.Item>
        <NavDropdown.Divider />
        <NavDropdown.Item href='/admin/movies/add'>Добавить фильм</NavDropdown.Item>
    </NavDropdown>
);

export default HeaderNavBar;
import React from 'react';
import {getMetaProperty, hasRole} from './misc';
import { REGISTER_MODAL_INSTANCE, APPLICATION_INSTANCE } from './Application';
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import NavDropdown from "react-bootstrap/NavDropdown";
import Button from "react-bootstrap/Button";

const HeaderNavBar = ({ userAuthenticated }) => (
    <Navbar bg="dark" expand="lg" className='navbar-dark'>
        <div className='container'>
            <Navbar.Brand href="/" className='text-white'>Movies List</Navbar.Brand>
            <Navbar.Toggle aria-controls="basic-navbar-nav" />
            <Navbar.Collapse>
                <Nav className="mr-auto">
                    {userAuthenticated && hasRole('ROLE_ADMIN') && <AdminNavItem />}
                    <UserNavBarItem userAuthenticated={userAuthenticated} />
                </Nav>
            </Navbar.Collapse>
        </div>
    </Navbar>
);

const UserNavBarItem = ({ userAuthenticated }) => userAuthenticated
    ? <ProfileUserNavBarItem />
    : <LoginUserNavBarItem />;

const ProfileUserNavBarItem = () => (
        <NavDropdown title={getMetaProperty('user:name') + ' ' + getMetaProperty('user:surname')}
                     id="userNavbarDropdown" className='text-white'>
            <NavDropdown.Item href='/profile'>Настройки профиля</NavDropdown.Item>
            <NavDropdown.Divider />
            <NavDropdown.Item onClick={() => APPLICATION_INSTANCE.logout()}>Выйти</NavDropdown.Item>
        </NavDropdown>
);

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
    </NavDropdown>
);

export default HeaderNavBar;
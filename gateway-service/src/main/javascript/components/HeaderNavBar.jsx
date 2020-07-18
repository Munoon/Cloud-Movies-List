import React from 'react';
import { getMetaProperty, postForm }  from './misc';
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
                    <UserNavBarItem userAuthenticated={userAuthenticated} />
                </Nav>
            </Navbar.Collapse>
        </div>
    </Navbar>
);

const UserNavBarItem = ({ userAuthenticated }) => userAuthenticated
    ? <ProfileUserNavBarItem />
    : <LoginUserNavBarItem />;

const ProfileUserNavBarItem = () => {
    return (
        <NavDropdown title={getMetaProperty('user:name') + ' ' + getMetaProperty('user:surname')}
                     id="userNavbarDropdown" className='text-white'>
            <NavDropdown.Item onClick={() => APPLICATION_INSTANCE.logout()}>Выйти</NavDropdown.Item>
        </NavDropdown>
    );
}

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

export default HeaderNavBar;
import React from 'react';
import { getMetaProperty }  from './misc';
import Navbar from "react-bootstrap/Navbar";
import Nav from "react-bootstrap/Nav";
import NavDropdown from "react-bootstrap/NavDropdown";

const HeaderNavBar = ({ userAuthenticated }) => (
    <Navbar bg="light" expand="lg" style={{ padding: '15px' }}>
        <Navbar.Brand href="/">Movies List</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse>
            <Nav className="mr-auto">
                <UserNavBarItem userAuthenticated={userAuthenticated} />
            </Nav>
        </Navbar.Collapse>
    </Navbar>
);

const UserNavBarItem = ({ userAuthenticated }) => userAuthenticated
    ? <ProfileUserNavBarItem />
    : <LoginUserNavBarItem />;

const ProfileUserNavBarItem = () => {
    return (
        <NavDropdown title={getMetaProperty('user:name') + ' ' + getMetaProperty('user:surname')} id="userNavbarDropdown">
            <NavDropdown.Item className='disabled'>{getMetaProperty('user:email')}</NavDropdown.Item>
            <NavDropdown.Divider />
            <NavDropdown.Item href="/logout">Выйти</NavDropdown.Item>
        </NavDropdown>
    );
}

const LoginUserNavBarItem = () => (
    <Nav.Link href="/login">Войти</Nav.Link>
);

export default HeaderNavBar;
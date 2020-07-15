import React from 'react';
import 'bootstrap/js/dist/dropdown';
import { getMetaProperty }  from './misc';

const HeaderNavBar = ({ userAuthenticated }) => (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
        <div className="container-fluid">
            <a className="navbar-brand" href="/">Movies List</a>
            <button className="navbar-toggler" type="button" data-toggle="collapse"
                    data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span className="navbar-toggler-icon"/>
            </button>
            <div className="collapse navbar-collapse d-flex">
                <ul className="navbar-nav mr-auto mb-2 mb-lg-0">
                    <UserNavBarItem {...{ userAuthenticated }} />
                </ul>
            </div>
        </div>
    </nav>
);

const UserNavBarItem = ({ userAuthenticated }) => userAuthenticated
    ? <ProfileUserNavBarItem />
    : <LoginUserNavBarItem />;

const ProfileUserNavBarItem = () => {
    return (
        <li className="nav-item dropdown">
            <a className="nav-link dropdown-toggle" href="#" id="userNavbarDropdown" role="button"
               data-toggle="dropdown" aria-expanded="false">
                {getMetaProperty('user:name')} {getMetaProperty('user:surname')}
            </a>
            <ul className="dropdown-menu" aria-labelledby="userNavbarDropdown">
                <li>
                    <a className="dropdown-item disabled" href="#" aria-disabled="true">{getMetaProperty('user:email')}</a>
                </li>
                <li>
                    <a className="dropdown-item" href="/logout">Выйти</a>
                </li>
            </ul>
        </li>
    );
}

const LoginUserNavBarItem = () => (
    <li className="nav-item">
        <a className="nav-link active" aria-current="page" href="/login">Войти</a>
    </li>
);

export default HeaderNavBar;
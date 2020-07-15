import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import HeaderNavBar from './HeaderNavBar';

const getMetaProperty = name => document.querySelector(`meta[property="${name}"]`).content;

const Application = ({ body: Body }) => {
    const userAuthenticated = getMetaProperty('user:authenticated') === 'true';
    let user = null;
    if (userAuthenticated) {
        user = {
            email: getMetaProperty('user:email')
        };
    }

    const settings = { userAuthenticated, user };
    return (
        <>
            <HeaderNavBar {...settings} />
            <Body {...settings} />
        </>
    );
}

export default Application;
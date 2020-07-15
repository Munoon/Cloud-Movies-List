import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import HeaderNavBar from './HeaderNavBar';
import { getMetaProperty } from './misc';

const Application = ({ body: Body }) => {
    const userAuthenticated = getMetaProperty('user:authenticated') === 'true';

    const settings = { userAuthenticated };
    return (
        <>
            <HeaderNavBar {...settings} />
            <Body {...settings} />
        </>
    );
}

export default Application;
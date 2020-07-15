import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';

const body = ({ userAuthenticated }) => (
    <h1>userAuthenticated = {userAuthenticated ? 'true' : 'false'}</h1>
);

ReactDOM.render(<Application body={body} />, document.getElementById('root'));
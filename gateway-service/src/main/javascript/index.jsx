import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import {connect} from "react-redux";

const MainPage = () => (
    <Application>
        <MainPageIndex />
    </Application>
);

const MainPageIndex = connect(state => ({ user: state.user }))((props) => (
    <h1>userAuthenticated = {props.user !== null ? 'true' : 'false'}</h1>
));

ReactDOM.render(<MainPage />, document.getElementById('root'));
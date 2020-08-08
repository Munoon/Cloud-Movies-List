import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import {connect} from "react-redux";

const body = connect(state => ({ user: state.user }))((props) => (
    <h1>userAuthenticated = {props.user !== null ? 'true' : 'false'}</h1>
));

ReactDOM.render(<Application body={body} />, document.getElementById('root'));
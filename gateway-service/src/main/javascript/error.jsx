import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import { getMetaProperty } from "./components/misc";

const ErrorPage = () => (
    <Application miniApplication={getMetaProperty('application:mini') === 'true'}
                 indexPage={getMetaProperty('service:gateway:url')}>
        <div className='jumbotron mt-3 text-center'>
            <h4>Да-да, это ошибка</h4>
            <h1>{getMetaProperty('error:status')}</h1>
            <h3>{getMetaProperty('error:message')}</h3>
        </div>
    </Application>
);

document.addEventListener('DOMContentLoaded', () => {
    ReactDOM.render(<ErrorPage />, document.getElementById('root'));
});

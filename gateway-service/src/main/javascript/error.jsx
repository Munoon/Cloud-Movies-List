import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import { getMetaProperty } from "./components/misc";

const ErrorPage = () => {
    return (
        <Application>
            <div className='jumbotron mt-3 text-center'>
                <h4>Да-да, это ошибка</h4>
                <h1>{getMetaProperty('error:status')}</h1>
                <h3>{getMetaProperty('error:message')}</h3>
            </div>
        </Application>
    );
}

ReactDOM.render(<ErrorPage />, document.getElementById('root'));
import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';

const body = () => {
    return (
        <div className='jumbotron mt-3'>
            <h1>ID фильма: {CURRENT_MOVIE.id}</h1>
            <h2>Название: {CURRENT_MOVIE.name}</h2>
        </div>
    );
}

ReactDOM.render(<Application body={body} />, document.getElementById('root'));
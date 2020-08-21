import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';

const MoviePage = () => {
    return (
        <Application>
            <div className='jumbotron mt-3'>
                <h1>ID фильма: {CURRENT_MOVIE.id}</h1>
                <h2>Название: {CURRENT_MOVIE.name}</h2>
                {CURRENT_MOVIE.hasAvatar && (<img
                    src={`/movies/movie/${CURRENT_MOVIE.id}/avatar`}
                    alt={`${CURRENT_MOVIE.name} avatar`} />)}
            </div>
        </Application>
    );
}

ReactDOM.render(<MoviePage />, document.getElementById('root'));
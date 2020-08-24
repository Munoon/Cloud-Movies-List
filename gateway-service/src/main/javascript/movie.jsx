import React, {useEffect, useState} from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import {getMetaProperty} from "./components/misc";
import {MovieImage} from "./components/MoviesComponents";
import {fetcher, getFetcher} from "./components/api";
import useSWR from "swr";
import {toast} from "react-toastify";

const smallMovieInfo = {
    id: getMetaProperty('current_movie:id'),
    name: getMetaProperty('current_movie:name'),
    hasAvatar: getMetaProperty('current_movie:hasAvatar') === 'true',
    registered: getMetaProperty('current_movie:registered')
};

const getMovieQuery = `
    query ($movieId: ID) {
        movie(id: $movieId) {
            originalName, about, country, genres, premiere, age, time
        }
    }
`;

const MoviePage = () => {
    const { data, error } = useSWR('/movies/graphql', getFetcher({
        method: 'POST',
        body: JSON.stringify({
            query: getMovieQuery,
            variables: { movieId: smallMovieInfo.id }
        })
    }));

    const movie = data && data.data && data.data.movie ? data.data.movie : null;

    if (data && data.errors) {
        toast.error('Ошибка запроса фильма: ' + data.errors.map(error => error.message).join('; '));
    }

    if (error) {
        error.useDefaultErrorParser();
    }

    const tableData = movie ? [
        { title: 'Оригинальное название', value: movie.originalName },
        { title: 'Описание', value: movie.about },
        { title: 'Страна', value: movie.country },
        { title: 'Жанр', value: movie.genres.join(', ') },
        { title: 'Дата премьера', value: movie.premiere },
        { title: 'Возраствное ограничение', value: movie.age },
        { title: 'Длительность', value: movie.time }
    ] : []

    return (
        <Application>
            <div className='jumbotron mt-3'>
                <h1>ID фильма: {smallMovieInfo.id}</h1>
                <h2>Название: {smallMovieInfo.name}</h2>
                <MovieImage movie={smallMovieInfo} />

                <table>
                    <tbody>
                        {tableData.map(item => (
                            <tr key={item.title}>
                                <td>{item.title}</td>
                                <td>{item.value}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </Application>
    );
}

ReactDOM.render(<MoviePage />, document.getElementById('root'));
import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import {getMetaProperty} from "./components/misc";
import {MovieImage} from "./components/MoviesComponents";
import {movieGraphQLClient, parseGraphQLError} from "./components/api";
import useSWR from "swr";
import {gql} from "graphql-request/dist";

const smallMovieInfo = {
    id: getMetaProperty('current_movie:id'),
    name: getMetaProperty('current_movie:name'),
    hasAvatar: getMetaProperty('current_movie:hasAvatar') === 'true',
    registered: getMetaProperty('current_movie:registered')
};

const getMovieQuery = gql`
    query ($movieId: ID) {
        movie(id: $movieId) {
            originalName, about, country, genres, premiere, age, time
        }
    }
`;

const graphQLMovieRequest = movieId => movieGraphQLClient.request(getMovieQuery, { movieId });

const MoviePage = () => {
    const { data, error } = useSWR(smallMovieInfo.id, graphQLMovieRequest);

    if (error) {
        parseGraphQLError(error, { movie: 'Ошибка запроса информации о фильме' });
    }

    const movie = data && data.movie ? data.movie : null;

    const tableData = movie ? [
        { title: 'Оригинальное название', value: movie.originalName },
        { title: 'Описание', value: movie.about },
        { title: 'Страна', value: movie.country },
        { title: 'Жанр', value: movie.genres.join(', ') },
        { title: 'Дата премьера', value: movie.premiere },
        { title: 'Возрастное ограничение', value: movie.age },
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

document.addEventListener('DOMContentLoaded', () => {
    ReactDOM.render(<MoviePage />, document.getElementById('root'));
});

import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import { getMetaProperty } from "./components/misc";
import useSWR from "swr/esm/use-swr";
import { getFetcher } from "./components/api";
import { query } from "gql-query-builder";

const body = () => {
    const movieId = getMetaProperty('movie:id');
    const { data: response } = useSWR(`/movies/graphql`, getFetcher({
        method: 'POST',
        body: JSON.stringify(query({
            operation: 'movie',
            variables: { id: { type: 'ID', value: movieId } },
            fields: ['name']
        }))
    }));

    return (
        <div className='jumbotron mt-3'>
            <h1>ID фильма: {movieId}</h1>
            {response && (
                <h2>Название: {response.data.movie.name}</h2>
            )}
        </div>
    );
}

ReactDOM.render(<Application body={body} />, document.getElementById('root'));
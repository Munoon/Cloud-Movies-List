import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import { getMetaProperty } from "./components/misc";
import useSWR from "swr/esm/use-swr";
import { fetcher } from "./components/api";

const body = () => {
    let movieId = getMetaProperty('movie:id');
    const { data } = useSWR(`/movies/${movieId}`, fetcher);
    return (
        <div className='jumbotron mt-3'>
            <h1>ID фильма: {movieId}</h1>
            {data && (
                <h2>Название: {data.name}</h2>
            )}
        </div>
    );
}

ReactDOM.render(<Application body={body} />, document.getElementById('root'));
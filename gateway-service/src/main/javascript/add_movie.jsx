import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import { InputField } from "./components/misc";
import { fetcher } from "./components/api";
import { mutation } from "gql-query-builder";

const AddMovie = () => {
    const onSubmit = e => {
        e.preventDefault();
        let name = e.target.querySelector('input[name="name"]').value

        fetcher('/movies/graphql', {
            method: 'POST',
            body: JSON.stringify(mutation({
                operation: 'addMovie',
                variables: {
                    newMovie: {
                        value: { name },
                        type: 'CreateMovie',
                        required: true
                    }
                },
                fields: ['id']
            }))
        }).then(({ data: { addMovie: movie } }) => {
            location.href = `/movie/${movie.id}`
        });
    };

    return (
        <Application>
            <form className='jumbotron mt-3' onSubmit={onSubmit}>
                <h1>Добавить фильм</h1>
                <InputField type='text' name='name' title='Название' />
                <button className='btn btn-primary'>Создать</button>
            </form>
        </Application>
    );
}

ReactDOM.render(<AddMovie />, document.getElementById('root'));
import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import { InputField } from "./components/misc";
import { fetcher } from "./components/api";
import { mutation } from "gql-query-builder";

const AddMovie = () => {
    let avatarImageId = null;

    const onSubmit = e => {
        e.preventDefault();
        let name = e.target.querySelector('input[name="name"]').value

        fetcher('/movies/graphql', {
            method: 'POST',
            body: JSON.stringify(mutation({
                operation: 'addMovie',
                variables: {
                    newMovie: {
                        value: { name, avatarImageId },
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

    const addFileHandler = e => {
        const formData = new FormData()
        formData.append('file', e.target.files[0])

        fetcher('/movies/templates/create', {
            method: 'POST',
            body: formData,
            addContentTypeHeader: false
        }).then(data => avatarImageId = data.id)
            .catch(e => e.useDefaultErrorParser())
    };

    return (
        <Application>
            <form className='jumbotron mt-3' onSubmit={onSubmit}>
                <h1>Добавить фильм</h1>
                <InputField type='text' name='name' title='Название' />
                <button className='btn btn-primary'>Создать</button>
            </form>

            <form>
                <input type="file" name='file' onChange={addFileHandler} />
            </form>
        </Application>
    );
}

ReactDOM.render(<AddMovie />, document.getElementById('root'));
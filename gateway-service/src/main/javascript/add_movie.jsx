import React from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import { InputField } from "./components/misc";
import { fetcher } from "./components/api";

const body = () => {
    const onSubmit = e => {
        e.preventDefault();
        let name = e.target.querySelector('input[name="name"]').value

        fetcher('/movies/admin/movies/add', {
            method: 'POST',
            body: JSON.stringify({ name })
        }).then(response => {
            location.href = `/movie/${response.id}`
        });
    };

    return (
        <form className='jumbotron mt-3' onSubmit={onSubmit}>
            <h1>Добавить фильи</h1>
            <InputField type='text' name='name' title='Название' />
            <button className='btn btn-primary'>Создать</button>
        </form>
    );
}

ReactDOM.render(<Application body={body} />, document.getElementById('root'));
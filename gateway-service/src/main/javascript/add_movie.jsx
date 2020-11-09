import React, {useRef, useState} from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import {InputField} from "./components/misc";
import {fetcher} from "./components/api";
import FormCheck from "react-bootstrap/FormCheck";
import {useForm} from "react-hook-form";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCheck} from "@fortawesome/free-solid-svg-icons/faCheck";
import {gql} from "graphql-request/dist";

const addMovieQuery = gql`
    mutation($name: String!, $originalName: String,
        $avatar: Upload, $about: String,
        $country: String!, $genres: [String]!,
        $premiere: LocalDate!, $age: String, $time: String) {
        addMovie(newMovie: {
            name: $name,
            originalName: $originalName,
            about: $about,
            country: $country,
            genres: $genres,
            premiere: $premiere,
            age: $age,
            time: $time
        }, avatar: $avatar) { id }
    }
`;

const AddMovie = () => {
    const [fileUploaded, setFileUploaded] = useState(null);
    const fileInput = useRef(null);
    const { register, handleSubmit, errors } = useForm();

    const onSubmit = data => {
        const formData = new FormData()
        formData.append('file', fileInput.current.files[0]);
        formData.append('map', `{"file": ["variables.avatar"]}`);
        formData.append('operations', JSON.stringify({
            query: addMovieQuery,
            variables: { ...data }
        }));

        fetcher('/movies/graphql', {
            method: 'POST',
            body: formData,
            addContentTypeHeader: false
        }).then(({ data }) => {
            if (data && data.addMovie) {
                location.href = `/movie/${data.addMovie.id}`;
            } else {
                toast.error("Ошибка создания фильма");
            }
        }).catch(e => e.useDefaultErrorParser());
    };

    return (
        <Application>
            <form>
                <input type="file" name='file' id='addAvatarFileInput'
                       ref={fileInput}
                       onChange={() => setFileUploaded(false)} hidden/>
            </form>

            <form className='jumbotron mt-3' onSubmit={handleSubmit(onSubmit)}>
                <h1>Добавить фильм</h1>
                <InputField
                    type='text' name='name' title='Название'
                    ref={register({
                        required: 'Пожалуйста, введите название',
                        maxLength: { value: 40, message: 'Название должно содержать до 40 символов' }
                    })}
                    error={errors.name && errors.name.message} />

                <div>
                    <label htmlFor="addAvatarFileInput" className='btn btn-primary'>Добавить изображение</label>
                    {fileUploaded !== null && <FontAwesomeIcon icon={faCheck} className='ml-1 text-success' />}
                </div>

                <InputField
                    type='text' name='originalName' title='Оригинальное название'
                    ref={register({
                        required: 'Пожалуйста, введите оригинальное название',
                        maxLength: { value: 40, message: 'Оригинальное название должно содержать до 40 символов' }
                    })}
                    error={errors.originalName && errors.originalName.message} />
                <InputField
                    type='text' name='about' title='Описание'
                    ref={register({
                        maxLength: { value: 300, message: 'Описание должно содержать до 300 символов' }
                    })}
                    error={errors.about && errors.about.message} />
                <InputField
                    type='text' name='country' title='Страна'
                    ref={register({
                        required: 'Пожалуйста, укажите страну',
                        minLength: { value: 2, message: 'Страна должна содержать 2 символа' },
                        maxLength: { value: 2, message: 'Страна должна содержать 2 символа' }
                    })}
                    error={errors.country && errors.country.message} />
                <InputField type='date' name='premiere' title='Премьера' ref={register} />
                <InputField
                    type='text' name='age' title='Возрастное ограничение'
                    ref={register({
                        required: 'Пожалуйста, введите возрастное ограничение',
                        maxLength: { value: 10, message: 'Возрастное ограничение должно содержать до 10 символов' }
                    })}
                    error={errors.age && errors.age.message} />
                <InputField
                    type='text' name='time' title='Длительность'
                    ref={register({
                        required: 'Пожалуйста, введите длительность фильма',
                        maxLength: { value: 10, message: 'Длительность фильма должно содержать до 10 символов' }
                    })}
                    error={errors.time && errors.time.message} />

                <div className="mb-3">
                    <label className="form-label">Жанры</label>

                    <FormCheck
                        custom type='checkbox' id='actionGenreCheckbox'
                        label='Action' value='ACTION'
                        name='genres' ref={register}
                    />

                    <FormCheck
                        custom type='checkbox' id='adventureGenreCheckbox'
                        label='ADVENTURE' value='ADVENTURE'
                        name='genres' ref={register}
                    />

                    <FormCheck
                        custom type='checkbox' id='comedyGenreCheckbox'
                        label='COMEDY' value='COMEDY'
                        name='genres' ref={register}
                    />

                    <FormCheck
                        custom type='checkbox' id='crimeGenreCheckbox'
                        label='CRIME' value='CRIME'
                        name='genres' ref={register}
                    />
                </div>

                <button className='btn btn-primary'>Создать</button>
            </form>
        </Application>
    );
}

ReactDOM.render(<AddMovie />, document.getElementById('root'));
import React, {useState} from 'react';
import {UserRole} from './store/user';
import {SearchMovieItem} from "./SearchMovieForm";
import {Button, Spinner} from 'react-bootstrap';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faEye} from "@fortawesome/free-solid-svg-icons/faEye";

declare global {
    interface Window {
        isServer: boolean
        serverProperties: Record<string, string>
        renderServer: () => string
    }
}

export const getMetaProperty = (name: string): string => {
    if (window.isServer) {
        return window.serverProperties[name];
    }

    let el = document.querySelector<HTMLMetaElement>(`meta[name="${name}"]`);
    return el ? el.content : null;
}
export const updateMetaProperty = (name: string, value: string) =>
    document.querySelector<HTMLMetaElement>(`meta[name="${name}"]`).content = value;

export function getUserRoles(): UserRole[] {
    let rolesStr = getMetaProperty('user:roles');
    if (!rolesStr) {
        return [];
    }

    // @ts-ignore
    return rolesStr.substring(1, rolesStr.length - 1).split(', ');
}

interface InputFiledProps {
    id?: string;
    title: string;
    name: string;
    type: string;
    value?: string;
    placeholder?: string;
    error?: string;
    children?: React.ReactNode
}

export const InputField =
    React.forwardRef<HTMLInputElement, InputFiledProps>((props, ref) => (
    <div className="mb-3">
        <label htmlFor={props.id} className="form-label">{props.title}</label>
        <div className='input-group'>
            <input type={props.type} name={props.name} id={props.id} placeholder={props.placeholder}
                   className={'form-control' + ((props.error && props.error !== '') ? ' is-invalid' : '')}
                   ref={ref} defaultValue={props.value} />
            {props.children}
            {props.error && props.error !== '' && (<div className='invalid-feedback'>{props.error}</div>)}
        </div>
    </div>
));

export const LoadingInput = (props: { loading: boolean, children: React.ReactNode }) => (
    <div className='loading-input-container'>
        {props.children}
        {props.loading && (
            <Spinner animation="border" role="status">
                <span className="sr-only">Loading...</span>
            </Spinner>
        )}
    </div>
);

export const getErrorsCount = (errors: {}): number => {
    let errorsCount = 0;
    for (let error in errors) {
        if (errors.hasOwnProperty(error)) {
            ++errorsCount;
        }
    }
    return errorsCount;
};

export const hasRole = (role: UserRole): boolean => {
    let roles = getUserRoles();
    return roles.includes(role);
}

interface PasswordFieldProps {
    title?: string
    name?: string
    error?: string
    placeholder?: string
}
export const PasswordField = React.forwardRef<HTMLInputElement, PasswordFieldProps>((props, ref) => {
    const [showPass, setShowPass] = useState(false);

    return (
        <InputField
            title={props.title || 'Пароль'}
            name={props.name || 'password'}
            placeholder={props.placeholder}
            type={showPass ? 'text' : 'password'}
            ref={ref}
            error={props.error}>

            <div className='input-group-append'>
                <Button onMouseDown={() => setShowPass(true)}
                        onMouseUp={() => setShowPass(false)}
                        className='box-shadowing-none' variant='primary'>
                    <FontAwesomeIcon icon={faEye} />
                </Button>
            </div>
        </InputField>
    );
});

export const mapGenreEnumToString = (genre: string): string => ({
    ABSURDIST: 'Абсурдист',
    ACTION: 'Экшн',
    ADVENTURE: 'Приключение',
    COMEDY: 'Комедия',
    CRIME: 'Криминал',
    DRAMA: 'Драма',
    FANTASY: 'Фентези',
    HISTORICAL: 'Исторический',
    HISTORICAL_FICTION: 'Историческая фантастика',
    HORROR: 'Ужастик',
    MAGICAL_REALISM: 'Магический реализм',
    MYSTERY: 'Детективный роман',
    PARANOID_FICTION: 'Параноидальная беллетристика',
    PHILOSOPHICAL: 'Философский',
    POLITICAL: 'Политический',
    ROMANCE: 'Романтика',
    SAGA: 'Сага',
    SATIRE: 'Сатира',
    SCIENCE_FICTION: 'Научная фантастика',
    SOCIAL: 'Социальный',
    SPECULATIVE: 'Спекулятивный',
    THRILLER: 'Триллер',
    URBAN: 'Городской',
    WESTERN: 'Западный'
} as { [key: string]: string; })[genre];

export const renderGenres = (movie: SearchMovieItem) => movie.genres
    .slice(0, 3)
    .map(genre => mapGenreEnumToString(genre))
    .join(", ");
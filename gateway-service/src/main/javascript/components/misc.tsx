import React from 'react';
import { UserRole } from './store/user';

export const getMetaProperty = (name: string): string => {
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
    id: string;
    title: string;
    name: string;
    type: string;
    value: string;
    placeholder: string;
    error: string;
}

export const InputField =
    React.forwardRef<HTMLInputElement, InputFiledProps>((props, ref) => (
    <div className="mb-3">
        <label htmlFor={props.id} className="form-label">{props.title}</label>
        <input type={props.type} name={props.name} id={props.id} placeholder={props.placeholder}
               className={'form-control' + ((props.error && props.error !== '') ? ' is-invalid' : '')}
               ref={ref} defaultValue={props.value} />
        {props.error && props.error !== '' && (<div className='invalid-feedback'>{props.error}</div>)}
    </div>
));

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
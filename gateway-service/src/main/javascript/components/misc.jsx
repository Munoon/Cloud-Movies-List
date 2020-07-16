import React from 'react';

export const getMetaProperty = name => document.querySelector(`meta[name="${name}"]`).content;

export const Spinner = () => (
    <div className="spinner-border" role="status">
        <span className="sr-only">Loading...</span>
    </div>
);

export const InputField = React.forwardRef((props, ref) => (
    <div className="mb-3">
        <label htmlFor={props.id} className="form-label">{props.title}</label>
        <input type={props.type} name={props.name} id={props.id} placeholder={props.placeholder}
               className={'form-control' + ((props.error && props.error !== '') ? ' is-invalid' : '')}
               ref={ref} />
        {props.error && props.error !== '' && (<div className='invalid-feedback'>{props.error}</div>)}
    </div>
));

export function postForm(path, params = {}) {
    const form = document.createElement('form');
    form.method = 'post';
    form.action = path;

    params[getMetaProperty('_csrf_parameter')] = getMetaProperty('_csrf');
    for (const key in params) {
        if (params.hasOwnProperty(key)) {
            const hiddenField = document.createElement('input');
            hiddenField.type = 'hidden';
            hiddenField.name = key;
            hiddenField.value = params[key];

            form.appendChild(hiddenField);
        }
    }

    document.body.appendChild(form);
    form.submit();
}
import React from 'react';

export const getMetaProperty = name => document.querySelector(`meta[name="${name}"]`).content;

export const InputField = React.forwardRef((props, ref) => (
    <div className="mb-3">
        <label htmlFor={props.id} className="form-label">{props.title}</label>
        <input type={props.type} name={props.name} id={props.id} placeholder={props.placeholder}
               className={'form-control' + ((props.error && props.error !== '') ? ' is-invalid' : '')}
               ref={ref} />
        {props.error && props.error !== '' && (<div className='invalid-feedback'>{props.error}</div>)}
    </div>
));

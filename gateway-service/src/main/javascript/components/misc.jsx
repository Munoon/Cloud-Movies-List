import React from 'react';

export const getMetaProperty = name => document.querySelector(`meta[property="${name}"]`).content;

export const Spinner = () => (
    <div className="spinner-border" role="status">
        <span className="sr-only">Loading...</span>
    </div>
);
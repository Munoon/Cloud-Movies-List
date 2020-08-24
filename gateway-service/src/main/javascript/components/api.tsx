import useSWR from "swr";
import { getMetaProperty } from "./misc";
import { toast } from "react-toastify";
import React from "react";
import { User } from "./store/user";

interface CustomRequestInit extends RequestInit {
    headers?: Record<string, string>;
    params?: Record<string, string>;
    addContentTypeHeader?: boolean;
}

enum ErrorType {
    REQUEST_EXCEPTION,
    VALIDATION_ERROR,
    DATA_ERROR,
    NOT_UNIQUE_ERROR,
    APPLICATION_EXCEPTION,
    ACCESS_DENIED,
    NOT_FOUND
}

interface ErrorInfo {
    url: string;
    errorType: ErrorType;
    details: string[];
    fields?: Record<string, string[]>;
}

export const fetcher = (input: RequestInfo, init: CustomRequestInit = { headers: {}, addContentTypeHeader: true }): Promise<Response> => {
    if (init.headers === undefined) {
        init.headers = {};
    }

    init.headers[getMetaProperty('_csrf_header')] = getMetaProperty('_csrf');

    if (init.headers['Content-Type'] === undefined && init.addContentTypeHeader) {
        init.headers['Content-Type'] = 'application/json';
    } else if (!init.addContentTypeHeader) {
        delete init.headers['Content-Type'];
    }
    init.cache = 'no-cache';
    if (init.params !== undefined) {
        const params = init.params;
        input += '?' + Object.keys(params)
            .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(params[k]))
            .join('&');
    }

    let data: Response;
    return fetch(input, init)
        .then(res => {
            data = res
            return res.text();
        })
        .then(text => {
            let json = parseFromJSON(text);
            if (!data.ok) {
                throw {
                    name: 'RequestException',
                    response: json,
                    data,
                    useDefaultErrorParser: () => parseError(json, data)
                };
            }
            return json;
        });
}

export const getFetcher = (settings: CustomRequestInit) =>
    (input: RequestInfo, init: CustomRequestInit) => fetcher(input, { ...init, ...settings })

function parseError(error: ErrorInfo, data: Response) {
    let messages = [];
    if (error.details) {
        messages.push(error.details.join(', '))
    }
    if (error.fields) {
        for (let field in error.fields) {
            messages.push(`${field}: ${error.fields[field].join(', ')}`);
        }
    }
    toast.error(getMultipleLinesToastElement(messages));
}

function getMultipleLinesToastElement(lines: string[]) {
    let linesInOneStr = lines.join('<br/>');
    if (lines.length > 1) {
        linesInOneStr = linesInOneStr.substr(5);
    }
    return <div dangerouslySetInnerHTML={{ __html: linesInOneStr }}/>
}

function parseFromJSON(text: string) {
    try {
        return JSON.parse(text);
    } catch (e) {
        return '';
    }
}

export function getProfile(): { user: User, isLoading: boolean, isError: boolean } {
    const { data, error } = useSWR('/users/profile', fetcher);

    return {
        user: data as unknown as User,
        isLoading: !error && !data,
        isError: error
    }
}
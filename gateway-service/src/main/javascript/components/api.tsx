import useSWR from "swr";
import {getMetaProperty} from "./misc";
import {toast} from "react-toastify";
import React from "react";
import {User} from "./store/user";
import {GraphQLClient} from "graphql-request";

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

export const fetcher = (input: RequestInfo, init: CustomRequestInit = { headers: {}, addContentTypeHeader: true }): Promise<any> => {
    if (init.headers === undefined) {
        init.headers = {};
    }

    if (!init.addContentTypeHeader) {
        init.addContentTypeHeader = true;
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
                    useDefaultErrorParser: () => parseError(json)
                };
            }
            return json;
        });
}

export const getFetcher = (settings: CustomRequestInit) =>
    (input: RequestInfo, init: CustomRequestInit) => fetcher(input, { ...init, ...settings })

export const movieGraphQLClient = new GraphQLClient('/movies/graphql', {
    headers: {
        'Content-Type': 'application/json',
        [getMetaProperty('_csrf_header')]: getMetaProperty('_csrf')
    }
});

function parseError(error: ErrorInfo, prefix: string = "") {
    let messages = prefix === '' ? [] : [prefix];
    if (error.details && error.details.length !== 0) {
        messages.push(error.details.join(', '))
    }
    if (error.fields) {
        for (let field in error.fields) {
            messages.push(`${field}: ${error.fields[field].join(', ')}`);
        }
    }
    toast.error(getMultipleLinesToastElement(messages));
}

type GraphQLErrorParserType = { (errorInfo: ErrorInfo): void } | string
export function parseGraphQLError(e: any, errorParser: Record<string, GraphQLErrorParserType> = {}) {
    if (e.response.status === 200) {
        e.response.errors.forEach((error: any) => {
            if (error.extensions.errorInfo) {
                const errorInfo = error.extensions.errorInfo as ErrorInfo;
                const parser = errorParser[errorInfo.url]
                if (parser) {
                    if (typeof parser === "string") {
                        parseError(errorInfo, parser)
                    } else {
                        parser(errorInfo)
                    }
                } else {
                    parseError(errorInfo)
                }
            } else {
                toast.error("Ошибка: " + error.message);
            }
        });
    } else {
        const errorInfo = JSON.parse(e.response.error)
        parseError(errorInfo)
    }
}

function getMultipleLinesToastElement(lines: string[]) {
    let linesInOneStr = lines.join('<br/>');
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
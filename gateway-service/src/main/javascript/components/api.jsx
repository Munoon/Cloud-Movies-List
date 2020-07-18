import useSWR from "swr";
import { getMetaProperty } from "./misc";
import { toast } from "react-toastify";

export const fetcher = (...args) => {
    if (args[1] === undefined) {
        args[1] = {
            headers: {}
        };
    } else if (args[1].headers === undefined) {
        args[1].headers = {};
    }
    args[1].headers[getMetaProperty('_csrf_header')] = getMetaProperty('_csrf');
    args[1].headers['Content-Type'] = 'application/json';
    args[1].cache = 'no-cache';

    let data;
    return fetch(...args)
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

function parseError(error, data) {
    toast.error(`Ошибка: ${error.error}`); // TODO add better error parsing
}

function parseFromJSON(text) {
    try {
        return JSON.parse(text);
    } catch (e) {
        return '';
    }
}

export function getProfile() {
    const { data, error } = useSWR('/users/profile', fetcher)

    return {
        user: data,
        isLoading: !error && !data,
        isError: error
    }
}
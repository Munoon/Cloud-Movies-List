import useSWR from "swr";
import { getMetaProperty } from "./misc";

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
    return new Promise(resolve => {
        let data;
        fetch(...args)
            .then(res => {
                data = res;
                return res.text();
            })
            .then(text => {
                let response;
                try {
                    response = JSON.parse(text);
                } catch (e) {
                    response = '';
                }
                resolve(response, data);
            })
    });
}

export function getProfile() {
    const { data, error } = useSWR('/user-resource-service/profile', fetcher)

    return {
        user: data,
        isLoading: !error && !data,
        isError: error
    }
}
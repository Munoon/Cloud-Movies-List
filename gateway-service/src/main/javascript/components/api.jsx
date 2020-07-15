import useSWR from "swr";

export const fetcher = (...args) => fetch(...args).then(res => res.json());

export function getProfile() {
    const { data, error } = useSWR('/user-resource-service/profile', fetcher)

    return {
        user: data,
        isLoading: !error && !data,
        isError: error
    }
}
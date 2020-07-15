import useSWR from "swr";

const fetcher = (...args) => fetch(...args).then(res => res.json());

export function getProfile() {
    const { data, error } = useSWR('/user-resource-service/profile', fetcher)

    return {
        user: data,
        isLoading: !error && !data,
        isError: error
    }
}
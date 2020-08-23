export interface Movie {
    id: string;
    name: string;
    hasAvatar: boolean;
}

export interface MoviesState {
    movies: Record<string, Movie>;
    latestMovies: {
        totalPages: number,
        movies: string[],
        currentPage: number,
        loading: boolean
    }
}

const defaultValue: MoviesState = {
    movies: {},
    latestMovies: {
        totalPages: 0,
        movies: [],
        currentPage: 0,
        loading: true
    }
}

const MoviesAction = {
    ADD_LATEST_MOVIES: 'add_latest_movies',
    LATEST_MOVIES_LOADING: 'latest_movies_loading'
}

interface AddLatestMovies {
    type: typeof MoviesAction.ADD_LATEST_MOVIES;
    totalPages: number;
    movies: Movie[];
    currentPage: number;
    loading: boolean;
}

interface LatestMoviesLoading {
    type: typeof MoviesAction.LATEST_MOVIES_LOADING
}

type MovieAction = AddLatestMovies | LatestMoviesLoading;

export default function (state = defaultValue, action: MovieAction): MoviesState {
    switch (action.type) {
        case MoviesAction.ADD_LATEST_MOVIES:
            let almAction = action as AddLatestMovies
            return {
                ...state,
                movies: {
                    ...state.movies,
                    ...almAction.movies.reduce((acc, item) => {
                        acc[item.id] = item
                        return acc;
                    }, {} as Record<string, Movie>)
                },
                latestMovies: {
                    totalPages: almAction.totalPages,
                    currentPage: almAction.currentPage,
                    movies: [...state.latestMovies.movies, ...almAction.movies.map(movie => movie.id)],
                    loading: almAction.loading
                }
            }
        case MoviesAction.LATEST_MOVIES_LOADING:
            return {
                ...state,
                latestMovies: {
                    ...state.latestMovies,
                    loading: true
                }
            }
        default:
            return state;
    }
}

export const latestMoviesLoading = () => ({ type: MoviesAction.LATEST_MOVIES_LOADING })

export const addLatestMovie = (latestMovies: { totalPages: number, movies: Movie[], currentPage: number }): AddLatestMovies => ({
    type: MoviesAction.ADD_LATEST_MOVIES,
    totalPages: latestMovies.totalPages,
    movies: latestMovies.movies,
    currentPage: latestMovies.currentPage,
    loading: false
})
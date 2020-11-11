import React, {useEffect, useState} from 'react';
import ReactDOM from 'react-dom';
import Application from "./components/Application";
import {SearchMovieItem} from "./components/SearchMovieForm";
import {connect, DefaultRootState} from "react-redux";
import {movieGraphQLClient, parseGraphQLError} from "./components/api";
import {Form, InputGroup, Button} from 'react-bootstrap';
import {MovieImage} from "./components/MoviesComponents";
import {Highlighter} from "react-bootstrap-typeahead";
import {LoadingInput, renderGenres} from "./components/misc";
import {gql} from "graphql-request/dist";

declare var query: string[];

const SearchStoreActionType = {
    ADD: 'SEARCH_ITEMS_ADD',
    SET: 'SEARCH_ITEMS_SET'
};

interface SearchStoreAction {
    type: string;
    data?: SearchStore;
}

interface StoreState extends DefaultRootState {
    searchStore: SearchStore
}

interface SearchStore {
    data: SearchData;
    query: string;
    currentPage: number;
}

interface SearchData {
    totalPages: number,
    totalElements: number,
    movies: SearchMovieItem[]
}

const addItems = (data: SearchStore): SearchStoreAction => ({ type: SearchStoreActionType.ADD, data });
const setItems = (data: SearchStore): SearchStoreAction => ({ type: SearchStoreActionType.SET, data });

const storeDefaultData: SearchStore = { data: { movies: [], totalPages: 0, totalElements: 0 }, query: query ? query[0] : '', currentPage: 0 };

function searchStore(state: SearchStore = storeDefaultData, action: SearchStoreAction): SearchStore {
    switch (action.type) {
        case SearchStoreActionType.SET:
            return {
                data: action.data.data,
                currentPage: action.data.currentPage,
                query: action.data.query
            };
        case SearchStoreActionType.ADD:
            return {
                data: {
                    totalPages: action.data.data.totalPages,
                    totalElements: action.data.data.totalElements,
                    movies: [...state.data.movies, ...action.data.data.movies]
                },
                currentPage: action.data.currentPage,
                query: action.data.query
            };
        default:
            return state;
    }
}

const SearchPage = () => (
    <Application additionalStore={{ searchStore }}>
        <div className='mt-3 pl-5 pr-5'>
            <SearchInput />
            <ResultList />
        </div>
    </Application>
);

const requestQuery = gql`
    query ($query: String, $page: Int) {
        findMovies(findQuery: $query, count: 20, page: $page) {
            totalPages, totalElements, movies { id, name, originalName, age, genres, hasAvatar }
        }
    }
`;

const findMovies = (query: string, page: number): Promise<SearchData> => new Promise((resolve) =>
    movieGraphQLClient.request(requestQuery, { query, page })
        .then(data => resolve(data.findMovies))
        .catch(e => parseGraphQLError(e, { findMovies: 'Ошибка поиска' })))

const SearchInput = connect(null, { setItems })((props: { setItems: typeof setItems }) => {
    const [inputQuery, setQuery] = useState(query ? query[0] : '');
    const [loading, setLoading] = useState(false);

    const handleSubmit = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        makeRequest();
    };

    const makeRequest = () => {
        setLoading(true);
        findMovies(inputQuery, 0)
            .then(data => props.setItems({ data, query: inputQuery, currentPage: 0 }))
            .then(() => setLoading(false));
    };

    useEffect(() => {
        if (inputQuery && inputQuery.length >= 1) {
            makeRequest();
        }
    }, []);

    const buttonDisabled = loading || inputQuery.length < 1;

    return (
        <div className='input-group'>
            <LoadingInput loading={loading}>
                <Form.Control title='' name='query'
                              type='text' placeholder='Название фильма'
                              value={inputQuery}
                              onChange={e => setQuery(e.target.value)} />
            </LoadingInput>
            <InputGroup.Append>
                <Button variant='primary' onClick={handleSubmit} disabled={buttonDisabled}>Поиск</Button>
            </InputGroup.Append>
        </div>
    );
});

interface ResultListProps { searchStore: SearchStore, addItems: typeof addItems }
const ResultList = connect((data: StoreState) => ({ searchStore: data.searchStore }), { addItems })((props: ResultListProps) => {
    const [loading, setLoading] = useState(false);
    const handleLoadMoreClick = (e: React.MouseEvent<HTMLElement>) => {
        e.preventDefault();
        const query = props.searchStore.query;
        const page = props.searchStore.currentPage + 1;
        setLoading(true);
        findMovies(query, page)
            .then(data => {
                props.addItems({ data, query, currentPage: page })
                setLoading(false);
            });
    };
    return (
        <div className='p-4 pt-0'>
            {props.searchStore.data.movies.map(movie => (
                <MovieItem movie={movie} key={movie.id} search={props.searchStore.query} />
            ))}
            {props.searchStore.data.movies.length === 0 && (
                <div className='text-center'>
                    <span>Мы не нашли ни одного фильма</span>
                </div>
            )}
            {props.searchStore.currentPage + 1 < props.searchStore.data.totalPages && (
                <div className='text-center'>
                    <a href='#' className={loading ? 'btn btn-link disabled' : ''} onClick={handleLoadMoreClick}>Загрузить ещё</a>
                </div>
            )}
        </div>
    );
});

const MovieItem = ({ movie, search }: { movie: SearchMovieItem, search: string }) => (
    <a className='search-movie-item-lg row mb-2 text-decoration-none text-decoration-underline-parent' href={`/movie/${movie.id}`}>
        <MovieImage movie={movie} />
        <div className='movie-info ml-2'>
            <h3 className='custom-highlighter text-decoration-underline'>
                <Highlighter search={search}>{movie.name}</Highlighter>
            </h3>
            <span className='text-muted custom-highlighter'>
                <Highlighter search={search}>{movie.originalName}</Highlighter>
            </span>
            <br/>
            <span className='text-muted'>{movie.age}, {renderGenres(movie)}</span>
        </div>
    </a>
);

document.addEventListener('DOMContentLoaded', () => {
    ReactDOM.render(<SearchPage />, document.getElementById('root'));
});

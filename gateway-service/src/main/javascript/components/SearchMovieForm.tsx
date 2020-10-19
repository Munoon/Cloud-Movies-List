import React, { useRef, useState } from "react";
import { fetcher } from "./api";
import { toast } from "react-toastify";
import { AsyncTypeahead, Highlighter } from "react-bootstrap-typeahead";
import { Form, InputGroup } from "react-bootstrap";
import { MovieImage } from "./MoviesComponents";
import Button from "react-bootstrap/Button";
import { renderGenres } from "./misc";

const SearchMovieForm = () => {
    const [loading, setLoading] = useState(false);
    const [options, setOptions] = useState([] as SearchMovieItem[]);
    const inputRef = useRef(null);

    const handleSearch = (query: string) => {
        setLoading(true);

        fetcher('/movies/graphql', {
            method: 'POST',
            body: JSON.stringify({
                query: searchMovieGraphQLQuery,
                variables: { query, page: 0, count: 5 }
            })
        })
            .then(response => {
                if (response.data && response.data.findMovies) {
                    setOptions(response.data.findMovies.movies);
                } else {
                    toast.error('Ошибка поиска фильмов');
                }
                setLoading(false);
            });
    };

    const searchMovie = (e: React.MouseEvent<HTMLElement>) => {
        e.preventDefault();
        if (inputRef.current && inputRef.current.inputNode) {
            let query = inputRef.current.inputNode.value;
            location.href = '/search?query=' + encodeURIComponent(query);
        }
    };

    const handleChange = (data: Array<SearchMovieItem>) => location.href = '/movie/' + data[0].id;

    return (
        <form className="mx-1 my-auto d-inline w-40">
            <div className="input-group">
                <div className='flex-1-1-auto'>
                    <AsyncTypeahead
                        id="search-movie-form-input"
                        isLoading={loading}
                        labelKey="name"
                        minLength={3}
                        onSearch={handleSearch}
                        options={options}
                        filterBy={() => true}
                        onChange={handleChange}
                        // @ts-ignore
                        renderInput={({ inputRef, referenceElementRef, ...inputProps }) => (
                            <Form.Control
                                {...inputProps}
                                ref={(ref: HTMLInputElement) => {
                                    inputRef(ref);
                                    referenceElementRef(ref);
                                }}
                                placeholder='Название фильма'
                                className={inputProps.className}
                            />
                        )}
                        renderMenuItemChildren={(movie, props) => (
                            <MovieItem movie={movie} search={props.text} />
                        )}
                        ref={inputRef}
                    />
                </div>
                <InputGroup.Append>
                    <Button variant='outline-success' onClick={searchMovie}>Найти</Button>
                </InputGroup.Append>
            </div>
        </form>
    );
};

const searchMovieGraphQLQuery = `
    query ($query: String, $count: Int, $page: Int) {
        findMovies(findQuery: $query, count: $count, page: $page) {
            totalPages, totalElements, movies { id, name, originalName, age, genres, hasAvatar }
        }
    }
`;

export interface SearchMovieItem {
    id: string;
    name: string;
    originalName: string;
    age: string;
    genres: string[];
    hasAvatar: boolean;
}

const MovieItem = ({ movie, search = '' }: ({ movie: SearchMovieItem, search?: string })) => (
    <div className='search-movie-item row'>
        <MovieImage movie={movie} />
        <div className='movie-info ml-2'>
            <h6 className='custom-highlighter'>
                <Highlighter search={search}>{movie.name}</Highlighter>
            </h6>
            <span className='text-muted custom-highlighter'>
                <Highlighter search={search}>{movie.originalName}</Highlighter>
            </span>
            <br/>
            <span className='text-muted'>{movie.age}, {renderGenres(movie)}</span>
        </div>
    </div>
);

export default SearchMovieForm;
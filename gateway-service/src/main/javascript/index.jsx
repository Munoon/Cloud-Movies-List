import React, { useEffect, useState } from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import { connect } from "react-redux";
import moviesStore, { addLatestMovie, latestMoviesLoading } from './components/store/movies'
import {movieGraphQLClient, parseGraphQLError} from "./components/api";
import { faChevronRight } from "@fortawesome/free-solid-svg-icons/faChevronRight";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faChevronLeft } from "@fortawesome/free-solid-svg-icons/faChevronLeft";
import { MovieImage } from "./components/MoviesComponents";
import { gql } from 'graphql-request';

const IndexPage = () => (
    <Application additionalStore={{ moviesStore }}>
        <MainPage />
    </Application>
);

const latestMoviesQuery = gql`
    query ($page: Int!) {
        latestMovies(count: 10, page: $page) {
            totalPages,
            movies {
                id, name, hasAvatar
            }
        }
    }
`;

const MainPage = connect(null, { addLatestMovie })(props => {
    useEffect(() => {
        movieGraphQLClient.request(latestMoviesQuery, { page: 0 })
            .then(data => props.addLatestMovie({ ...data.latestMovies, currentPage: 0 }))
            .catch(e => parseGraphQLError(e, { latestMovies: 'Ошибка загрузки новых фильмов' }));
    });

    return (
        <div className='jumbotron mt-3'>
            <LatestMoviesList />
        </div>
    );
});

const TitledMoviesList = props => {
    const [list, setList] = useState(null)
    const [canScroll, setCanScroll] = useState({
        canScrollNext: false,
        canScrollPrev: false
    });

    useEffect(() => {
        updateScrollSettings();
        if (list !== null && list.scrollWidth === list.clientWidth) {
            props.onScrollBottom();
        }
    }, [props.children]);

    const updateScrollSettings = () => setCanScroll({
        canScrollNext: list ? list.scrollWidth - list.scrollLeft - list.clientWidth !== 0 : false,
        canScrollPrev: list ? list.scrollLeft > 0 : false
    });

    const onScroll = () => {
        if (list.scrollWidth - list.scrollLeft - list.clientWidth <= 10 && props.onScrollBottom) {
            props.onScrollBottom();
        }
        updateScrollSettings();
    }

    const scroll = left => list.scroll({
        top: 0,
        left,
        behavior: 'smooth'
    });

    const scrollNext = () => scroll(list.scrollLeft + 300);
    const scrollPrev = () => scroll(list.scrollLeft - 300);

    const prevButtonClasses = 'movies-list-button col' + (!canScroll.canScrollPrev ? ' disabled' : '')
    const nextButtonClasses = 'movies-list-button col' + (!canScroll.canScrollNext ? ' disabled' : '')

    return (
        <div>
            <h3>{props.title}</h3>
            <div className='movies-list-with-buttons row'>
                <div className={prevButtonClasses} onClick={() => scrollPrev()}>
                    <FontAwesomeIcon icon={faChevronLeft} />
                </div>
                <div className='movies-list col' onScroll={() => onScroll()} ref={ref => setList(ref)}>
                    {props.children}
                </div>
                <div className={nextButtonClasses} onClick={() => scrollNext()}>
                    <FontAwesomeIcon icon={faChevronRight} />
                </div>
            </div>
        </div>
    );
}

const LatestMoviesList = connect(({ moviesStore: { latestMovies, movies } }) => ({ latestMovies, movies }), { addLatestMovie, latestMoviesLoading })(props => {
    const onScrollBottom = () => {
        let latestMovies = props.latestMovies
        if (latestMovies.totalPages > latestMovies.currentPage + 1 && !props.latestMovies.loading) {
            const newPage = latestMovies.currentPage + 1;
            props.latestMoviesLoading();

            movieGraphQLClient.request(latestMoviesQuery, { page: newPage })
                .then(data => props.addLatestMovie({ ...data.latestMovies, currentPage: newPage }))
                .catch(e => parseGraphQLError(e, { latestMovies: 'Ошибка загрузки новых фильмов' }));
        }
    };

    return (
        <TitledMoviesList title='Новые фильмы' onScrollBottom={onScrollBottom}>
            {props.latestMovies.movies.length === 0 && <span>Нет доступных фильмов</span>}
            {props.latestMovies.movies.map(id => (
                <MovieAvatar key={id} movie={props.movies[id]}/>
            ))}
        </TitledMoviesList>
    );
});

const MovieAvatar = ({ movie}) => (
    <a href={`/movie/${movie.id}`} className='movie-avatar'>
        <MovieImage movie={movie} />
        <h4>{movie.name}</h4>
    </a>
);

document.addEventListener('DOMContentLoaded', () => {
    ReactDOM.render(<IndexPage />, document.getElementById('root'));
});

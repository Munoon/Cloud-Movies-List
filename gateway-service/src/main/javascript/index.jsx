import React, {useCallback, useEffect, useState} from 'react';
import ReactDOM from 'react-dom';
import Application from './components/Application';
import { connect } from "react-redux";
import moviesStore, { addLatestMovie, latestMoviesLoading } from './components/store/movies'
import { fetcher } from "./components/api";
import {faChevronRight} from "@fortawesome/free-solid-svg-icons/faChevronRight";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronLeft} from "@fortawesome/free-solid-svg-icons/faChevronLeft";

const IndexPage = () => (
    <Application additionalStore={{ moviesStore }}>
        <MainPage />
    </Application>
);

const latestMoviesQuery = `
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
        fetcher('/movies/graphql', {
            method: 'POST',
            body: JSON.stringify({
                query: latestMoviesQuery,
                variables: { page: 0 }
            })
        }).then(response => {
            if (response.data && response.data.latestMovies) {
                props.addLatestMovie({ ...response.data.latestMovies, currentPage: 0 })
            }
        }).catch(e => e.useDefaultErrorParser())
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
            console.log('add')
            const newPage = latestMovies.currentPage + 1;
            props.latestMoviesLoading();
            fetcher('/movies/graphql', {
                method: 'POST',
                body: JSON.stringify({
                    query: latestMoviesQuery,
                    variables: { page: newPage }
                })
            }).then(response => {
                if (response.data && response.data.latestMovies) {
                    props.addLatestMovie({ ...response.data.latestMovies, currentPage: newPage })
                }
            }).catch(e => e.useDefaultErrorParser());
        }
    };

    return (
        <TitledMoviesList title='Новые фильмы' onScrollBottom={onScrollBottom}>
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

const MovieImage = ({ movie }) => (
    <img
        src={movie.hasAvatar ? `/movies/movie/${movie.id}/avatar` : '/static/img/default_movie_avatar.jpg'}
        alt={`Avatar for '${movie.name}' movie`} />
);

ReactDOM.render(<IndexPage />, document.getElementById('root'));
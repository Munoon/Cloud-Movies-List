import React from "react";
import { Movie } from "./store/movies";

export const MovieImage = ({ movie }: { movie: Movie }) => (
    <img
        className='movie-avatar'
        src={movie.hasAvatar ? `/movies/movie/${movie.id}/avatar` : '/static/img/default_movie_avatar.jpg'}
        alt={`Avatar for '${movie.name}' movie`} />
);
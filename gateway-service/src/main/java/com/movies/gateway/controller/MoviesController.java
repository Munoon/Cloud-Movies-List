package com.movies.gateway.controller;

import com.movies.common.movie.MovieTo;
import com.movies.gateway.movie.MoviesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class MoviesController {
    private MoviesService moviesService;

    @GetMapping("/movie/{movieId}")
    public String movie(@PathVariable String movieId, Model model) {
        MovieTo movie = moviesService.getMovieById(movieId);
        model.addAttribute("movie", movie);
        return "movie_page";
    }
}

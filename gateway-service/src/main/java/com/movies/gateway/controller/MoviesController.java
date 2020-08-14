package com.movies.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MoviesController {
    @GetMapping("/movie/{movieId}")
    public String movie(@PathVariable String movieId, Model model) {
        model.addAttribute("movieId", movieId);
        return "movie_page";
    }
}

package com.movies.gateway.movie;

import com.movies.common.movie.SmallMovieTo;
import com.movies.gateway.config.AuthenticateIfCan;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "movies-list-service", configuration = AuthenticateIfCan.class)
public interface MovieClient {
    @RequestMapping(method = RequestMethod.GET, path = "/movie/{id}", consumes = "application/json")
    SmallMovieTo getMovieById(@PathVariable("id") String id);
}
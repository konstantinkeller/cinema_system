package com.csci4050.cinema_system.dao;

import com.csci4050.cinema_system.model.Movie;
import com.csci4050.cinema_system.model.User;
import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    Movie findMovieById(Long movieId);
}

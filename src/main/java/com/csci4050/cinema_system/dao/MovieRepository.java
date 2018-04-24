package com.csci4050.cinema_system.dao;

import com.csci4050.cinema_system.model.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {
    Movie findMovieById(Long movieId);
    List<Movie> findTop5ByOrderByIdDesc();
}

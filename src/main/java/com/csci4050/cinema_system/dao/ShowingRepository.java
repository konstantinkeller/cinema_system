package com.csci4050.cinema_system.dao;

import com.csci4050.cinema_system.model.Movie;
import com.csci4050.cinema_system.model.Showing;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;

public interface ShowingRepository extends CrudRepository<Showing, Long> {
    Showing findShowingById(Long showingId);
    Iterable<Showing> findShowingsByShowdate(Date showingShowdate);
    Iterable<Showing> findShowingByMovie(Movie showingMovie);
}

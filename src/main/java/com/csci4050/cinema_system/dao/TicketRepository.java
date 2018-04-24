package com.csci4050.cinema_system.dao;

import com.csci4050.cinema_system.model.Movie;
import com.csci4050.cinema_system.model.Ticket;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Ticket findTicketById(Long TicketId);
    Iterable<Ticket> findTicketsByShowdate(Date TicketShowdate);
    Iterable<Ticket> findTicketByMovie(Movie TicketMovie);
}

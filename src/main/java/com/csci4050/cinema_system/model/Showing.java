package com.csci4050.cinema_system.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
public class Showing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private Date showdate;

    @Basic
    private Time showtime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;
}

package com.csci4050.cinema_system.model;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}

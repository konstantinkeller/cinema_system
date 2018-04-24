package com.csci4050.cinema_system.controller;

import com.csci4050.cinema_system.dao.MovieRepository;
import com.csci4050.cinema_system.model.Movie;
import com.csci4050.cinema_system.model.Showing;
import com.csci4050.cinema_system.component.MovieListsFormatter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/movies")
public class MoviesWebController {
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    MailSender mailSender;

    private MovieListsFormatter movieListsFormatter = new MovieListsFormatter(4);

    @GetMapping
    public String showMovies(Model model) {
        Iterable<Movie> movieList = movieRepository.findAll();
        List<List<Movie>> movieLists = movieListsFormatter.format(movieList);

        model.addAttribute("movieLists", movieLists);

        return "movies";
    }

    @PostMapping
    public String searchMovies(Model model,
                               @RequestParam(value = "title", required = false) String title,
                               @RequestParam(value = "date", required = false) String date,
                               @RequestParam(value = "category", required = false) String category) {
        Iterable<Movie> allMovies = movieRepository.findAll();
        List<List<Movie>> movieLists;

        if ((title != null && !title.isEmpty() || (date != null && !date.isEmpty()) || (category != null && !category.isEmpty()))) {
            List<Movie> results = new LinkedList<>();
            boolean match;
            for (Movie m : allMovies) {
                match = false;
                if (title != null && !title.isEmpty()) {
                    match = StringUtils.containsIgnoreCase(m.getTitle(), title);
                    if (!match) continue;
                }
                if (date != null && !date.isEmpty()) {
                    match = false;
                    for (Showing s : m.getShowings()) {
                        match = (s.getShowdate().compareTo(Date.valueOf(date)) == 0);
                        if (match) break;
                    }
                    if (!match) continue;
                }
                if (category != null && !category.isEmpty()) {
                    match = StringUtils.containsIgnoreCase(m.getCategory(), category);
                    if (!match) continue;
                }

                if (match) results.add(m);
            }
            movieLists = movieListsFormatter.format(results);
        } else {
            movieLists = movieListsFormatter.format(allMovies);
        }

        model.addAttribute("movieLists", movieLists);
        model.addAttribute("title", title);
        model.addAttribute("date", date);
        model.addAttribute("category", category);

        return "movies";
    }

}

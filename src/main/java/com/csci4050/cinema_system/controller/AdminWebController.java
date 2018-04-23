package com.csci4050.cinema_system.controller;

import com.csci4050.cinema_system.dao.MovieRepository;
import com.csci4050.cinema_system.dao.UserRepository;
import com.csci4050.cinema_system.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminWebController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping
    public String index(Model model) {
        return "admin/index";
    }

    @GetMapping("/movies")
    public String showManageMovies(Model model) {
        Iterable<Movie> movieList = movieRepository.findAll();

        model.addAttribute("movieList", movieList);

        return "admin/movies";
    }

    @PostMapping("/movies")
    public String updateMovies(Model model, @RequestParam(value = "delete_mov", required = false) String delete_mov,
                               @RequestParam(value = "add_mov", required = false) String add_mov,
                               @RequestParam(value = "edit_mov", required = false) String edit_mov,
                               RedirectAttributes redirectAttributes) {
        if (delete_mov != null) {
            movieRepository.deleteById(Long.valueOf(delete_mov));
        } else if (add_mov != null) {
            return "redirect:/admin/movies/new";
        } else if (edit_mov != null) {
            redirectAttributes.addFlashAttribute("mov_id", edit_mov);
            return "redirect:/admin/movies/edit";
        }

        return "redirect:/admin/movies";
    }

    @GetMapping("/movies/new")
    public String addMovie(Model model) {
        model.addAttribute("mov", new Movie());

        return "admin/edit_movie";
    }

    @GetMapping("/movies/edit")
    public String editMovie(Model model, @ModelAttribute("mov_id") String mov_id) {
        Movie mov = movieRepository.findMovieById(Long.valueOf(mov_id));
        model.addAttribute("mov", mov);

        return "admin/edit_movie";
    }

    @PostMapping("/movies/save")
    public String saveMovie(Model model, @ModelAttribute("mov") Movie mov) {
        movieRepository.save(mov);

        return "redirect:/admin/movies";
    }

}

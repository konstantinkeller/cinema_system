package com.csci4050.cinema_system.controller;

import com.csci4050.cinema_system.component.MovieListsFormatter;
import com.csci4050.cinema_system.dao.MovieRepository;
import com.csci4050.cinema_system.dao.ShowingRepository;
import com.csci4050.cinema_system.dao.TicketRepository;
import com.csci4050.cinema_system.dao.UserRepository;
import com.csci4050.cinema_system.model.Booking;
import com.csci4050.cinema_system.model.Movie;
import com.csci4050.cinema_system.model.Showing;
import com.csci4050.cinema_system.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;
import java.util.List;

@Controller
@Scope("session")
@RequestMapping("/book")
public class BookingWebController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    MailSender mailSender;

    private MovieListsFormatter movieListsFormatter = new MovieListsFormatter(4);

    @GetMapping
    public String listMovies(Model model) {
        List<List<Movie>> movieLists = movieListsFormatter.format(movieRepository.findAll());
        model.addAttribute("movieLists", movieLists);

        return "booking/select_movie";
    }

    @PostMapping
    public String selectMovie(Model model, @RequestParam("select_movie") String mov_id, HttpSession session) {
        Booking booking = new Booking();
        booking.setMovie(movieRepository.findMovieById(Long.valueOf(mov_id)));
        session.setAttribute("booking", booking);

        return "redirect:/book/showtimes";
    }

    @GetMapping("/showtimes")
    public String listShowings(Model model, @SessionAttribute("booking") Booking booking, HttpSession session) {
        Movie mov = booking.getMovie();
        Iterable<Showing> showtimeList = showingRepository.findShowingByMovie(mov);

        model.addAttribute("mov", mov);
        model.addAttribute("showtimeList", showtimeList);

        return "booking/select_showtime";
    }

    @PostMapping("/showtimes")
    public String selectShowings(Model model, @SessionAttribute("booking") Booking booking, @RequestParam("select_showtime") String s_id,
                                 HttpSession session, HttpServletRequest request, Principal principal) {
        Booking b = booking;

        Ticket t = new Ticket();
        t.setBooking(b);
        t.setMovie(b.getMovie());
        t.setShowing(showingRepository.findShowingById(Long.valueOf(s_id)));
        t.setUser(userRepository.findByEmail(principal.getName()));
        t = ticketRepository.save(t);
        b.addTicket(t);

        session.setAttribute("booking", b);

        return "redirect:/book/seats";
    }

    @GetMapping("/seats")
    public String listSeats(Model model, @SessionAttribute("booking") Booking booking, HttpSession session) {
        return "booking/review_order";
    }

    @PostMapping("/seats")
    public String selectSeats(Model model, @SessionAttribute("booking") Booking booking, HttpSession session) {
        session.setAttribute("booking", booking);

        return "redirect:/book/review";
    }

    @GetMapping("/review")
    public String showReview(Model model, @SessionAttribute("booking") Booking booking, HttpSession session) {
        Collection<Ticket> tickets = booking.getTickets();

        model.addAttribute("tickets", tickets);

        return "booking/review_order";
    }

}

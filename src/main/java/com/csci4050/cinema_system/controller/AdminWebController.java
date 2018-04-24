package com.csci4050.cinema_system.controller;

import com.csci4050.cinema_system.dao.MovieRepository;
import com.csci4050.cinema_system.dao.PromoRepository;
import com.csci4050.cinema_system.dao.ShowingRepository;
import com.csci4050.cinema_system.dao.UserRepository;
import com.csci4050.cinema_system.dto.UserDto;
import com.csci4050.cinema_system.model.Movie;
import com.csci4050.cinema_system.model.Promotion;
import com.csci4050.cinema_system.model.Showing;
import com.csci4050.cinema_system.model.User;
import com.csci4050.cinema_system.service.StorageService;
import com.csci4050.cinema_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
@RequestMapping("/admin")
public class AdminWebController {
    @Autowired
    private UserService userService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ShowingRepository showingRepository;

    @Autowired
    private PromoRepository promoRepository;

    @Autowired
    MailSender mailSender;

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
                               @RequestParam(value = "edit_showings", required = false) String edit_showings,
                               RedirectAttributes redirectAttributes) {
        if (delete_mov != null) {
            movieRepository.deleteById(Long.valueOf(delete_mov));
        } else if (add_mov != null) {
            return "redirect:/admin/movies/new";
        } else if (edit_mov != null) {
            redirectAttributes.addFlashAttribute("mov_id", edit_mov);
            return "redirect:/admin/movies/edit";
        } else if (edit_showings != null) {
            redirectAttributes.addFlashAttribute("mov_id", edit_showings);
            return "redirect:/admin/movies/showings";
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
    public String saveMovie(Model model, @ModelAttribute("mov") Movie mov, @RequestParam(value = "cover_pic", required = false) MultipartFile cover_pic) {
        Movie existingMov = movieRepository.findMovieById(mov.getId());
        if (!cover_pic.isEmpty()) {
            if (existingMov != null) {
                if (existingMov.getImageName() != null) storageService.delete(existingMov.getImageName());
            }
            String uploadedName = storageService.store(cover_pic);
            mov.setImageName(uploadedName);
        } else {
            if (existingMov != null) {
                if (existingMov.getImageName() != null) mov.setImageName(existingMov.getImageName());
            }
        }
        movieRepository.save(mov);

        return "redirect:/admin/movies";
    }

    @GetMapping("/movies/showings")
    public String showShowings(Model model, @ModelAttribute("mov_id") String mov_id, HttpServletRequest request) {
        Movie mov = movieRepository.findMovieById(Long.valueOf(mov_id));
        Collection<Showing> showings = mov.getShowings();

        HttpSession session = request.getSession();
        session.setAttribute("mov", mov);

        model.addAttribute("mov", mov);
        model.addAttribute("showings", showings);

        return "admin/showings";
    }

    @PostMapping("/movies/showings")
    public String updateShowings(Model model,
                                 @SessionAttribute("mov") Movie mov,
                                 @RequestParam(value = "delete_show", required = false) String delete_show,
                                 @RequestParam(value = "add_show", required = false) String add_show,
                                 @RequestParam(value = "edit_show", required = false) String edit_show,
                                 RedirectAttributes redirectAttributes,
                                 SessionStatus status) {

        if (add_show != null) {
            redirectAttributes.addAttribute("mov_id", mov.getId());
            return "redirect:/admin/movies/showings/new";
        } else if (edit_show != null) {
            redirectAttributes.addAttribute("s_id", edit_show);
            return "redirect:/admin/movies/showings/edit";
        } else if (delete_show != null) {
            mov.removeFromShowings(mov.getShowingById(Long.valueOf(delete_show)));
            movieRepository.save(mov);
            status.setComplete();
        }

        redirectAttributes.addFlashAttribute("mov_id", mov.getId());
        return "redirect:/admin/movies/showings";
    }

    @GetMapping("/movies/showings/new")
    public String addShowing(Model model, @ModelAttribute("mov_id") String mov_id) {
        Movie mov = movieRepository.findMovieById(Long.valueOf(mov_id));

        model.addAttribute("mov", mov);
        model.addAttribute("show", new Showing());

        return "admin/edit_showing";
    }

    @GetMapping("/movies/showings/edit")
    public String editShowing(Model model, @ModelAttribute("s_id") String s_id) {
        Showing show = showingRepository.findShowingById(Long.valueOf(s_id));
        Movie mov = show.getMovie();

        model.addAttribute("mov", mov);
        model.addAttribute("show", show);

        return "admin/edit_showing";
    }

    @PostMapping("/movies/showings/save")
    public String saveShowing(Model model, SessionStatus status, @SessionAttribute("mov") Movie mov, @ModelAttribute("show") Showing show,
                              RedirectAttributes redirectAttributes) {
        if (mov.getShowings().contains(show)) {
            show.setMovie(mov);
            showingRepository.save(show);
        } else {
            mov.addToShowings(show);
            movieRepository.save(mov);
        }

        status.setComplete();

        redirectAttributes.addFlashAttribute("mov_id", mov.getId());
        return "redirect:/admin/movies/showings";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        Iterable<User> userList = userRepository.findAll();

        model.addAttribute("userList", userList);

        return "admin/users";
    }

    @PostMapping("/users")
    public String updateUsers(Model model,
                              @RequestParam(value = "delete_user", required = false) String delete_user,
                              @RequestParam(value = "add_user", required = false) String add_user,
                              @RequestParam(value = "edit_user", required = false) String edit_user,
                              RedirectAttributes redirectAttributes,
                              SessionStatus status) {
        if (add_user != null) {
            return "redirect:/admin/users/new";
        } else if (edit_user != null) {
            redirectAttributes.addFlashAttribute("user_id", edit_user);
            return "redirect:/admin/users/edit";
        } else if (delete_user != null) {
            userRepository.deleteById(Long.valueOf(delete_user));
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/users/new")
    public String addUser(Model model) {
        model.addAttribute("userDto", new UserDto());

        return "admin/edit_user";
    }

    @GetMapping("/users/edit")
    public String editUser(Model model, @ModelAttribute("user_id") String user_id) {
        User user = userRepository.findUserById(Long.valueOf(user_id));
        model.addAttribute("userDto", new UserDto(user));

        return "admin/edit_user";
    }

    @PostMapping("/users/save")
    public String saveUser(Model model, @ModelAttribute("userDto") UserDto userDto) {
        if (userDto.getId() != null) {
            userService.update(userDto);
        } else {
            userService.save(userDto);
        }

        return "redirect:/admin/users";
    }

    @GetMapping("/promos")
    public String showPromos(Model model) {
        Iterable<Promotion> promoList = promoRepository.findAll();

        model.addAttribute("promoList", promoList);

        return "admin/promos";
    }

    @PostMapping("/promos")
    public String updatePromos(Model model,
                               @RequestParam(value = "delete_promo", required = false) String delete_promo,
                               @RequestParam(value = "add_promo", required = false) String add_promo,
                               @RequestParam(value = "edit_promo", required = false) String edit_promo,
                               RedirectAttributes redirectAttributes,
                               SessionStatus status) {
        if (add_promo != null) {
            return "redirect:/admin/promos/new";
        } else if (edit_promo != null) {
            redirectAttributes.addFlashAttribute("promo_id", edit_promo);
            return "redirect:/admin/promos/edit";
        } else if (delete_promo != null) {
            promoRepository.deleteById(Long.valueOf(delete_promo));
        }

        return "redirect:/admin/promos";
    }

    @GetMapping("/promos/new")
    public String addPromo(Model model) {
        model.addAttribute("promo", new Promotion());

        return "admin/edit_promo";
    }

    @GetMapping("/promos/edit")
    public String editPromo(Model model, @ModelAttribute("promo_id") String promo_id) {
        Promotion promo = promoRepository.findPromotionById(Long.valueOf(promo_id));
        model.addAttribute("promo", promo);

        return "admin/edit_promo";
    }

    @PostMapping("/promos/save")
    public String savePromo(Model model, @ModelAttribute("promo") Promotion promo) {
        if (promo.getId() == null) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Cinema System New Promotion");
            message.setText("A new promotional offer has been added!\nCode: " + promo.getCode() + "\nDiscount: " + promo.getDiscount() + "%\nExpires: " + promo.getExpires().toString());
            message.setFrom("cinema@sigilhosting.com");
            for (User u : userRepository.findAll()) {
                if (u.getPromos()) {
                    message.setTo(u.getEmail());
                    try {
                        mailSender.send(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        promoRepository.save(promo);

        return "redirect:/admin/promos";
    }
}

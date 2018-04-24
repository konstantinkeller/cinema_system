package com.csci4050.cinema_system.model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity(name = "Movie")
@Table(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageName;

    private String title;
    private String category;
    private String cast;
    private String director;
    private String producer;

    @Column(length = 5000)
    private String synopsis;

    private String mpaaRating;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Review> reviews;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Showing> showings;

    public Movie() {
        reviews = new ArrayList<>();
        showings = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getMpaaRating() {
        return mpaaRating;
    }

    public void setMpaaRating(String mpaaRating) {
        this.mpaaRating = mpaaRating;
    }

    public Collection<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Collection<Review> reviews) {
        this.reviews.clear();

        if (reviews != null) this.reviews.addAll(reviews);
    }

    public Collection<Showing> getShowings() {
        return showings;
    }

    public Showing getShowingById(Long id) {
        for (Showing s : showings) {
            if (id.equals(s.getId())) return s;
        }

        return null;
    }

    public void setShowings(Collection<Showing> showings) {
        this.showings.clear();

        if (showings != null) this.showings.addAll(showings);
    }

    public void addToShowings(Showing showing) {
        showing.setMovie(this);
        this.showings.add(showing);
    }

    public void removeFromShowings(Showing showing) {
        this.showings.remove(showing);
    }
}

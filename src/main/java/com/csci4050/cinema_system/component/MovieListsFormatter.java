package com.csci4050.cinema_system.component;

import com.csci4050.cinema_system.model.Movie;

import java.util.LinkedList;
import java.util.List;

public class MovieListsFormatter {

    private int itemsPerGroup;

    public MovieListsFormatter(int itemsPerGroup) {
        this.itemsPerGroup = itemsPerGroup;
    }

    public List<List<Movie>> format(Iterable<Movie> list) {
        List<List<Movie>> out = new LinkedList<>();
        List<Movie> tmp = new LinkedList<>();

        int size = 0;
        for (Movie m : list) size++;

        int i = 0;
        for (Movie m : list) {
            tmp.add(m);
            if (size == (i + 1) || tmp.size() == this.itemsPerGroup) {
                out.add(tmp);
                tmp = new LinkedList<>();
            }
            i++;
        }

        return out;
    }
}

package com.example.ahmed.movieapp.Adapter;

import com.example.ahmed.movieapp.Models.MovieModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Ahmed on 1/29/2018.
 */

public class MovieSorter {
    public void sortMovieModelsByName(ArrayList<MovieModel> MovieModels){
        Collections.sort(MovieModels, new Comparator<MovieModel>() {
            @Override
            public int compare(MovieModel lhs, MovieModel rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
    }

    public void sortMovieModelsByRating(ArrayList<MovieModel> MovieModels){
        Collections.sort(MovieModels, new Comparator<MovieModel>() {
            @Override
            public int compare(MovieModel lhs, MovieModel rhs) {
                float ratingLhs=Float.parseFloat(lhs.getVoteRange());
                float ratingRhs=Float.parseFloat(rhs.getVoteRange());
                if(ratingLhs<ratingRhs)
                {
                    return 1;
                }
                else if(ratingLhs>ratingRhs)
                {
                    return -1;
                }
                else
                {
                    return 0;
                }
            }
        });
    }
}
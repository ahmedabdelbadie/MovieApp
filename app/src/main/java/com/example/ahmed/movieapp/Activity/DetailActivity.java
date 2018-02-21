package com.example.ahmed.movieapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ahmed.movieapp.Fragment.DetailFragment;
import com.example.ahmed.movieapp.Models.MovieModel;
import com.example.ahmed.movieapp.R;

public class DetailActivity extends AppCompatActivity {
    DetailFragment detailFragment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MovieModel movieModel = getIntent().getParcelableExtra("item");
        detailFragment.showUserSelectedMovie(movieModel);
    }
}

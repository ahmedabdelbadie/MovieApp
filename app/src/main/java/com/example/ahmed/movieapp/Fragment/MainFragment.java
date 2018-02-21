package com.example.ahmed.movieapp.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ahmed.movieapp.Adapter.MovieAdapter;
import com.example.ahmed.movieapp.Adapter.MovieSorter;
import com.example.ahmed.movieapp.Models.MovieModel;
import com.example.ahmed.movieapp.R;
import com.example.ahmed.movieapp.DataBase.RealmMovie;
import com.example.ahmed.movieapp.Singlton.Singlton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainFragment extends Fragment  {
    RecyclerView recyclerView;
    ArrayList <MovieModel> movieList = new ArrayList <>();
    MovieModel data;
    private static final String TAG = "MAINFRAGMENT";
    Realm realm;
    public MovieAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            movieList = savedInstanceState.getParcelableArrayList("movielist");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = v.findViewById(R.id.rv_movie);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        if (!movieList.isEmpty() && savedInstanceState!=null){
            adapter = new MovieAdapter(MainFragment.this, movieList);
            recyclerView.setAdapter(adapter);
        }
        realm = Realm.getDefaultInstance();
        return v;
    }


    public void startShow(String link) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, link, new Response.Listener <String>() {
            @Override
            public void onResponse(String response) {
                try {
                    movieList.clear();
                    JSONObject obj = null;
                    obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String posterPath = jsonObject.getString("poster_path");
                        String adult = jsonObject.getString("adult");
                        String overview = jsonObject.getString("overview");
                        String releaseDate = jsonObject.getString("release_date");
                        String id = jsonObject.getString("id");
                        String originalTitle = jsonObject.getString("original_title");
                        String originalLanguage = jsonObject.getString("original_language");
                        String title = jsonObject.getString("title");
                        String backdrop = jsonObject.getString("backdrop_path");
                        String popularity = jsonObject.getString("popularity");
                        String voteCount = jsonObject.getString("vote_count");
                        String video = jsonObject.getString("video");
                        String voteRange = jsonObject.getString("vote_average");

                        data = new MovieModel(posterPath,
                                adult,
                                overview,
                                releaseDate,
                                id,
                                originalTitle,
                                originalLanguage,
                                title,
                                backdrop,
                                popularity,
                                voteCount,
                                video,
                                voteRange);
                        movieList.add(data);

                    }
                    if (!movieList.isEmpty()) {
                        adapter = new MovieAdapter(MainFragment.this, movieList);
                        recyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        });
        Singlton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList("movielist",movieList);
    }

    public void start() {
        movieList.clear();
        try {
            RealmResults <RealmMovie> results = realm.where(RealmMovie.class).findAll();
            for (RealmMovie realmMovie : results) {
                data = new MovieModel(realmMovie.getPosterPath(),
                        realmMovie.getAdult(),
                        realmMovie.getOverview(),
                        realmMovie.getReleaseDate(),
                        realmMovie.getId(),
                        realmMovie.getOriginalTitle(),
                        realmMovie.getOriginalLang(),
                        realmMovie.getTitle(),
                        realmMovie.getBackdrop(),
                        realmMovie.getPopularity(),
                        realmMovie.getVoteCount(),
                        realmMovie.getVideo(),
                        realmMovie.getVoteRange());
                movieList.add(data);

            }
            if (!movieList.isEmpty()) {
                adapter = new MovieAdapter(MainFragment.this, movieList);
                recyclerView.setAdapter(adapter);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "start: " + e);
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
    }



}

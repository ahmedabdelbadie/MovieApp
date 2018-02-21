package com.example.ahmed.movieapp.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.ahmed.movieapp.Adapter.ReviewAdapter;
import com.example.ahmed.movieapp.Adapter.TrailersAdapter;
import com.example.ahmed.movieapp.DataBase.RealmMovie;
import com.example.ahmed.movieapp.Models.MovieModel;
import com.example.ahmed.movieapp.Models.ReviewModel;
import com.example.ahmed.movieapp.R;
import com.example.ahmed.movieapp.Singlton.Singlton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private TextView title, rate, releaseDate, desc, trailerText, reviewText;
    private ImageView moviePoter, movieBack;
    private ImageView iv;
    Realm realm;
    RecyclerView rvTrailer;
    RecyclerView rvReview;
    ArrayList <String> trailers = new ArrayList <>();
    ArrayList <ReviewModel> reviewModels = new ArrayList <>();
    private static final String TAG = "REVIEW";

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        title = v.findViewById(R.id.tv_title);
        rate = v.findViewById(R.id.tv_rate);
        releaseDate = v.findViewById(R.id.tv_movie_data);
        desc = v.findViewById(R.id.tv_desc);
        moviePoter = v.findViewById(R.id.iv_movie);
        movieBack = v.findViewById(R.id.iv_movie_back);
        iv = v.findViewById(R.id.iv_fav);
        trailerText = v.findViewById(R.id.tv_trailer_text);
        reviewText = v.findViewById(R.id.tv_review_text);


        rvTrailer = v.findViewById(R.id.rv_trailers);
        rvTrailer.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvReview = v.findViewById(R.id.rv_reviews);
        rvReview.setLayoutManager(new LinearLayoutManager(getActivity()));

        return v;
    }

    public void showUserSelectedMovie(MovieModel movieModel) {

        title.setText(movieModel.getTitle());
        releaseDate.setText(movieModel.getReleaseDate());
        rate.setText("Avarage Rating : " + movieModel.getVoteRange() + " /10");
        desc.setText(movieModel.getOverview());
        String basePoster = "http://image.tmdb.org/t/p/w185/";
        String linkPoster = basePoster + movieModel.getPosterPath();

        Picasso.with(getActivity())
                .load(linkPoster).fit()
                .into(moviePoter);

        String linkback = basePoster + movieModel.getBackdrop();

        Picasso.with(getActivity())
                .load(linkback)
                .into(movieBack);

        if (realm.where(RealmMovie.class).equalTo("id", movieModel.getId()).findFirst() == null) {
            iv.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
        } else {
            iv.setImageResource(R.mipmap.ic_favorite_black_24dp);
        }
        String trailerLink = "http://api.themoviedb.org/3/movie/" +
                movieModel.getId() +
                "/videos?api_key=6aa860fc8c6328a719c4a59f2fc6f032";


        String reviewsLink = "http://api.themoviedb.org/3/movie/" +
                movieModel.getId() +
                "/reviews?api_key=6aa860fc8c6328a719c4a59f2fc6f032";
        showTrailer(trailerLink);
        Log.d(TAG, "showUserSelectedMovie: " + reviewsLink);
        showReview(reviewsLink);


    }

    private void showTrailer(String trailerLink) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, trailerLink, new Response.Listener <String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = null;
                    obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String trailer = jsonObject.getString("key");
                        trailers.add(trailer);
                        Log.d("trailer", "onResponse: " + trailers.get(i));
                    }
                    if (trailers.isEmpty())
                        trailerText.setText(" Sorry we don't Have any Trailer");
                    else
                        rvTrailer.setAdapter(new TrailersAdapter(getActivity(), trailers));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.d(TAG, "onErrorResponse: "+error.toString());
            }
        });
        Singlton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }

    private void showReview(String reviewLink) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, reviewLink, new Response.Listener <String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = null;
                    obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("results");
                    ReviewModel reviewModel;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String auth = jsonObject.getString("author");
                        String review = jsonObject.getString("content");
                        reviewModel = new ReviewModel(auth, review);
                        reviewModels.add(reviewModel);
                        Log.d(TAG, "onResponse: " + reviewModels.get(i).getAuth());
                    }
                    if (reviewModels.isEmpty())
                        reviewText.setText(" Sorry we don't Have any Review movie");
                    else
                        rvReview.setAdapter(new ReviewAdapter(getActivity(), reviewModels));
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
}

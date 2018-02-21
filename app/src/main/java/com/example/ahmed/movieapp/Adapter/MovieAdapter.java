package com.example.ahmed.movieapp.Adapter;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.movieapp.Activity.DetailActivity;
import com.example.ahmed.movieapp.Activity.MainActivity;
import com.example.ahmed.movieapp.DataBase.RealmMovie;
import com.example.ahmed.movieapp.Fragment.MainFragment;
import com.example.ahmed.movieapp.Models.MovieModel;
import com.example.ahmed.movieapp.NaviFrag;
import com.example.ahmed.movieapp.Pefs.PrefsController;
import com.example.ahmed.movieapp.R;
import com.example.ahmed.movieapp.SortBy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Ahmed on 12/8/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter <MovieAdapter.MovieHolder> {
    private MainFragment mainFragment;
    private NaviFrag naviFrag;
    private Fragment fragment;
    private ArrayList <MovieModel> movie_models;
    public static final String TAG = "MOVIEADAPTER";
    private PrefsController controller;
    MovieSorter sorter;

    Realm realm;

    public MovieAdapter(Fragment fragment, ArrayList <MovieModel> movie_models) {
        this.movie_models = movie_models;
        if (fragment instanceof MainFragment) {
            this.mainFragment = (MainFragment) fragment;
        } else if (fragment instanceof NaviFrag) {
            this.naviFrag = (NaviFrag) fragment;
        }
        this.fragment = fragment;
        realm = Realm.getDefaultInstance();
        controller = PrefsController.getInstance(fragment.getActivity());
        sorter = new MovieSorter();
    }


    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_movie, parent, false);
        return new MovieHolder(row);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, final int position) {
        MovieModel model = movie_models.get(position);
        holder.movie_title.setText(model.getTitle());
        holder.rbRate.setRating((Float.parseFloat(movie_models.get(position).getVoteRange()) / 10.0F) * 5.0F);
        holder.rbRate.setIsIndicator(false);
        if (fragment instanceof NaviFrag) {
            holder.rl.setPadding(20, 20, 20, 20);
        }
        String posterPath = model.getPosterPath();
        String baseUrl = "http://image.tmdb.org/t/p/";
        String recommendedSize = "w185";
        String completeUrl =
                new StringBuilder(baseUrl)
                        .append(recommendedSize)
                        .append(posterPath)
                        .toString();
        Picasso.with(fragment.getActivity()).load(completeUrl).fit().into(holder.movie_image);

/*
        Log.d(TAG, "onCreate"+realm.where(RealmMovie.class).equalTo("id",movie_models.get(position).getId()).findFirst());
*/

        if (realm.where(RealmMovie.class).equalTo("id", movie_models.get(position).getId()).findFirst() == null) {
            holder.movie_fav.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
        } else {
            holder.movie_fav.setImageResource(R.mipmap.ic_favorite_black_24dp);
        }
    }


    @Override
    public int getItemCount() {
        return movie_models.size();
    }

    public void sortByRate() {
        sorter.sortMovieModelsByRating(movie_models);
        notifyDataSetChanged();
    }

    public void sortByTitle() {
        sorter.sortMovieModelsByName(movie_models);
        notifyDataSetChanged();
    }


    class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView movie_title;
        me.zhanghai.android.materialratingbar.MaterialRatingBar rbRate;
        ImageView movie_image, movie_fav;
        RelativeLayout rl;

        MovieHolder(View itemView) {
            super(itemView);
            movie_title = itemView.findViewById(R.id.tv_title);
            rbRate = itemView.findViewById(R.id.rb_rate);
            movie_image = itemView.findViewById(R.id.iv_poster);
            movie_fav = itemView.findViewById(R.id.iv_fav);
            rl = itemView.findViewById(R.id.rl);
            movie_image.setOnClickListener(this);
            movie_fav.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            MainActivity mainActivity = (MainActivity) fragment.getActivity();
            if (view == movie_fav) {
                Log.d(TAG, "onClick: 1" + realm.where(RealmMovie.class).
                        equalTo("id", movie_models.get(getLayoutPosition()).getId())
                        .findFirst());
                if (realm.where(RealmMovie.class).equalTo("id", movie_models.get(getLayoutPosition()).getId()).findFirst()
                        == null) {
                    movie_fav.setImageResource(R.mipmap.ic_favorite_black_24dp);
                    save(movie_models.get(getLayoutPosition()));
                } else {
                    movie_fav.setImageResource(R.mipmap.ic_favorite_border_black_24dp);
                    delete(movie_models.get(getLayoutPosition()));
                }
                /*Log.i(TAG, "onClick: "+(boolean)(fragment instanceof NaviFrag));
                Log.i(TAG, "onClick: "+mainActivity.controller.getSortBy().equals("fav"));*/
                if (fragment instanceof NaviFrag || mainActivity.controller.getSortBy().equals("fav") ){
                    movie_models.remove(getLayoutPosition());
                    notifyDataSetChanged();
                }
            } else if (view == movie_image) {
                if (fragment instanceof MainFragment) {

                    if (mainActivity.isMyPhoneTablet()) {
                        if (MainActivity.isNwConnected(mainFragment.getActivity()) && controller.getSortBy() != "fav") {
                            mainFragment.startShow(controller.getSortBy());
                        } else if (MainActivity.isNwConnected(mainFragment.getActivity()) && controller.getSortBy() == "fav") {
                            mainFragment.start();
                        } else {
                            Toast.makeText(mainFragment.getActivity(), " please check you networkconnection We Show Your Favourite movie", Toast.LENGTH_LONG).show();
                            mainFragment.start();
                        }

                        Log.d(TAG, "onClick: is my phone tablet");
                        mainActivity.detailFragment.getView().setVisibility(View.VISIBLE);
                        //mainActivity.showDetail();
                        mainActivity.detailFragment.showUserSelectedMovie(movie_models.get(getAdapterPosition()));

                    } else {
                        Log.d(TAG, "onClick: isnot tablet ");
                        Intent intent = new Intent(mainActivity, DetailActivity.class);
                        intent.putExtra("item", movie_models.get(getLayoutPosition()));
                        mainActivity.startActivity(intent);
                    }
                } else {
                    MainActivity navi = (MainActivity) naviFrag.getActivity();
                    Log.d(TAG, "onClick: isnot tablet ");
                    Intent intent = new Intent(naviFrag.getActivity(), DetailActivity.class);
                    intent.putExtra("item", movie_models.get(getLayoutPosition()));
                    navi.startActivity(intent);
                }
            }

        }

    }

    private void delete(MovieModel movieModel) {
        final RealmResults <RealmMovie> results = realm.where(RealmMovie.class).equalTo("id", movieModel.getId()).findAll();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }

    private void save(final MovieModel movieModel) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.d(TAG, "execute: 1 relm");
                RealmMovie model = realm.createObject(RealmMovie.class); // Create a new object
                model.setId(movieModel.getId());
                model.setAdult(movieModel.getAdult());
                model.setBackdrop(movieModel.getBackdrop());
                model.setOriginalLang(movieModel.getOriginalLang());
                model.setOriginalTitle(movieModel.getOriginalTitle());
                model.setOverview(movieModel.getOverview());
                model.setPopularity(movieModel.getPopularity());
                model.setPosterPath(movieModel.getPosterPath());
                model.setTitle(movieModel.getTitle());
                model.setReleaseDate(movieModel.getReleaseDate());
                model.setVideo(movieModel.getVideo());
                model.setVoteCount(movieModel.getVoteCount());
                model.setVoteRange(movieModel.getVoteRange());
                Log.d(TAG, "execute: realm");
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Object save successfully");

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "onSuccess: Object save error");
                Toast.makeText(mainFragment.getActivity(), "Object save error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onViewDetachedFromWindow(MovieHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }
}

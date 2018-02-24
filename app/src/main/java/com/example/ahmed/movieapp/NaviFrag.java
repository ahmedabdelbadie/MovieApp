package com.example.ahmed.movieapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.ahmed.movieapp.Adapter.MovieAdapter;
import com.example.ahmed.movieapp.DataBase.RealmMovie;
import com.example.ahmed.movieapp.Fragment.MainFragment;
import com.example.ahmed.movieapp.Models.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


/**
 * A simple {@link Fragment} subclass.
 */
public class NaviFrag extends Fragment {
    private boolean mUserLearned, mFromSavePref;
    private static final String KEY_USER_LEARNED = "key_user_learned";
    private static final String FILE_NAME = "savePref";
    private static final String TAG = "BLANK";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private RecyclerView mRecyclerView;
    private View containerView;
    private ArrayList<MovieModel> movieList;
    MovieAdapter adapter;
    Realm realm;
    ImageView imageView ;


    public NaviFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUserLearned = Boolean.valueOf(getFromPref(getActivity(), KEY_USER_LEARNED, "false"));
        if (savedInstanceState != null) {
            mFromSavePref = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.fragment_navi, null);
        // Inflate the layout for this fragment
        realm = Realm.getDefaultInstance();
        mRecyclerView=v.findViewById(R.id.rv_item);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext() ,LinearLayoutManager.VERTICAL,false));
        imageView = v.findViewById(R.id.iv_b);
        Picasso.with(getActivity()).load(R.drawable.images).into(imageView);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public void setup(int blank_frag, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(blank_frag);
        getActivity().setTitle(R.string.fav);
        mDrawerLayout = drawerLayout;
        mActionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearned) {
                    mUserLearned = true;
                    saveToPref(getActivity(), KEY_USER_LEARNED, mUserLearned + "");
                }
                getActivity().invalidateOptionsMenu();
                start();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_favorite_black_24dp, getActivity().getTheme());
        mActionBarDrawerToggle.setHomeAsUpIndicator(drawable);
        mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        if (!mUserLearned && !mFromSavePref) {
            mDrawerLayout.openDrawer(containerView);
        }
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPref(Context c, String k, String v) {
        SharedPreferences preferences = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(k, v);
        editor.apply();
    }

    public static String getFromPref(Context c, String k, String d) {
        SharedPreferences preferences = c.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return preferences.getString(k, d);
    }
    public void start() {
        movieList = new ArrayList <>();
        try {
            MovieModel data;
            RealmResults<RealmMovie> results = realm.where(RealmMovie.class).findAll();
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
                adapter = new MovieAdapter(NaviFrag.this, movieList);
                mRecyclerView.setAdapter(adapter);
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "start: " + e);
        }

    }
}

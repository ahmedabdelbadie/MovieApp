package com.example.ahmed.movieapp.Pefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Abo Abdel-Badie on 11/20/2016.
 */

public class PrefsController {
    private SharedPreferences sharedPreferences;
    private static PrefsController instance;

    public static PrefsController getInstance(Context context){
        if (instance == null) instance = new PrefsController(context);
        return instance;
    }

    public PrefsController(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void saveSort(String sort){
        sharedPreferences.edit().putBoolean("remember", true)
                .putString("sortBy", sort)
                .apply();
    }

    public String getSortBy(){
        String sortBy;
        sortBy = sharedPreferences.getString("sortBy","http://api.themoviedb.org/3/movie/popular?api_key=ddcab0907164e022b72ad3818937cf00");
        return sortBy ;
    }
}

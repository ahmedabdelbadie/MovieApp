package com.example.ahmed.movieapp.Activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ahmed.movieapp.Adapter.MovieAdapter;
import com.example.ahmed.movieapp.Fragment.DetailFragment;
import com.example.ahmed.movieapp.Fragment.MainFragment;
import com.example.ahmed.movieapp.NaviFrag;
import com.example.ahmed.movieapp.Pefs.PrefsController;
import com.example.ahmed.movieapp.R;
import com.example.ahmed.movieapp.SortBy;
import com.example.ahmed.movieapp.services.MyService;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobService;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public MainFragment mainFragment;
    public DetailFragment detailFragment;
    private String popular, rated;
    public PrefsController controller;
    boolean isMyPhoneTablet = false;
    FragmentManager manager;

    private static final String TAG = "MAINACTIVITY";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        /*getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        myToolbar.setNavigationIcon(R.mipmap.ic_favorite_black_24dp);*/
        /*myToolbar.getNavigationIcon().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);*/
        //myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.mipmap.ic_favorite_black_24dp));
        setSupportActionBar(myToolbar);

        NaviFrag fragment = (NaviFrag) getSupportFragmentManager().findFragmentById(R.id.navifragment);
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        fragment.setup(R.id.navifragment,drawerLayout,myToolbar);
        mainFragment = (MainFragment) manager.findFragmentById(R.id.fragment_main);
        detailFragment = (DetailFragment) manager.findFragmentById(R.id.fragment_detail);
        controller = PrefsController.getInstance(this);
        Log.d("frag", "onCreate: " + detailFragment);

        if (detailFragment != null) {
            Log.d("frag", "onCreate: tablet mode ");
            //manager.beginTransaction().hide(detailFragment).commit();
            isMyPhoneTablet = true;
            manager.beginTransaction().hide(detailFragment).commit();
        }
        constructFloating();
        popular = getResources().getString(R.string.popular);
        rated = getResources().getString(R.string.rated);
        if (savedInstanceState==null){
        if (isNwConnected(this) && controller.getSortBy() != "fav") {
            mainFragment.startShow(controller.getSortBy());
        } else if (isNwConnected(this) && controller.getSortBy() == "fav") {
            mainFragment.start();
        } else {
            Toast.makeText(this, " please check you networkconnection We Show Your Favourite movie", Toast.LENGTH_LONG).show();
            mainFragment.start();
            mainFragment.start();
        }}
    }
    void constructService(){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyService.class) // the JobService that will be called
                .setTag("my-unique-tag")        // uniquely identifies the job
                .build();

        dispatcher.mustSchedule(myJob);

    }

    private void constructFloating() {
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.ic_home_black_18px);
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

// repeat many times:
        ImageView itemIcon1 = new ImageView(this);
        itemIcon1.setImageResource(R.drawable.ic_star_rate_black_18px);
        ImageView itemIcon2 = new ImageView(this);
        itemIcon2.setImageResource(R.drawable.ic_store_black_18px);

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        SubActionButton sortByRate = itemBuilder.setContentView(itemIcon1).build();
        SubActionButton sortByTitle = itemBuilder.setContentView(itemIcon2).build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(sortByRate)
                .addSubActionView(sortByTitle)
                // ...
                .attachTo(actionButton)
                .build();

        sortByRate.setTag("rate");
        sortByTitle.setTag("title");

        sortByRate.setOnClickListener(this);
        sortByTitle.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.m_rated:
                if (!controller.getSortBy().equals(rated)) {
                    controller.saveSort(rated);
                    mainFragment.startShow(rated);
                }
                break;

            case R.id.m_popular:
                if (!controller.getSortBy().equals(popular)) {
                    controller.saveSort(popular);
                    mainFragment.startShow(popular);
                }
                break;
            case R.id.m_fav:
                if (!controller.getSortBy().equals("fav")) {
                    controller.saveSort("fav");
                    mainFragment.start();
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public boolean isMyPhoneTablet() {
        return isMyPhoneTablet;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public static boolean isNwConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
        return nwInfo != null && nwInfo.isConnectedOrConnecting();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag().equals("rate")){
            mainFragment.adapter.sortByRate();

        }else if (view.getTag().equals("title") ){
            mainFragment.adapter.sortByTitle();
        }
    }
}

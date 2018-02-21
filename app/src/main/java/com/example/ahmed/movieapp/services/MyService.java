package com.example.ahmed.movieapp.services;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

/**
 * Created by Ahmed on 1/31/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyService extends com.firebase.jobdispatcher.JobService {
    

    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        Toast.makeText(this, "ahmed then ahmed ", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        return false;
    }
}

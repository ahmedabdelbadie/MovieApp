package com.example.ahmed.movieapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.example.ahmed.movieapp.Fragment.DetailFragment;
import com.example.ahmed.movieapp.Models.ReviewModel;
import com.example.ahmed.movieapp.R ;

import java.util.ArrayList;

/**
 * Created by Ahmed on 1/11/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter <ReviewAdapter.ReviewHolder> {
    ArrayList<ReviewModel> models ;
    Context context;
    private static final String TAG = "REVIEW";
    public ReviewAdapter(Context context, ArrayList<ReviewModel> models) {
        this.context=context;
        this.models = models ;
        Log.d(TAG, "ReviewAdapter: ");
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_review,parent,false);
        Log.d(TAG, "onCreateViewHolder: 1 "+v);
        Log.d(TAG, "onCreateViewHolder: "+R.layout.rv_item_review);
        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Log.d(TAG, "getItemCount: 5"+models.get(position).getAuth());

        Log.d(TAG, "onBindViewHolder: "+holder.tvAuth);

        holder.tvAuth.setText(models.get(position).getAuth());
        holder.tvReview.setText(models.get(position).getRev());
        Log.d(TAG, "getItemCount: 6");
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: 4");
        return models.size();

    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        TextView tvAuth , tvReview ;

        public ReviewHolder(View v) {
            super(v);
            Log.d(TAG, "ReviewHolder: 2");
            tvAuth= v.findViewById(R.id.tv_auth);
            tvReview = v.findViewById(R.id.tv_rev);
            Log.d(TAG, "ReviewHolder: 3");
        }
    }
}

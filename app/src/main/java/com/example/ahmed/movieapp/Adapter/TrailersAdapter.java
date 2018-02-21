package com.example.ahmed.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.movieapp.R;

import java.util.ArrayList;

/**
 * Created by Ahmed on 1/10/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {
    Context context ;
    ArrayList<String> trailers;
    public TrailersAdapter(Context context, ArrayList<String> trailers) {
    this.context=context;
    this.trailers=trailers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_trailer,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTrailer.setText(" Play Trailer "+(position+1));

    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTrailer ;
        ImageView iv ;
        ViewHolder(View itemView) {
            super(itemView);
            tvTrailer = itemView.findViewById(R.id.tv_trailer);
            iv = itemView.findViewById(R.id.iv_trail);
            tvTrailer.setOnClickListener(this);
            iv.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

                String url = "http://www.youtube.com/watch?v=" +
                        trailers.get((getLayoutPosition()));
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

        }
    }
}

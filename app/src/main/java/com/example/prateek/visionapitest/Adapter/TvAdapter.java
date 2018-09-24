package com.example.prateek.visionapitest.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prateek.visionapitest.Model.Tv;
import com.example.prateek.visionapitest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TvAdapter extends RecyclerView.Adapter<TvAdapter.TvViewHolder> {

    private List<Tv> tv;
    private int rowLayout;
    private Context context;
    private String poster_url = "http://image.tmdb.org/t/p/w185/";

    public static class TvViewHolder extends RecyclerView.ViewHolder {

        LinearLayout tvLayout;
        TextView tvTitle;
        TextView tvOvervire;
        ImageView ivTv;

        public TvViewHolder(View v) {
            super(v);
            tvLayout = (LinearLayout) v.findViewById(R.id.tv_layout);
            tvTitle = (TextView) v.findViewById(R.id.title);
            tvOvervire = (TextView) v.findViewById(R.id.overview);
            ivTv = (ImageView) v.findViewById(R.id.ivTv);
        }
    }

    public TvAdapter(List<Tv> tv, int rowLayout, Context context) {
        this.tv = tv;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public TvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new TvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TvViewHolder holder, int position) {
        holder.tvTitle.setText(tv.get(position).getName());
        holder.tvOvervire.setText(tv.get(position).getOverview());
        Picasso.with(context).load(poster_url + tv.get(position).getPosterPath()).into(holder.ivTv);
    }

    @Override
    public int getItemCount() {
        return tv.size();
    }
}

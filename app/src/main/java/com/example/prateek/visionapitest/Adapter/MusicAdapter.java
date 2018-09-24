package com.example.prateek.visionapitest.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prateek.visionapitest.Model.Track;
import com.example.prateek.visionapitest.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private List<Track> tracks;
    private int rowLayout;
    private Context context;

    public static class MusicViewHolder extends RecyclerView.ViewHolder {

        LinearLayout musicLayout;
        TextView trackTitle;
        TextView trackArtist;
        ImageView ivTrack;

        public MusicViewHolder(View v) {
            super(v);
            musicLayout = (LinearLayout) v.findViewById(R.id.music_layout);
            trackTitle = (TextView) v.findViewById(R.id.title);
            trackArtist = (TextView) v.findViewById(R.id.artist);
            ivTrack = (ImageView) v.findViewById(R.id.ivTrack);
        }
    }

    public MusicAdapter(List<Track> tracks, int rowLayout, Context context) {
        this.tracks = tracks;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        holder.trackTitle.setText(tracks.get(position).getName());
        holder.trackArtist.setText(tracks.get(position).artist.getName());
        Picasso.with(context).load(tracks.get(position).image.get(3).getText()).resize(150, 150).into(holder.ivTrack);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

}

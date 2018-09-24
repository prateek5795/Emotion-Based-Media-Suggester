package com.example.prateek.visionapitest.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tracks {

    @SerializedName("track")
    public List<Track> track = null;

    @SerializedName("@attr")
    public Attr_ attr;

    public Tracks(List<Track> track, Attr_ attr) {
        this.track = track;
        this.attr = attr;
    }

    public List<Track> getTrack() {
        return track;
    }

    public Attr_ getAttr() {
        return attr;
    }
}

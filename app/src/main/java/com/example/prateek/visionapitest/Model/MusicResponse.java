package com.example.prateek.visionapitest.Model;

import com.google.gson.annotations.SerializedName;

public class MusicResponse {

    @SerializedName("tracks")
    public Tracks tracks;

    public MusicResponse(Tracks tracks) {
        this.tracks = tracks;
    }

    public Tracks getTracks() {
        return tracks;
    }
}

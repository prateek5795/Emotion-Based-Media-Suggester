package com.example.prateek.visionapitest.Model;

import com.google.gson.annotations.SerializedName;

public class Artist {
    @SerializedName("name")
    public String name;

    @SerializedName("mbid")
    public String mbid;

    @SerializedName("url")
    public String url;

    public Artist(String name, String mbid, String url) {
        this.name = name;
        this.mbid = mbid;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getMbid() {
        return mbid;
    }

    public String getUrl() {
        return url;
    }
}

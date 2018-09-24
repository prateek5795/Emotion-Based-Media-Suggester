package com.example.prateek.visionapitest.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Track {

    @SerializedName("name")
    public String name;

    @SerializedName("duration")
    public String duration;

    @SerializedName("mbid")
    public String mbid;

    @SerializedName("url")
    public String url;

    @SerializedName("streamable")
    public Streamable streamable;

    @SerializedName("artist")
    public Artist artist;

    @SerializedName("image")
    public List<Image> image = null;

    @SerializedName("@attr")
    public Attr attr;

    public Track(String name, String duration, String mbid, String url, Streamable streamable, Artist artist, List<Image> image, Attr attr) {
        this.name = name;
        this.duration = duration;
        this.mbid = mbid;
        this.url = url;
        this.streamable = streamable;
        this.artist = artist;
        this.image = image;
        this.attr = attr;
    }

    public String getName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    public String getMbid() {
        return mbid;
    }

    public String getUrl() {
        return url;
    }

    public Streamable getStreamable() {
        return streamable;
    }

    public Artist getArtist() {
        return artist;
    }

    public List<Image> getImage() {
        return image;
    }

    public Attr getAttr() {
        return attr;
    }
}

package com.example.prateek.visionapitest.Model;

import com.google.gson.annotations.SerializedName;

public class Streamable {

    @SerializedName("#text")
    public String text;

    @SerializedName("fulltrack")
    public String fulltrack;

    public Streamable(String text, String fulltrack) {
        this.text = text;
        this.fulltrack = fulltrack;
    }

    public String getText() {
        return text;
    }

    public String getFulltrack() {
        return fulltrack;
    }
}

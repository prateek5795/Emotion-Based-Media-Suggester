package com.example.prateek.visionapitest.Model;

import com.google.gson.annotations.SerializedName;

public class Attr {

    @SerializedName("rank")
    public String rank;

    public Attr(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }
}

package com.example.prateek.visionapitest.Model;

import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("#text")
    public String text;

    @SerializedName("size")
    public String size;

    public Image(String text, String size) {
        this.text = text;
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public String getSize() {
        return size;
    }
}

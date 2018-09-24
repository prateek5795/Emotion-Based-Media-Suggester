package com.example.prateek.visionapitest.Model;

import com.google.gson.annotations.SerializedName;

public class Attr_ {

    @SerializedName("tag")
    public String tag;

    @SerializedName("page")
    public String page;

    @SerializedName("perPage")
    public String perPage;

    @SerializedName("totalPages")
    public String totalPages;

    @SerializedName("total")
    public String total;

    public Attr_(String tag, String page, String perPage, String totalPages, String total) {
        this.tag = tag;
        this.page = page;
        this.perPage = perPage;
        this.totalPages = totalPages;
        this.total = total;
    }

    public String getTag() {
        return tag;
    }

    public String getPage() {
        return page;
    }

    public String getPerPage() {
        return perPage;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public String getTotal() {
        return total;
    }
}

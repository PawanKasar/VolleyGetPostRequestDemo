package com.example.apicallingdemo.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pawan on 26-03-2018.
 */

public class DataModelClass {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("imageurl")
    @Expose
    private String imageurl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}

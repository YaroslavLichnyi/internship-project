package com.example.internshipproject.entities;

import java.io.Serializable;
import java.util.List;

import com.example.internshipproject.entities.Film;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Search implements Serializable
{

    @SerializedName("Search")
    @Expose
    private List<Film> search = null;
    @SerializedName("totalResults")
    @Expose
    private String totalResults;
    @SerializedName("Response")
    @Expose
    private String response;
    //private final static long serialVersionUID = -5564941752548845062L;

    public List<Film> getSearch() {
        return search;
    }

    public void setSearch(List<Film> search) {
        this.search = search;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}
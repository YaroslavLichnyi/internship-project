package com.example.internshipproject.entities;


import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Represents a film. Generates based on JSON document that
 * {@link com.example.internshipproject.FilmRepository} gets from server.
 */
public class Film implements Serializable {

    @SerializedName("Title")
    @Expose
    private String title;

    @SerializedName("Year")
    @Expose
    private String year;

    @SerializedName("imdbID")
    @Expose
    private String imdbID;

    @SerializedName("Type")
    @Expose
    private String type;

    @SerializedName("Poster")
    @Expose
    private String poster;

    private final static long serialVersionUID = 834745676111564182L;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    @Override
    public String toString() {
        return "Film{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", type='" + type + '\'' +
                ", poster='" + poster + '\'' +
                '}';
    }
}
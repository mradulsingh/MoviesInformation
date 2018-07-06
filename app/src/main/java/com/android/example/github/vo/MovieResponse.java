package com.android.example.github.vo;

import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.android.example.github.db.GithubTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@TypeConverters(GithubTypeConverters.class)
public class MovieResponse implements Serializable {

    @SerializedName("page")
    private Long page;

    @SerializedName("total_results")
    private Long totalResults;

    @SerializedName("total_pages")
    private Long totalPages;

    @SerializedName("results")
    private ArrayList<MovieInfo> movieInfoItems;

    public Long getPage() {
        return page;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public ArrayList<MovieInfo> getMovieInfoItems() {
        return movieInfoItems;
    }

    public void setMovieInfoItems(ArrayList<MovieInfo> movieInfoItems) {
        this.movieInfoItems = movieInfoItems;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "page=" + page +
                ", totalResults=" + totalResults +
                ", totalPages=" + totalPages +
                ", movieInfoItems=" + movieInfoItems +
                '}';
    }
}



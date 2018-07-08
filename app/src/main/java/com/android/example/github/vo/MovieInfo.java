package com.android.example.github.vo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Build;
import android.support.annotation.NonNull;

import com.android.example.github.db.GithubTypeConverters;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "MovieInfo")
@TypeConverters(GithubTypeConverters.class)
public class MovieInfo implements Serializable {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private String movieId;

    @SerializedName("vote_average")
    private String voteAverage;

    @NonNull
    @SerializedName("title")
    private String title;

    @NonNull
    @SerializedName("overview")
    private String description;

    @NonNull
    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("genre_ids")
    private ArrayList<String> genreIds;

    @NonNull
    @SerializedName("release_date")
    private String releaseDate;

    @NonNull
    @SerializedName("original_title")
    private String originalTitle;

    @NonNull
    @SerializedName("vote_count")
    private Integer voteCount;

    @SerializedName("poster_path")
    private String moviePosterURL;

    @NonNull
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(@NonNull String movieId) {
        this.movieId = movieId;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(@NonNull String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public ArrayList<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(ArrayList<String> genreIds) {
        this.genreIds = genreIds;
    }

    @NonNull
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(@NonNull String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @NonNull
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(@NonNull String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @NonNull
    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(@NonNull Integer voteCount) {
        this.voteCount = voteCount;
    }

    @NonNull
    public String getMoviePosterURL() {
        return moviePosterURL;
    }

    public void setMoviePosterURL(@NonNull String moviePosterURL) {
        this.moviePosterURL = moviePosterURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieInfo movieInfo = (MovieInfo) o;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.equals(movieId, movieInfo.movieId);
        } else {
            return movieId.equals(movieInfo.movieId);
        }
    }

    @Override
    public int hashCode() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Objects.hash(movieId);
        } else {
            return movieId.hashCode();
        }
    }

    @Override
    public String toString() {
        return "MovieInfo{" +
                "movieId='" + movieId + '\'' +
                ", voteAverage='" + voteAverage + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", genreIds=" + genreIds +
                ", releaseDate='" + releaseDate + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", voteCount=" + voteCount +
                ", moviePosterURL='" + moviePosterURL + '\'' +
                '}';
    }
}

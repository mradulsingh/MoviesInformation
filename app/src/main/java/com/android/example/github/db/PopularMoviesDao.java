package com.android.example.github.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.android.example.github.vo.MovieInfo;

import java.util.List;

@Dao
public interface PopularMoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(MovieInfo... movieInfo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieList(List<MovieInfo> movieList);

    @Query("SELECT * FROM MovieInfo")
    LiveData<List<MovieInfo>> getAllMovies();

//    @Query("SELECT * FROM MovieInfo WHERE original_title = :itemId")
//    LiveData<MovieInfo> getMovieById(String itemId);

//    @Delete
//    void deleteMovie(MovieInfo item);
}

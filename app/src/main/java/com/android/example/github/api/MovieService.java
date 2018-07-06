package com.android.example.github.api;

import android.arch.lifecycle.LiveData;

import com.android.example.github.vo.MovieInfo;
import com.android.example.github.vo.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/{movieType}")
    Call<MovieResponse> getMovieListCall(
            @Path("movieType") String movieType,
            @Query("api_key") String apiKey,
            @Query("page") int page
            );

    @GET("movie/{movieType}")
    LiveData<ApiResponse<MovieResponse>> getMovieListLiveData(
            @Path("movieType") String movieType,
            @Query("api_key") String apiKey,
            @Query("page") int page);
}

package com.android.example.github.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.android.example.github.api.ApiResponse;
import com.android.example.github.api.MovieService;
import com.android.example.github.db.MovieDb;
import com.android.example.github.vo.MovieInfo;
import com.android.example.github.vo.MovieResponse;
import com.android.example.github.vo.Resource;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class FetchNextMoviePageTask implements Runnable {

    private static final String API_KEY = "1629d9f319180fab65a709e65ca9a077";

    private final MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
    private final String movieType;
    private final MovieService movieService;
    private final MovieDb movieDb;
    private int pageIndex;

    public FetchNextMoviePageTask(String movieType, MovieService movieService,
                                  MovieDb movieDb, int pageIndex) {
        this.movieType = movieType;
        this.movieService = movieService;
        this.movieDb = movieDb;
        this.pageIndex = pageIndex;
    }

    @Override
    public void run() {
        LiveData<List<MovieInfo>> mld =  movieDb.movieDao().getAllMovies();

        if (mld == null) {
            liveData.postValue(null);
            return;
        }
        final Integer nextPage = pageIndex;
        if (nextPage == null){
            liveData.postValue(Resource.success(false));
            return;
        }
        try {
            Response<MovieResponse> response = movieService.getMovieListCall(
                    movieType, API_KEY, nextPage ).execute();
            ApiResponse<MovieResponse> apiResponse = new ApiResponse<>(response);
            if (apiResponse.isSuccessful()){
                if (apiResponse.body != null
                        && apiResponse.body.getMovieInfoItems() != null
                        && !apiResponse.body.getMovieInfoItems().isEmpty()) {
                    for (MovieInfo movie : apiResponse.body.getMovieInfoItems()) {
                        try {
                            Log.d(FetchNextMoviePageTask.class.getSimpleName(), movie.toString());
                            movieDb.beginTransaction();
                            movieDb.movieDao().insertMovie(movie);
                            movieDb.setTransactionSuccessful();
                        } finally {
                            movieDb.endTransaction();
                        }
                        liveData.postValue(Resource.success(apiResponse.getNextPage() != null));
                    }
                }
            }else {
                liveData.postValue(Resource.error(apiResponse.errorMessage, true));
            }

        } catch (IOException e) {
            liveData.postValue(Resource.error(e.getMessage(), true));
        }
    }

    LiveData<Resource<Boolean>> getLiveData() {
        return liveData;
    }
}

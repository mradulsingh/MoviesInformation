package com.android.example.github.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.example.github.AppExecutors;
import com.android.example.github.api.ApiResponse;
import com.android.example.github.api.MovieService;
import com.android.example.github.db.PopularMoviesDao;
import com.android.example.github.db.MovieDb;
import com.android.example.github.util.AbsentLiveData;
import com.android.example.github.util.RateLimiter;
import com.android.example.github.vo.MovieInfo;
import com.android.example.github.vo.MovieResponse;
import com.android.example.github.vo.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MovieRepository {

    private PopularMoviesDao movieDao;
    private MovieService movieService;
    private MovieDb movieDb;
    private AppExecutors appExecutors;

    public static final String API_KEY = "1629d9f319180fab65a709e65ca9a077";

    @Inject
    public MovieRepository(PopularMoviesDao movieDao, MovieService movieService, MovieDb movieDb,
                           AppExecutors appExecutors) {
        this.movieDao = movieDao;
        this.movieService = movieService;
        this.movieDb = movieDb;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<MovieResponse>> loadMovies(String movieType, int pageIndex){
        return new NetworkBoundResource<MovieResponse, MovieResponse>(appExecutors){
            @Override
            protected void saveCallResult(@NonNull MovieResponse item) {
                if (item.getMovieInfoItems() != null && !item.getMovieInfoItems().isEmpty())
                    movieDao.insertMovieList(item.getMovieInfoItems());
            }

            @Override
            protected boolean shouldFetch(@Nullable MovieResponse data) {
                return data == null || data.getMovieInfoItems() == null || data.getMovieInfoItems().isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<MovieResponse> loadFromDb() {
                return Transformations.switchMap(movieDao.getAllMovies(), (movies) -> {
                    if (movies != null && !movies.isEmpty()) {
                        MutableLiveData<MovieResponse> mld = new MutableLiveData<>();
                        MovieResponse movieResponse = new MovieResponse();
                        ArrayList<MovieInfo> movieInfos = new ArrayList<>();
                        movieInfos.addAll(movies);
                        movieResponse.setMovieInfoItems(movieInfos);
                        mld.setValue(movieResponse);
                        return mld;
                    } else {
                        return AbsentLiveData.create();
                    }
                });
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MovieResponse>> createCall() {
                return movieService.getMovieListLiveData(movieType, API_KEY, pageIndex);
            }

        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> searchNextMoviePage(int pageIndex, String movieType) {
        FetchNextMoviePageTask fetchNextSearchPageTask = new FetchNextMoviePageTask(
                movieType, movieService, movieDb, pageIndex);
        appExecutors.networkIO().execute(fetchNextSearchPageTask);
        return fetchNextSearchPageTask.getLiveData();
    }

}

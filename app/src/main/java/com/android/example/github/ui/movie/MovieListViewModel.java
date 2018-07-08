package com.android.example.github.ui.movie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.widget.ProgressBar;

import com.android.example.github.api.MovieService;
import com.android.example.github.repository.MovieRepository;
import com.android.example.github.util.AbsentLiveData;
import com.android.example.github.util.Objects;
import com.android.example.github.util.RequestConstant;
import com.android.example.github.vo.MovieInfo;
import com.android.example.github.vo.MovieResponse;
import com.android.example.github.vo.Resource;

import javax.inject.Inject;

public class MovieListViewModel extends ViewModel {

    private MutableLiveData<Integer> nextPageIndex = new MutableLiveData<>();

    private Integer pageIndex = 1;

    private String movieType = RequestConstant.MovieListType.topRated;

    private LiveData<Resource<MovieResponse>> movies;

    private MovieRepository movieRepository;

    private NextPageHandler nextPageHandler;

    /**
     * getting reference of the Movie repository
     */
    @Inject
    public MovieListViewModel(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
        nextPageHandler = new MovieListViewModel.NextPageHandler(this.movieRepository);
        nextPageIndex.setValue(pageIndex);
    }

    @VisibleForTesting
    public LiveData<Resource<MovieResponse>> getResults() {
        movies = movieRepository.loadMovies(movieType, pageIndex);
        return movies;
    }

    @VisibleForTesting
    public LiveData<MovieListViewModel.LoadMoreState> getLoadMoreStatus() {
        return nextPageHandler.getLoadMoreState();
    }

    @VisibleForTesting
    public void loadNextPage(int m) {
        if (nextPageHandler.hasMore && nextPageIndex.getValue() >= 0)
            Log.d("page index ...." , nextPageIndex.getValue().toString() + m);
            nextPageHandler.getNextPage(nextPageIndex.getValue() + m, movieType);
    }

    public void setMovieType(String movieType) {
        this.movieType = movieType;
    }

    static class LoadMoreState {
        private final boolean running;
        private final String errorMessage;
        private boolean handledError = false;

        LoadMoreState(boolean running, String errorMessage) {
            this.running = running;
            this.errorMessage = errorMessage;
        }

        boolean isRunning() {
            return running;
        }

        String getErrorMessage() {
            return errorMessage;
        }

        String getErrorMessageIfNotHandled() {
            if (handledError) {
                return null;
            }
            handledError = true;
            return errorMessage;
        }
    }

    @VisibleForTesting
    static class NextPageHandler implements Observer<Resource<Boolean>> {

        @Nullable
        private LiveData<Resource<Boolean>> nextPageLiveData;
        private final MutableLiveData<LoadMoreState> loadMoreState = new MutableLiveData<>();

        private final MovieRepository repository;

        @VisibleForTesting
        boolean hasMore;

        @VisibleForTesting
        NextPageHandler(MovieRepository repository) {
            this.repository = repository;
            reset();
        }

        public void getNextPage(Integer nextPageIndex, String movieType) {
            unregister();
            nextPageLiveData = repository.searchNextMoviePage(nextPageIndex, movieType);
            loadMoreState.setValue(new MovieListViewModel.LoadMoreState(true, null));
            nextPageLiveData.observeForever(this);
        }

        @Override
        public void onChanged(@Nullable Resource<Boolean> result) {
            if (result == null) {
                reset();
            } else {
                switch (result.status) {
                    case SUCCESS:

                        hasMore = Boolean.TRUE.equals(result.data);
                        unregister();
                        loadMoreState.setValue(new MovieListViewModel.LoadMoreState(false, null));
                        break;
                    case ERROR:
                        hasMore = true;
                        unregister();
                        loadMoreState.setValue(new MovieListViewModel.LoadMoreState(false, result.message));
                        break;
                }
            }
        }

        private void unregister() {
            if (nextPageLiveData != null) {
                nextPageLiveData.removeObserver(this);
                nextPageLiveData = null;
            }
        }

        private void reset() {
            unregister();
            hasMore = true;
            loadMoreState.setValue(new MovieListViewModel.LoadMoreState(false,
                    null));
        }

        MutableLiveData<MovieListViewModel.LoadMoreState> getLoadMoreState() {
            return loadMoreState;
        }
    }
}

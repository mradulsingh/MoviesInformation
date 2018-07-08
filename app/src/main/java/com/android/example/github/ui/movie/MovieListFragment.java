package com.android.example.github.ui.movie;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
//import android.databinding.DataBindingComponent;
import android.content.Context;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.android.example.github.R;

//import com.android.example.github.binding.FragmentDataBindingComponent;
import com.android.example.github.binding.FragmentDataBindingComponent;
import com.android.example.github.databinding.ActivityMovieListFragmentBinding;
//import com.android.example.github.di.AppModule;
import com.android.example.github.di.Injectable;
import com.android.example.github.util.AutoClearedValue;

import javax.inject.Inject;

public class MovieListFragment extends Fragment implements Injectable{

    int m = 0;

    public static final String LOG_TAG = MovieListFragment.class.getSimpleName();

    MovieListViewModel movieListViewModel;

    AutoClearedValue<ActivityMovieListFragmentBinding> autoClearedValueBinding;
    AutoClearedValue<MovieListAdapter> movieListAdapterAutoClearedValue;
    ActivityMovieListFragmentBinding binding;
    
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(
                getContext()), R.layout.activity_movie_list_fragment, container, false,
                dataBindingComponent);
        autoClearedValueBinding = new AutoClearedValue<>(this, binding);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        movieListViewModel = ViewModelProviders.of(this, viewModelProviderFactory)
                .get(MovieListViewModel.class);
        initRecyclerView();
        initMovieList();
    }

    private void initRecyclerView() {
        MovieListAdapter adapter = new MovieListAdapter(dataBindingComponent, this);
        autoClearedValueBinding.get().recyclerList.setAdapter(adapter);
        movieListAdapterAutoClearedValue = new AutoClearedValue<>(this, adapter);
        autoClearedValueBinding.get().recyclerList.setItemAnimator(new DefaultItemAnimator());
        autoClearedValueBinding.get().recyclerList.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();
                int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastPosition == movieListAdapterAutoClearedValue.get().getItemCount() - 1){
                    m = m + 1;
                    autoClearedValueBinding.get().nextPageProgressBar.setVisibility(View.VISIBLE);
                    movieListViewModel.loadNextPage(m);
                }
            }
        });
    }

    private void dialogForMovieQueries() {
        MovieFilterFragment dialog = MovieFilterFragment.newInstance(this);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), LOG_TAG);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            dialogForMovieQueries();
        } else if (item.getItemId() == R.id.popularMovies) {
            m = 0;
            RefreshFragment();
//            loadFirstPage(m);
        } else if (item.getItemId() == R.id.nowPlayingMovies) {
            m = 1;
            RefreshFragment();
//            loadFirstPage(m);
        }
        return super.onOptionsItemSelected(item);
    }

    /** For refreshing fragment after selecting filter */
    private void RefreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    private void initMovieList(){
        movieListViewModel.getResults().observe(this, (resource) -> {
            if (resource != null) {
                switch (resource.status) {
                    case LOADING:
                        Log.d(LOG_TAG, "loading...");
                        autoClearedValueBinding.get().nextPageProgressBar.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        Log.d(LOG_TAG, "successful...");
                        autoClearedValueBinding.get().nextPageProgressBar.setVisibility(View.GONE);
                        if (resource.data != null
                                && resource.data.getMovieInfoItems() != null
                                && !resource.data.getMovieInfoItems().isEmpty()) {
                            movieListAdapterAutoClearedValue.get().replace(
                                    resource.data.getMovieInfoItems());
                        } else {
                            movieListAdapterAutoClearedValue.get().replace(null);
                        }
                        break;
                    case ERROR:
                        autoClearedValueBinding.get().nextPageProgressBar.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    ///////////////////////////////////////////
//    private void initSearchInputListener() {
//        autoClearedValueBinding.get().movieQuery.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                doSearch(v);
//                return true;
//            }
//            return false;
//        });
//        autoClearedValueBinding.get().movieQuery.setOnKeyListener((v, keyCode, event) -> {
//            if ((event.getAction() == KeyEvent.ACTION_DOWN)
//                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                doSearch(v);
//                return true;
//            }
//            return false;
//        });
//    }

//    private void doSearch(View v) {
//        String query = autoClearedValueBinding.get().movieQuery.getText().toString();
//        // Dismiss keyboard
//        dismissKeyboard(v.getWindowToken());
////        autoClearedValueBinding.get().setQuery(query);
//        movieListViewModel.setQuery(query);
//    }

    private void dismissKeyboard(IBinder windowToken) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
    }
}

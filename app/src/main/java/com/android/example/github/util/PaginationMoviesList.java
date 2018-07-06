package com.android.example.github.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class PaginationMoviesList extends RecyclerView.OnScrollListener {
    LinearLayoutManager manager;

    public PaginationMoviesList(LinearLayoutManager manager) {
        this.manager = manager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleList = manager.getChildCount();
        int scrolledUpList = manager.findFirstVisibleItemPosition();
        int totalItemList = manager.getItemCount();

        if (!isLoading() && !isLastPage()){
            if ((visibleList + scrolledUpList >= totalItemList) && scrolledUpList >= 0 &&
                    totalItemList >= getTotalPageCount()){
                loadMoreItems();
            }
        }
    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLoading();

    public abstract boolean isLastPage();
}

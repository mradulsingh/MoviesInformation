package com.android.example.github.ui.movie;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.example.github.R;
import com.android.example.github.databinding.ItemListBinding;
import com.android.example.github.ui.common.DataBoundListAdapter;
import com.android.example.github.vo.MovieInfo;

public class MovieListAdapter extends DataBoundListAdapter<MovieInfo, ItemListBinding> {

    private android.databinding.DataBindingComponent dataBindingComponent;

    public MovieListAdapter(DataBindingComponent dataBindingComponent){
        this.dataBindingComponent = dataBindingComponent;
    }

    @Override
    protected ItemListBinding createBinding(ViewGroup parent) {
        ItemListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_list, parent, false, dataBindingComponent);
        return binding;
    }

    @Override
    protected void bind(ItemListBinding binding, MovieInfo item) {
        binding.setMovieInfo(item);
    }

    @Override
    protected boolean areItemsTheSame(MovieInfo oldItem, MovieInfo newItem) {
        return oldItem.equals(newItem);
    }

    @Override
    protected boolean areContentsTheSame(MovieInfo oldItem, MovieInfo newItem) {
        return oldItem.equals(newItem);
    }
}

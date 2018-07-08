package com.android.example.github.ui.movie;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.example.github.R;
import com.android.example.github.databinding.ItemListBinding;
import com.android.example.github.ui.common.DataBoundListAdapter;
import com.android.example.github.vo.MovieInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class MovieListAdapter extends DataBoundListAdapter<MovieInfo, ItemListBinding> {

    private DataBindingComponent dataBindingComponent;
    private Fragment fragment;

    public MovieListAdapter(DataBindingComponent dataBindingComponent, Fragment fragment){
        this.dataBindingComponent = dataBindingComponent;
        this.fragment = fragment;
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
        Glide.with(fragment)
                .load("https://image.tmdb.org/t/p/w150" + Uri.parse(item.getMoviePosterURL()))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        binding.loadingIndicator.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        binding.loadingIndicator.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(binding.movieImage);
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

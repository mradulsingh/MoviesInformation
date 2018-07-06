/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.example.github.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.util.Log;

import com.android.example.github.api.GithubService;
import com.android.example.github.api.MovieService;
import com.android.example.github.api.TrustingNetworkClient;
import com.android.example.github.db.GithubDb;
import com.android.example.github.db.PopularMoviesDao;
import com.android.example.github.db.MovieDb;
import com.android.example.github.db.RepoDao;
import com.android.example.github.db.UserDao;
import com.android.example.github.util.LiveDataCallAdapterFactory;
import com.android.example.github.viewmodel.TLSSocketFactory;

import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Singleton;
import javax.net.ssl.X509TrustManager;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    @Singleton
    @Provides
    public static MovieService provideMovieService() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(
                (message) -> Log.d("API_LOG", message));
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(httpLoggingInterceptor);
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(client.build())
                .build()
                .create(MovieService.class);
    }

    @Singleton @Provides
    GithubService provideGithubService() {

        String url = "https://api.github.com/";
        try {
            URI uri = new URI(url);
            OkHttpClient client;
            try {
                client = TrustingNetworkClient.getInstace(uri.getHost());
            } catch (Exception e) {
                e.printStackTrace();
                client = null;
            }

            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory());
            if (client != null) builder.client(client);

            return builder.build()
                    .create(GithubService.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private OkHttpClient okHttpClient() {
//        return new OkHttpClient.Builder()
//                .addInterceptor(chain -> {
//
//                    Request original = chain.request();
//                    HttpUrl url = original.url().newBuilder()
//                            .addQueryParameter("apikey", "1629d9f319180fab65a709e65ca9a077")
//                            .addQueryParameter("plot", "full")
//                            .build();
//                    Request.Builder requestBuilder = original.newBuilder()
//                            .url(url)
//                            .method(original.method(), original.body());
//                    Request request = requestBuilder.build();
//                    return chain.proceed(request);
//                })
//                .build();
//    }


    @Singleton
    @Provides
    GithubDb provideDb(Application app) {
        return Room.databaseBuilder(app, GithubDb.class,"github.db").build();
    }

    @Singleton
    @Provides
    UserDao provideUserDao(GithubDb db) {
        return db.userDao();
    }

    @Singleton
    @Provides
    RepoDao provideRepoDao(GithubDb db) {
        return db.repoDao();
    }

    @Singleton
    @Provides
    MovieDb provideMovieDb(Application application){
        return Room.databaseBuilder(application, MovieDb.class, "MovieDb.db").build();
    }

    @Singleton
    @Provides
    PopularMoviesDao provideMovieDao(MovieDb movieDb){
        return movieDb.movieDao();
    }



}

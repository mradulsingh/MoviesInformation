package com.android.example.github.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.android.example.github.vo.MovieInfo;

@Database(entities = {MovieInfo.class}, version = 1)
public abstract class MovieDb extends RoomDatabase {
    private static final String DB_NAME = "MovieDb.db";

    abstract public PopularMoviesDao movieDao();

//    /**
//     * Singleton object of this class so that no other object of database is created
//     */
//    private static volatile MovieDb instance;
//
//    static synchronized MovieDb getDatabase(Context context) {
//        if (instance == null){
//            synchronized (MovieDb.class){
//                if (instance == null){
//                    //create database
//                }
//            }
//        }
//
//        return instance;
//    }

    private static MovieDb create(final Context context) {
        return Room.databaseBuilder(
                context,
                MovieDb.class,
                DB_NAME).build();
    }
}

package com.example.saunakc.tmdb.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Movie;
import android.util.Log;

/**
 * Created by saunakc on 08/06/16.
 *
 * This is the class that manages all the local DB configuration
 */


public class MovieDbHelper extends SQLiteOpenHelper{
    static final String LOG_TAG=SQLiteOpenHelper.class.getName();

    private static final int DATABASE_VERSION=1;
    static final String DATABASE_NAME="movie.db";

    public MovieDbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating a table for holding data for Popular Movies Section

          final String CREATE_POPULAR_MOVIES_TABLE =
                "CREATE TABLE " + MovieContract.PopularMovies.TABLE_NAME + " ( " +
                        MovieContract.PopularMovies._ID + " INTEGER PRIMARY KEY, " +
                        MovieContract.PopularMovies.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                        MovieContract.PopularMovies.COLUMN_ADULT+" TEXT, " +
                        MovieContract.PopularMovies.COLUMN_POPULARITY+" TEXT, " +
                        MovieContract.PopularMovies.COLUMN_OVERVIEW + " TEXT, " +
                        MovieContract.PopularMovies.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieContract.PopularMovies.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                        MovieContract.PopularMovies.COLUMN_VIDEO + " TEXT " +
                        " );";

        Log.d(LOG_TAG," #### About to execute create table query: " + CREATE_POPULAR_MOVIES_TABLE);
        db.execSQL(CREATE_POPULAR_MOVIES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS" + MovieContract.PopularMovies.TABLE_NAME);
        onCreate(db);
    }
}
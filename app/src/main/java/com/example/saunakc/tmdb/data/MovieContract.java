package com.example.saunakc.tmdb.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by saunakc on 08/06/16.
 */
public class MovieContract {
    /* we shall make use of CONTENT_AUTHORITY to create our Base URI.
   * All that will make use of this content provider will always make use of the below base URI*/
    public static final String CONTENT_AUTHORITY="com.example.saunakc.tmdb";
    public static final Uri BASE_CONTENT_URI =Uri.parse("content://"+CONTENT_AUTHORITY);

    /*Possible paths that can be appended to create different URIs are
    * content://com.example.saunakc.tmdb.data/popularmovies
    * content://com.example.saunakc.tmdb.data/topgrossingmovies*/

    public static final String PATH_POPULARMOVIES ="popularmovies";
    public static final String PATH_TOPMOVIES ="topgrossingmovies";

    public static final class PopularMovies implements BaseColumns {

        /* In order to access the data for Popular Movies, a URI will be required.
        * This URI needs to be decied in this inner class .. ......*/

        public static final Uri CONTENT_URI= BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULARMOVIES).build();

        /* There could be two type of values being returned,
        *   1. Either a group of values will be returned .
        *   2. Or a single item will be returned .... */

        public static final String CONTENT_TYPE=
                ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_POPULARMOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_POPULARMOVIES;

        public static final String TABLE_NAME="popularmovies";
        public static final String COLUMN_POSTER_PATH="posterpath";//done
        public static final String COLUMN_ADULT="adult";//done
        public static final String COLUMN_OVERVIEW="overview";//done
        public static final String COLUMN_RELEASE_DATE="releasedate";
        public static final String COLUMN_ORIGINAL_TITLE="originaltitle";//done
        public static final String COLUMN_POPULARITY="popularity";
        public static final String COLUMN_VIDEO="video";



        public static Uri buildPopularMoviesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);

        }
    }
}

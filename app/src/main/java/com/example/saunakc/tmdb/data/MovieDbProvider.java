package com.example.saunakc.tmdb.data;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by saunakc on 08/06/16.
 */
public class MovieDbProvider extends ContentProvider{

    public static final UriMatcher sUriMatcher=buildUriMatcher();
    private MovieDbHelper mDbHelper;
    private static final String popularMoviesSelection=
            MovieContract.PopularMovies.TABLE_NAME;

    private static final SQLiteQueryBuilder queryBuilderObj;

    static {
        queryBuilderObj=new SQLiteQueryBuilder();
        queryBuilderObj.setTables(MovieContract.PopularMovies.TABLE_NAME);
    }

    static UriMatcher buildUriMatcher(){

        Log.d("MovieDBProvider","Inside BuildURIMATCHER");
        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authority=MovieContract.CONTENT_AUTHORITY;
        //For each type of URI we need to add a return type

        matcher.addURI(authority,MovieContract.PATH_POPULARMOVIES,POPULAR);
        matcher.addURI(authority,MovieContract.PATH_POPULARMOVIES+"/0",POPULAR);
        return matcher;
    }

    private Cursor getPopularMovies(Uri uri,String[] projection,String sortOrder){


        /*query function usage
        *
        * Cursor dbCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);
        * */


        return queryBuilderObj.query(mDbHelper.getReadableDatabase(),
                null,
                null,
                null,
                null,
                null,
                null);

    }
    static final int POPULAR=100;


    @Override
    public boolean onCreate() {
        mDbHelper=new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case POPULAR:
            {
                retCursor=getPopularMovies(uri,null,null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri from function query ::: " + uri);

        }

        // The usage of this needs to be found out???
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match=sUriMatcher.match(uri);

        switch (match){
            case POPULAR:
                return MovieContract.PopularMovies.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case POPULAR:{
                long _id= db.insert(MovieContract.PopularMovies.TABLE_NAME,null,values);
                if (_id > 0 )
                   returnUri=MovieContract.PopularMovies.buildPopularMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case POPULAR:
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for (ContentValues value: values){
                        long _id=db.insert(MovieContract.PopularMovies.TABLE_NAME,null,value);
                        if(_id != -1){
                            returnCount ++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri,null);
                return returnCount;

            default:
                return super.bulkInsert(uri,values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //1. Create the DB object
        final SQLiteDatabase db=mDbHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int rowsDeleted;

        switch (match){
            case POPULAR:
                rowsDeleted=db.delete(MovieContract.PopularMovies.TABLE_NAME,selection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsDeleted !=0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db=mDbHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int rowsUpdated;
        Uri returnUri;

        switch (match){
            case POPULAR:
                rowsUpdated=db.update(MovieContract.PopularMovies.TABLE_NAME,values,selection,selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    @Override
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }
}

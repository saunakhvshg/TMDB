package com.example.saunakc.tmdb;


import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.saunakc.tmdb.ImageAdapter;
import com.example.saunakc.tmdb.R;
import com.example.saunakc.tmdb.data.MovieContract;

/**
 * Created by saunakc on 13/06/16.
 */
public class PopularMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks{
    private ImageAdapterNew imgAdapter;

    private static final String[] POPULARMOVIES_COLUMNS={
            MovieContract.PopularMovies.TABLE_NAME + "." + MovieContract.PopularMovies._ID,
            MovieContract.PopularMovies.COLUMN_POSTER_PATH,
            MovieContract.PopularMovies.COLUMN_ADULT,
            MovieContract.PopularMovies.COLUMN_OVERVIEW,
            MovieContract.PopularMovies.COLUMN_RELEASE_DATE,
            MovieContract.PopularMovies.COLUMN_POPULARITY,
            MovieContract.PopularMovies.COLUMN_ORIGINAL_TITLE,
            MovieContract.PopularMovies.COLUMN_VIDEO
    };

    static final int COL_INDEX_POPULARMOVIES_ID = 0;
    static final int COL_INDEX_POSTER_PATH = 1;
    static final int COL_INDEX_ADULT = 2;
    static final int COL_INDEX_OVERVIEW = 3;
    static final int COL_INDEX_RELEASE_DATE = 4;
    static final int COL_INDEX_POPULARITY = 5;
    static final int COL_INDEX_ORIGINAL_TITLE = 6;
    static final int COL_INDEX_VIDEO = 7;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(0,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        imgAdapter = new ImageAdapterNew(getActivity(),null,0);

        View view=inflater.inflate(R.layout.fragment_popular_movies,null);
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(imgAdapter);
        return view;


    }


    @Override
    public android.support.v4.content.Loader onCreateLoader(int id, Bundle args) {

        Log.d("PopularMoviesFragment","Inside OnCreateLoader .... !!!");
        Uri popularMovieUri = MovieContract.PopularMovies.buildPopularMoviesUri(id);

        return new android.support.v4.content.CursorLoader(getActivity(),
                popularMovieUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader loader, Object data) {
        if(imgAdapter == null){
            imgAdapter = new ImageAdapterNew(getActivity(),null,0);
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader loader) {
        imgAdapter.swapCursor(null);
    }
}

package com.example.saunakc.tmdb;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.saunakc.tmdb.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by saunakc on 15/06/16.
 */
public class ImageAdapterNew extends CursorAdapter{

    final String LOG_TAG=ImageAdapterNew.class.getName();

    public ImageAdapterNew(Context context, Cursor c, int flags) {
        super(context, c, flags);
        Log.d(LOG_TAG,"Inside Image Adapter New");
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d(LOG_TAG,"Inside new View of Image Adapter New");
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_popular_movies,parent,false);
        bindView(view,context,cursor);
        return view;
    }


    @Override
    public int getCount() {

//        if (getCursor() == null) {
//            return 0;
//        }
//
//        return super.getCount();

        return 20;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d(LOG_TAG,"Inside BindView of ImageAdapterNew");
        ImageView imageView =(ImageView) view.findViewById(R.id.imageView);
        int viewType = getItemViewType(cursor.getPosition());
        Log.d(LOG_TAG,"POSTER PATH: " + cursor.getString(PopularMoviesFragment.COL_INDEX_POSTER_PATH));
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" +
                cursor.getString(PopularMoviesFragment.COL_INDEX_POSTER_PATH)).into(imageView);

    }

    @Override
    public int getItemViewType(int position) {
        Log.d(LOG_TAG,"Inside getItemViewType");
        return super.getItemViewType(position);
    }
}

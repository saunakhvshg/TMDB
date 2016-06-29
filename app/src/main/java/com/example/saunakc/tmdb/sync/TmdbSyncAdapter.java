package com.example.saunakc.tmdb.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.saunakc.tmdb.R;
import com.example.saunakc.tmdb.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by saunakc on 09/06/16.
 */
public class TmdbSyncAdapter extends AbstractThreadedSyncAdapter{
    public static final String LOG_TAG=TmdbSyncAdapter.class.getName();

    public static final int SYNC_INTERVAL = 12;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;


    /*   We now define the projection array.
    *    This projection array has the info which columns to get the data from the local DB.
    *
    * */

    private static final String POPULAR_MOVIES_PROJECTION []=new String[]{
            MovieContract.PopularMovies._ID,
            MovieContract.PopularMovies.COLUMN_POSTER_PATH,
            MovieContract.PopularMovies.COLUMN_ADULT,
            MovieContract.PopularMovies.COLUMN_OVERVIEW,
            MovieContract.PopularMovies.COLUMN_RELEASE_DATE,
            MovieContract.PopularMovies.COLUMN_ORIGINAL_TITLE,
            MovieContract.PopularMovies.COLUMN_POPULARITY,
            MovieContract.PopularMovies.COLUMN_VIDEO
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_POSTER_PATH = 1;
    private static final int INDEX_ADULT = 2;
    private static final int INDEX_OVERVIEW = 3;
    private static final int INDEX_RELEASE_DATE = 4;
    private static final int INDEX_ORIGINAL_TITLE = 5;
    private static final int INDEX_POPULARITY = 6;
    private static final int INDEX_VIDEO = 7;

    public TmdbSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }


    public static void initialiseSyncAdapter(Context context){
        getSyncAccount(context);
    }


    public static void configurePeriodicSync(Context context,int syncInterval,int flexTime){
        // Before I call ContentResolver.requestSync  ... I will need to have the various input params. One of them is getSyncAccount

        Account account=getSyncAccount(context);
        String authority=context.getString(R.string.content_authority);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval,flexTime).
                    setSyncAdapter(account,authority).
                    setExtras(new Bundle()).build();

            Log.d(LOG_TAG,"Request Sync to be called in configure Periodic Sync");
            ContentResolver.requestSync(request);
        }else{
            Log.d(LOG_TAG," Inside else of configurePeriodicSync ########## ");
            ContentResolver.addPeriodicSync(account,authority,new Bundle(),syncInterval);
        }

    }

    public static void syncImmdeiately(Context context){
        Bundle bundle=new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED,true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority),bundle);
    }




    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        Log.d(LOG_TAG,"The name of the account created .... " + newAccount.name);

        // If the password doesn't exist, the account doesn't exist
        if ( null == accountManager.getPassword(newAccount) ) {

            Log.d(LOG_TAG,"##### Password Does not Exist ######");

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true"
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }



    private static void onAccountCreated(Account newAccount,Context context){

        /*Since we have created a new account !!!! */

        Log.d(LOG_TAG,"##### Inside onAccountCreated #####");
        TmdbSyncAdapter.configurePeriodicSync(context,SYNC_INTERVAL,SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount,context.getString(R.string.content_authority),true);
        syncImmdeiately(context);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.d(LOG_TAG,"##### Starting Syncing of Movie DB ##### ");

        String movieApiSetting="popular";

        //We shall be making the Httpurlconnection and bufferedreader object so that we can close this in finally block

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        try{
            String baseMovieReqURL="http://api.themoviedb.org/3/movie/popular?";
            String movieAPIKEY="api_key";
            String movieAPIVal="503f36886ff6cc467d3a00842eb9c4bc";
            Uri movieURIBuilder=Uri.parse(baseMovieReqURL).buildUpon()
                    .appendQueryParameter(movieAPIKEY,movieAPIVal).build();

            URL movieDBURL=new URL(movieURIBuilder.toString());
            Log.d("URL Being called: ",movieDBURL.toString());
            httpURLConnection= (HttpURLConnection) movieDBURL.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream=httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonReceived=new StringBuilder(bufferedReader.readLine());
            String popularMovieJsonString = jsonReceived.toString();
            Log.d(LOG_TAG,"Input received:" + jsonReceived);
            getPopularMoviesFromJson(popularMovieJsonString,1);

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return;
    }


    private void getPopularMoviesFromJson(String popularMovieJsonString,int parsingFlag){


        try{
            if(parsingFlag == 1){
                JSONObject tmdbJSONobj = new JSONObject(popularMovieJsonString);
                JSONArray jArray=tmdbJSONobj.getJSONArray("results");
                Vector<ContentValues> cVVector = new Vector<ContentValues>(jArray.length());

                for(int i=0;i<jArray.length();i++){
                    String movieName;
                    String movieposter_path,movie_adult_category,movie_overview,movie_release_date,movie_popularity,
                            movie_video;
                    JSONObject movieJSONObj=jArray.getJSONObject(i);
                    movieName=movieJSONObj.getString("original_title");
                    movieposter_path=movieJSONObj.getString("poster_path");
                    movie_adult_category=movieJSONObj.getString("adult");
                    movie_overview=movieJSONObj.getString("overview");
                    movie_release_date=movieJSONObj.getString("release_date");
                    movie_popularity=movieJSONObj.getString("popularity");
                    movie_video=movieJSONObj.getString("video");

                    Log.d(LOG_TAG,"About to put values in TmdbProvider ");

                    ContentValues popularMovieValues = new ContentValues();
                    popularMovieValues.put(MovieContract.PopularMovies.COLUMN_ADULT,movie_adult_category);
                    popularMovieValues.put(MovieContract.PopularMovies.COLUMN_ORIGINAL_TITLE,movieName);
                    popularMovieValues.put(MovieContract.PopularMovies.COLUMN_POSTER_PATH,movieposter_path);
                    popularMovieValues.put(MovieContract.PopularMovies.COLUMN_OVERVIEW,movie_overview);
                    popularMovieValues.put(MovieContract.PopularMovies.COLUMN_RELEASE_DATE,movie_release_date);
                    popularMovieValues.put(MovieContract.PopularMovies.COLUMN_POPULARITY,movie_popularity);
                    popularMovieValues.put(MovieContract.PopularMovies.COLUMN_VIDEO,movie_video);

                    cVVector.add(popularMovieValues);
                }

                int inserted = 0;

                if(cVVector.size() > 0){
                    ContentValues[] cvArray =  new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    Log.d(LOG_TAG,"Bulk Inserting Data into TMDB Provider");
                    getContext().getContentResolver().bulkInsert(MovieContract.PopularMovies.CONTENT_URI,cvArray);
                }
                Log.d(LOG_TAG,"sync complete !!! ");

            }

        }catch(JSONException e){
            e.printStackTrace();
        }
    }
}

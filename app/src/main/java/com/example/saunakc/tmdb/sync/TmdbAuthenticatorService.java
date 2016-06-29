package com.example.saunakc.tmdb.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by saunakc on 10/06/16.
 */
public class TmdbAuthenticatorService extends Service {

    private TmdbAuthenticator tmdbAuthenticatorObj;
    @Override
    public void onCreate() {
        tmdbAuthenticatorObj=new TmdbAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return tmdbAuthenticatorObj.getIBinder();
    }
}

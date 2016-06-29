package com.example.saunakc.tmdb.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by saunakc on 10/06/16.
 */
public class TmdbSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    public static TmdbSyncAdapter sTmdbSyncAdpater = null;

    @Override
    public void onCreate() {
        Log.d("TmdbSyncService","onCreate TmdbSyncSerice");
        synchronized (sSyncAdapterLock){
            if(sTmdbSyncAdpater == null){
                sTmdbSyncAdpater=new TmdbSyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sTmdbSyncAdpater.getSyncAdapterBinder();
    }
}

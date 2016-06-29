package com.example.saunakc.tmdb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.saunakc.tmdb.sync.TmdbSyncAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container,new PopularMoviesFragment()).commit();
        TmdbSyncAdapter.initialiseSyncAdapter(getApplicationContext());
    }
}

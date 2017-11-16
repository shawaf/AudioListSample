package com.shawaf.audiolistsample.app;

import android.app.Application;

import com.shawaf.audiolistsample.download.NetworkApiService;

/**
 * Created by mohamedelshawaf on 11/9/17.
 */

public class MainApplication extends Application {

    private NetworkApiService networkService;

    @Override
    public void onCreate() {
        super.onCreate();
        networkService = new NetworkApiService();

    }

    public NetworkApiService getNetworkService() {
        return networkService;
    }
}

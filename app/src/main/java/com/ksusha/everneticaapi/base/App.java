package com.ksusha.everneticaapi.base;

import android.app.Application;

import com.ksusha.everneticaapi.feature.trending.api.service.UnsplashService;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class App extends Application {

    private UnsplashService unsplashService;


    @Override
    public void onCreate() {
        super.onCreate();

        unsplashService = new UnsplashService();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)

                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                .memoryCacheSize(20 * 1024 * 1024)
                .build();

        ImageLoader.getInstance().init(config);
    }

    public UnsplashService getUnsplashService() {
        return unsplashService;
    }










}

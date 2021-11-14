package com.ksusha.everneticaapi.feature.trending.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {

    private static Retrofit retrofit = null;
    public static final String BASE_URL = "https://api.unsplash.com";
    public static final String API = "gb3YR6aEBAL7QTnk4r9lgGm-k1aFq9Z-iwpkx6X4XEE";

    public static ApiIntephase getApiInterphase() {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory
                    .create())
                    .build();
        }
        return retrofit.create(ApiIntephase.class);
    }
}

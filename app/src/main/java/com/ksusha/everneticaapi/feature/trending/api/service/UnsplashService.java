package com.ksusha.everneticaapi.feature.trending.api.service;

import com.ksusha.everneticaapi.feature.trending.api.ApiIntephase;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UnsplashService {
    public static String KEY_API = "gb3YR6aEBAL7QTnk4r9lgGm-k1aFq9Z-iwpkx6X4XEE";



    ApiIntephase apiIntephase;

    public UnsplashService() {
        Retrofit retrofit = createRetrofit();
        apiIntephase = retrofit.create(ApiIntephase.class);
    }

public ApiIntephase getApiIntephase(){
        return apiIntephase;
}

    private Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.unsplash.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient createOkHttpClient() {
        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", KEY_API)
                        .build();
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logging);

        return httpClient.build();
    }

}

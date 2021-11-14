package com.ksusha.everneticaapi.feature.trending.api;

import com.ksusha.everneticaapi.feature.model.ImageModel;
import com.ksusha.everneticaapi.feature.model.SearchModel;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

import static com.ksusha.everneticaapi.feature.trending.api.ApiUtilities.API;

public interface ApiIntephase {

    @Headers("Authorization: Client-ID " + API)
    @GET("/photos")
    Single<List<ImageModel>> getImages(//!!!
             @Query("page") int page,
             @Query("per_page") int per_page
    );

    @Headers("Authorization: Client-ID " + API)
    @GET("/search/photos")
    Single<SearchModel> searchImages(//!!!
            @Query("query") String query,
            @Query("per_page") int per_page
    );

}

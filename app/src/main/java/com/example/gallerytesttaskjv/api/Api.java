package com.example.gallerytesttaskjv.api;


import com.example.gallerytesttaskjv.model.Image;
import com.example.gallerytesttaskjv.model.Search;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface Api {
    @Headers("Authorization: Client-ID " + ApiUtilities.ACCESS_KEY)
    @GET("/photos")
    Call<List<Image>> getImages(
            @Query("page") int page,
            @Query("per_page") int perPage
    );


    @Headers("Authorization: Client-ID " + ApiUtilities.ACCESS_KEY)
    @GET("/search/photos")
    Call<Search> searchImage(
            @Query("query") String query
    );
}

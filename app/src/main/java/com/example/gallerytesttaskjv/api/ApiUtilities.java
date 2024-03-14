package com.example.gallerytesttaskjv.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiUtilities {
    public static final String BASE_URL = "https://api.unsplash.com";
    public static final String ACCESS_KEY = "HQ-X2jhl8aIfS_b-jq6TavR5T4Blwg2sH_LnUYaPuYU";
    public static Retrofit retrofit = null;


    public static Api getApiUtility() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(Api.class);
    }
}

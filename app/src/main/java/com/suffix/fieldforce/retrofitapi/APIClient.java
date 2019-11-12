package com.suffix.fieldforce.retrofitapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit;

    public static Retrofit getApiClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl("http://182.16.159.22:8352/UPTS/api/").addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}

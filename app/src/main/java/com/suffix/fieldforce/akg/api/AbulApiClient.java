package com.suffix.fieldforce.akg.api;

import com.suffix.fieldforce.BuildConfig;
import com.suffix.fieldforce.networking.Client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AbulApiClient {
  private static Retrofit retrofit;

  public static Retrofit getApiClient() {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
          .baseUrl(BuildConfig.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .client(Client.INSTANCE.getOkHttpClient())
          .build();
    }
    return retrofit;
  }
}

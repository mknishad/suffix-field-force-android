package com.suffix.fieldforce.networking

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.suffix.fieldforce.BuildConfig
import com.suffix.fieldforce.model.LocationResponse
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .writeTimeout(60, TimeUnit.SECONDS)
    .retryOnConnectionFailure(true)
    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BuildConfig.BASE_URL)
    .client(okHttpClient)
    .build()

interface FieldForceApiService {
    @POST("FFMS/api//geoLocationEntry.jsp")
    fun sendGeoLocationAsync(
        @Query("key") key: String,
        @Query("userId") userId: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String
    ):
            Deferred<LocationResponse>
}

object FieldForceApi {
    val retrofitService: FieldForceApiService by lazy {
        retrofit.create(FieldForceApiService::class.java)
    }
}
package com.suffix.fieldforce.networking

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.suffix.fieldforce.BuildConfig
import com.suffix.fieldforce.model.*
import com.suffix.fieldforce.networking.Client.okHttpClient
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

enum class FieldForceApiStatus { LOADING, ERROR, DONE }

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
object Client {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BuildConfig.BASE_URL)
    .client(okHttpClient)
    .build()

object FieldForceApi {
    val retrofitService: FieldForceApiService by lazy {
        retrofit.create(FieldForceApiService::class.java)
    }
}

interface FieldForceApiService {

    @POST("FFMS/api/geoLocationEntry.jsp")
    fun sendGeoLocationAsync(
        @Query("key") key: String,
        @Query("userId") userId: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String
    ): Deferred<LocationResponse>

    @POST("FFMS/api/getBillType.jsp")
    fun getBillTypeAsync(
        @Query("key") key: String,
        @Query("userId") userId: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String
    ): Deferred<BillTypeResponse>

    @POST("FFMS/api/billEntry.jsp")
    fun addBillAsync(
        @Query("key") key: String,
        @Query("userId") userId: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("billData") billData: String
    ): Deferred<AddBillResponse>

    @POST("FFMS/api/getUserBillList.jsp")
    fun getBillListAsync(
        @Query("key") key: String,
        @Query("userId") userId: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String
    ): Deferred<BillDashboardResponse>

    @POST("FFMS/api/getUserBillDetails.jsp")
    fun getBillDetailsAsync(
        @Query("key") key: String,
        @Query("userId") userId: String,
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("billId") billId: String
    ): Deferred<BillDetailsResponse>

    @POST("FFMS/api/technicianSummaryInfo.jsp")
    fun getTaskDashboardAsync(
        @Query("regTechId") regTechId: String
    ): Deferred<List<TaskDashboardResponse>>

    @POST("FFMS/api/assignTicketList.jsp")
    fun getAssignedTaskListAsync(
        @Query("regTechId") regTechId: String
    ): Deferred<List<TaskListResponse>>

    @POST("FFMS/api/acceptedTicketList.jsp")
    fun getAcceptedTaskListAsync(
        @Query("regTechId") regTechId: String
    ): Deferred<List<TaskListResponse>>

    @POST("FFMS/api/completedTicketList.jsp")
    fun getCompletedTaskListAsync(
        @Query("regTechId") regTechId: String
    ): Deferred<List<TaskListResponse>>

    @POST("FFMS/api/inprogressTicketList.jsp")
    fun getInProgressTaskListAsync(
        @Query("regTechId") regTechId: String
    ): Deferred<List<TaskListResponse>>

    @POST("FFMS/api/tickeDetailsInfo.jsp")
    fun getTaskDetailsAsync(
        @Query("regTechId") regTechId: String,
        @Query("ticketId") ticketId: String
    ): Deferred<List<TaskDetailsResponse>>
}

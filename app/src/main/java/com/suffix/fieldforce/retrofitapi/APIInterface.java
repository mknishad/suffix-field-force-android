package com.suffix.fieldforce.retrofitapi;

import com.suffix.fieldforce.model.AssignedTask;
import com.suffix.fieldforce.model.LocationResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @FormUrlEncoded
    @POST("FFMS/api/assignTicketList.jsp")
    Call<List<AssignedTask>> getAssignTicketList(@Field("regTechId") String regTechId);

    @POST("FFMS/api/geoLocationEntry.jsp")
    Call<LocationResponse> sendGeoLocation(
            @Query("key") String key,
            @Query("userId") String userId,
            @Query("lat") String lat,
            @Query("lng") String lng
    );
}

package com.suffix.fieldforce.retrofitapi;

import com.suffix.fieldforce.model.AssignedTask;
import com.suffix.fieldforce.model.LocationResponse;
import com.suffix.fieldforce.model.LoginResponse;
import com.suffix.fieldforce.model.SendPushTokenResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("FFMS/api/assignTicketList.jsp")
    Call<List<AssignedTask>> getAssignTicketList(@Query("regTechId") String regTechId);

    @POST("FFMS/api/tickeDetailsInfo.jsp")
    Call<List<AssignedTask>> getTickeDetailsInfo(
            @Query("regTechId") String regTechId,
            @Query("ticketId") String ticketId
    );

    @POST("FFMS/api/taskEntry.jsp")
    Call<List<AssignedTask>> taskEntry(
            @Query("key") String key,
            @Query("userId") String userId,
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("categoryId") String categoryId,
            @Query("ticketTitle") String ticketTitle,
            @Query("priority") String priority,
            @Query("imageString") String imageString,
            @Query("ticketDetails") String ticketDetails,
            @Query("startDate") String startDate,
            @Query("endDate") String endDate,
            @Query("districtId") String districtId,
            @Query("thanaId") String thanaId,
            @Query("address") String address,
            @Query("consumerName") String consumerName,
            @Query("consumerMobileNumber") String consumerMobileNumber
    );

    @POST("FFMS/api/ticketOpenInfo.jsp")
    Call<List<AssignedTask>> ticketOpenInfo(
            @Query("regTechId") String regTechId,
            @Query("ticketId") String ticketId,
            @Query("regTicketComments") String regTicketComments
    );

    @POST("FFMS/api/ticketInprogressInfo.jsp")
    Call<List<AssignedTask>> ticketInprogressInfo(
            @Query("regTechId") String regTechId,
            @Query("ticketId") String ticketId,
            @Query("regTicketComments") String regTicketComments
    );

    @POST("FFMS/api/ticketCloseInfo.jsp")
    Call<List<AssignedTask>> ticketCloseInfo(
            @Query("regTechId") String regTechId,
            @Query("ticketId") String ticketId,
            @Query("regTicketComments") String regTicketComments
    );

    @POST("FFMS/api/geoLocationEntry.jsp")
    Call<LocationResponse> sendGeoLocation(
            @Query("key") String key,
            @Query("userId") String userId,
            @Query("lat") String lat,
            @Query("lng") String lng
    );

    @POST("FFMS/api/login.jsp")
    Call<LoginResponse> login(
            @Query("key") String key,
            @Query("userId") String userId,
            @Query("password") String password,
            @Query("deviceId") String deviceId
    );

    @POST("FFMS/api/userDeviceInfoSet.jsp")
    Call<SendPushTokenResponse> sendPushToken(
            @Query("key") String key,
            @Query("userId") String userId,
            @Query("deviceId") String deviceId
    );
}

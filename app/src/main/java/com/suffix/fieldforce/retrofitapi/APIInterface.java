package com.suffix.fieldforce.retrofitapi;

import com.suffix.fieldforce.model.AssignedTask;
import com.suffix.fieldforce.model.ChatGroupMemberDataObj;
import com.suffix.fieldforce.model.DistrictData;
import com.suffix.fieldforce.model.LocationResponse;
import com.suffix.fieldforce.model.LoginResponse;
import com.suffix.fieldforce.model.ModelGroupChat;
import com.suffix.fieldforce.model.ModelUser;
import com.suffix.fieldforce.model.Project;
import com.suffix.fieldforce.model.RosterScheduleModel;
import com.suffix.fieldforce.model.SendPushTokenResponse;
import com.suffix.fieldforce.model.TaskEntry;
import com.suffix.fieldforce.model.Ticketstatus;

import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("FFMS/api/attendanceEntry.jsp")
    Call<LocationResponse> attendanceEntry(
        @Query("key") String key,
        @Query("userId") String userId,
        @Query("lat") String lat,
        @Query("lng") String lng,
        @Query("entryType") String entryType
    );

    @POST("FFMS/api/geoLocationEntry.jsp")
    Call<LocationResponse> sendGeoLocationAsync(
        @Query("key") String key,
        @Query("userId") String userId,
        @Query("lat") String lat,
        @Query("lng") String lng
    );

    @POST("FFMS/api/getDistrictThanaInfo.jsp")
    Call<List<DistrictData>> getDistrictThanaInfo();

    @POST("FFMS/api/assignTicketList.jsp")
    Call<List<AssignedTask>> assignTicketList(
            @Query("regTechId") String regTechId
    );

    @POST("FFMS/api/acceptedTicketList.jsp")
    Call<List<AssignedTask>> acceptedTicketList(
            @Query("regTechId") String regTechId
    );

    @POST("FFMS/api/completedTicketList.jsp")
    Call<List<AssignedTask>> completedTicketList(
            @Query("regTechId") String regTechId
    );

    @POST("FFMS/api/inprogressTicketList.jsp")
    Call<List<AssignedTask>> inprogressTicketList(
            @Query("regTechId") String regTechId
    );

    @POST("FFMS/api/ticketOpenInfo.jsp")
    Call<List<Ticketstatus>> ticketOpenInfo(
            @Query("regTechId") String regTechId,
            @Query("ticketId") String ticketId,
            @Query("regTicketComments") String regTicketComments,
            @Query("lat") String lat,
            @Query("lng") String lng
    );

    @POST("FFMS/api/ticketInprogressInfo.jsp")
    Call<List<Ticketstatus>> ticketInprogressInfo(
            @Query("regTechId") String regTechId,
            @Query("ticketId") String ticketId,
            @Query("regTicketComments") String regTicketComments,
            @Query("lat") String lat,
            @Query("lng") String lng
    );

    @POST("FFMS/api/ticketCloseInfo.jsp")
    Call<List<Ticketstatus>> ticketCloseInfo(
            @Query("regTechId") String regTechId,
            @Query("ticketId") String ticketId,
            @Query("regTicketComments") String regTicketComments,
            @Query("lat") String lat,
            @Query("lng") String lng
    );

    @POST("FFMS/api/tickeDetailsInfo.jsp")
    Call<List<AssignedTask>> getTickeDetailsInfo(
            @Query("regTechId") String regTechId,
            @Query("ticketId") String ticketId
    );

    @FormUrlEncoded
    @POST("FFMS/api/taskEntry.jsp")
    Call<TaskEntry> taskEntry(
            @Field("key") String key,
            @Field("userId") String userId,
            @Field("lat") String lat,
            @Field("lng") String lng,
            @Field("categoryId") String categoryId,
            @Field("ticketTitle") String ticketTitle,
            @Field("priority") String priority,
            @Field("imageString") String imageString,
            @Field("ticketDetails") String ticketDetails,
            @Field("startDate") String startDate,
            @Field("endDate") String endDate,
            @Field("districtId") String districtId,
            @Field("thanaId") String thanaId,
            @Field("address") String address,
            @Field("consumerName") String consumerName,
            @Field("consumerMobileNumber") String consumerMobileNumber
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

    @POST("FFMS/api/getUserRosterSchedule.jsp")
    Call<RosterScheduleModel> getUserRosterSchedule(
        @Query("key") String key,
        @Query("userId") String userId,
        @Query("lat") String lat,
        @Query("lng") String lng,
        @Query("year") String year,
        @Query("month") String month
    );

    @FormUrlEncoded
    @POST("FFMS/api/getUserList.jsp")
    Call<ModelUser> getUserList(
        @Field("key") String key,
        @Field("userId") String userId,
        @Field("lat") String lat,
        @Field("lng") String lng
    );

    @FormUrlEncoded
    @POST("FFMS/api/getChatGroupList.jsp")
    Call<ModelGroupChat> getChatGroupList(
        @Field("key") String key,
        @Field("userId") String userId,
        @Field("lat") String lat,
        @Field("lng") String lng
    );

    @FormUrlEncoded
    @POST("FFMS/api/addChatGroup.jsp")
    Call<ResponseBody> addChatGroup(
        @Field("key") String key,
        @Field("userId") String userId,
        @Field("lat") String lat,
        @Field("lng") String lng,
        @Field("chatGroupMemberData") String chatGroupMemberDataObj,
        @Field("chatGroupName") String chatGroupName
    );

    @FormUrlEncoded
    @POST("FFMS/api/getProjectList.jsp")
    Call<Project> getProjectList(
        @Field("key") String key,
        @Field("userId") String userId,
        @Field("lat") String lat,
        @Field("lng") String lng
    );

    @FormUrlEncoded
    @POST("FFMS/api/postTransportRequisition.jsp")
    Call<ResponseBody> postTransportRequisition(
        @Field("key") String key,
        @Field("userId") String userId,
        @Field("lat") String lat,
        @Field("lng") String lng,
        @Field("projectId") String projectId,
        @Field("regMaintenance") String regMaintenance,
        @Field("survey") String survey,
        @Field("clientConnectivity") String clientConnectivity,
        @Field("implementation") String implementation,
        @Field("startTime") String startTime,
        @Field("endTime") String endTime,
        @Field("remark") String remark
    );
}

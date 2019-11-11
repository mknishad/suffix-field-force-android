package com.suffix.fieldforce.retrofitapi;

import com.suffix.fieldforce.model.AssignedTask;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("assignTicketList.jsp")
    Call<List<AssignedTask>> getAssignTicketList(@Field("regTechId") String regTechId);
}

package com.suffix.fieldforce.retrofitapi;

import com.suffix.fieldforce.model.AssignedTask;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("FFMS/api/assignTicketList.jsp")
    Call<AssignedTask> getAssignTicketList(@Field("regTechId") String regTechId);
}

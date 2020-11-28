package com.suffix.fieldforce.abul.api;

import com.suffix.fieldforce.abul.model.AbulLoginResponse;
import com.suffix.fieldforce.abul.model.LoginRequest;
import com.suffix.fieldforce.abul.model.MemoListResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AbulApiInterface {
  @POST("login")
  Call<AbulLoginResponse> login(
      @Body LoginRequest loginRequest
  );

  @GET("invoice/{salesRepId}")
  Call<List<MemoListResponse>> getMemoList(
      @Path("salesRepId") String salesRepId
  );

}

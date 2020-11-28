package com.suffix.fieldforce.akg.api;

import com.suffix.fieldforce.akg.model.AbulLoginResponse;
import com.suffix.fieldforce.akg.model.LoginRequest;
import com.suffix.fieldforce.akg.model.MemoListResponse;

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

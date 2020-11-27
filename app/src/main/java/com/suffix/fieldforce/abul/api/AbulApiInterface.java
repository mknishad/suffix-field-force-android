package com.suffix.fieldforce.abul.api;

import com.suffix.fieldforce.abul.model.AbulLoginResponse;
import com.suffix.fieldforce.abul.model.LoginRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AbulApiInterface {
  @POST("login")
  Call<AbulLoginResponse> login(
      @Body LoginRequest loginRequest
  );
}

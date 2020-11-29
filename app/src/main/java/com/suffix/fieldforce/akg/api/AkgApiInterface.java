package com.suffix.fieldforce.akg.api;

import com.suffix.fieldforce.akg.model.AbulLoginResponse;
import com.suffix.fieldforce.akg.model.AttendenceRequest;
import com.suffix.fieldforce.akg.model.InvoiceDetail;
import com.suffix.fieldforce.akg.model.LoginRequest;
import com.suffix.fieldforce.akg.model.MemoListResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AkgApiInterface {
  @POST("login")
  Call<AbulLoginResponse> login(
      @Body LoginRequest loginRequest
  );

  @POST("sr-attendance")
  Call<ResponseBody> attendanceEntry(
      @Header("Authorization") String h1,
      @Body AttendenceRequest attendenceRequest
  );

  @GET("invoice/{salesRepId}")
  Call<List<MemoListResponse>> getMemoList(
      @Header("Authorization") String h1,
      @Path("salesRepId") int salesRepId
  );

  @GET("invoice-details/{invoiceId}")
  Call<List<InvoiceDetail>> getMemoDetails(
      @Header("Authorization") String h1,
      @Path("invoiceId") int invoiceId
  );
}

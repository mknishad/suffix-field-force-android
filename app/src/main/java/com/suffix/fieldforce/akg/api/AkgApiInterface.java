package com.suffix.fieldforce.akg.api;

import com.suffix.fieldforce.akg.model.ActiveCustomerRequest;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.AttendenceRequest;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.Distributor;
import com.suffix.fieldforce.akg.model.GeoLocationRequest;
import com.suffix.fieldforce.akg.model.GiftInvoiceRequest;
import com.suffix.fieldforce.akg.model.InvoiceDetail;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.model.LoginRequest;
import com.suffix.fieldforce.akg.model.MemoListResponse;
import com.suffix.fieldforce.akg.model.Slider;
import com.suffix.fieldforce.akg.model.StoreVisitRequest;
import com.suffix.fieldforce.akg.model.product.GiftModel;
import com.suffix.fieldforce.akg.model.product.ProductCategory;

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
  Call<AkgLoginResponse> login(
      @Body LoginRequest loginRequest
  );

  @POST("sr-attendance")
  Call<ResponseBody> attendanceEntry(
      @Header("Authorization") String h1,
      @Body AttendenceRequest attendenceRequest
  );

  @POST("invoice")
  Call<ResponseBody> createInvoice(
      @Header("Authorization") String h1,
      @Body InvoiceRequest invoiceRequest
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

  @GET("distributor-by-sr/{salesRepId}")
  Call<Distributor> getDistributor(
      @Header("Authorization") String h1,
      @Path("salesRepId") int salesRepId
  );

  @GET("product/{salesRepId}")
  Call<ProductCategory> getAllProduct(
      @Header("Authorization") String h1,
      @Path("salesRepId") int salesRepId
  );

  @GET("sr-customers/{srId}/{status}")
  Call<List<CustomerData>> getAllCustomer(
      @Header("Authorization") String h1,
      @Path("srId") int salesRepId,
      @Path("status") int status
  );

  @POST("active-customer")
  Call<ResponseBody> activeCustomer(
      @Header("Authorization") String h1,
      @Body ActiveCustomerRequest activeCustomerRequest
  );

  @POST("slider")
  Call<ResponseBody> collectSlider(
      @Header("Authorization") String h1,
      @Body Slider slider
  );

  @POST("visit-store")
  Call<ResponseBody> visitStore(
      @Header("Authorization") String h1,
      @Body StoreVisitRequest storeVisitRequest
  );

  @POST("geolocation")
  Call<ResponseBody> postGeoLocation(
      @Header("Authorization") String h1,
      @Body GeoLocationRequest geoLocationRequest
  );

  @GET("gift/{salesRepId}")
  Call<List<GiftModel>> getAllGifts(
      @Header("Authorization") String h1,
      @Path("salesRepId") int salesRepId
  );

  @POST("gift")
  Call<ResponseBody> createGiftInvoice(
      @Header("Authorization") String h1,
      @Body GiftInvoiceRequest giftInvoiceRequest
  );
}

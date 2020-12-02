package com.suffix.fieldforce.akg.database;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.activity.SaleActivity;
import com.suffix.fieldforce.akg.adapter.CustomArrayAdapter;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.product.ProductCategory;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.List;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DatabseManager {

  private Context context;
  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private AkgLoginResponse loginResponse;
  private String basicAuthorization;

  public DatabseManager(Context context) {
    this.context = context;
    preferences = new FieldForcePreferences(context);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);
    loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());
  }

  private void getAllCustomer() {
    Call<List<CustomerData>> call = apiInterface.getAllCustomer(basicAuthorization, loginResponse.getData().getId(), 1);
    call.enqueue(new Callback<List<CustomerData>>() {
      @Override
      public void onResponse(Call<List<CustomerData>> call, Response<List<CustomerData>> response) {
        if (response.isSuccessful()) {

        } else {
          System.out.println("error");
        }
      }

      @Override
      public void onFailure(Call<List<CustomerData>> call, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });
  }

  private void getAllCategory() {
    Call<ProductCategory> call = apiInterface.getAllProduct(basicAuthorization, loginResponse.getData().getId());
    call.enqueue(new Callback<ProductCategory>() {
      @Override
      public void onResponse(Call<ProductCategory> call, Response<ProductCategory> response) {
        if (response.isSuccessful()) {

        }
      }

      @Override
      public void onFailure(Call<ProductCategory> call, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });
  }

}

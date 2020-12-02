package com.suffix.fieldforce.akg.database.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.model.RealMCustomer;
import com.suffix.fieldforce.akg.database.model.RealMInvoice;
import com.suffix.fieldforce.akg.database.model.RealMProductCategory;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.model.product.CategoryModel;
import com.suffix.fieldforce.akg.model.product.ProductCategory;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.List;

import io.realm.Realm;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncManager {

  private Context context;
  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private AkgLoginResponse loginResponse;
  private String basicAuthorization;
  private Realm realm;

  public SyncManager(Context context) {
    this.context = context;
    realm = Realm.getDefaultInstance();
    preferences = new FieldForcePreferences(context);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);
    loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());
  }

  public void getAllCustomer() {
    Call<List<CustomerData>> call = apiInterface.getAllCustomer(basicAuthorization, loginResponse.getData().getId(), 1);
    call.enqueue(new Callback<List<CustomerData>>() {
      @Override
      public void onResponse(Call<List<CustomerData>> call, Response<List<CustomerData>> response) {
        if (response.isSuccessful()) {

          List<CustomerData> customerDataList = response.body();

          realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
              RealMCustomer realMCustomer = bgRealm.createObject(RealMCustomer.class);
              for (CustomerData customerData : customerDataList) {
                realMCustomer.getCustomerData().add(customerData);
              }
            }
          }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
              //Toast.makeText(context, "All Customer Synced", Toast.LENGTH_SHORT).show();
              getAllCategory();
            }
          });
        } else {
          Toast.makeText(context, "Connection is not successfull", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<List<CustomerData>> call, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });
  }

  //RealMProductCategory
  public void getAllCategory() {
    Call<ProductCategory> call = apiInterface.getAllProduct(basicAuthorization, loginResponse.getData().getId());
    call.enqueue(new Callback<ProductCategory>() {
      @Override
      public void onResponse(Call<ProductCategory> call, Response<ProductCategory> response) {
        if (response.isSuccessful()) {
          //Get All Category From Server
          ProductCategory productCategory = response.body();

          realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {

              RealMProductCategory realMProductCategory = bgRealm.createObject(RealMProductCategory.class);
              //Store all cigrette list
              for (CategoryModel cigretteeModel : productCategory.getCigrettee()) {
                realMProductCategory.getCigrettee().add(cigretteeModel);
              }
              //Store all bidi list
              for (CategoryModel bidiModel : productCategory.getBidi()) {
                realMProductCategory.getBidi().add(bidiModel);
              }
              //Store all match list
              for (CategoryModel matchModel : productCategory.getMatch()) {
                realMProductCategory.getCigrettee().add(matchModel);
              }
            }
          }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
              Toast.makeText(context, "All DaTa Synced", Toast.LENGTH_SHORT).show();
            }
          });

        }
      }

      @Override
      public void onFailure(Call<ProductCategory> call, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });
  }

  public void insertInvoice(InvoiceRequest invoiceRequest){

    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(Realm bgRealm) {
        //RealMInvoice realMInvoice = bgRealm.createObject(RealMInvoice.class);
        //realMInvoice.getRequestRealmList().add(invoiceRequest);
        bgRealm.copyToRealm(invoiceRequest);
      }
    }, new Realm.Transaction.OnSuccess() {
      @Override
      public void onSuccess() {
        Toast.makeText(context, "Invoice Added", Toast.LENGTH_SHORT).show();
      }
    });

  }

}

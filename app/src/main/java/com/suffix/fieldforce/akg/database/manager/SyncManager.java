package com.suffix.fieldforce.akg.database.manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.RealmDatabseManagerInterface;
import com.suffix.fieldforce.akg.database.model.RealMProductCategory;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.InvoiceProduct;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.model.product.CategoryModel;
import com.suffix.fieldforce.akg.model.product.ProductCategory;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
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

  public void getAllCustomer(RealmDatabseManagerInterface.Sync interfaceSync) {
    Call<List<CustomerData>> call = apiInterface.getAllCustomer(basicAuthorization, loginResponse.getData().getId(), 1);
    call.enqueue(new Callback<List<CustomerData>>() {
      @Override
      public void onResponse(Call<List<CustomerData>> call, Response<List<CustomerData>> response) {
        if (response.isSuccessful()) {

          List<CustomerData> customerDataList = response.body();

          realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
              //RealMCustomer realMCustomer = new RealMCustomer();
              for (CustomerData customerData : customerDataList) {
                bgRealm.copyToRealmOrUpdate(customerData);
              }
            }
          }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
              //Toast.makeText(context, "All Customer Synced", Toast.LENGTH_SHORT).show();
              getAllCategory(interfaceSync);
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
  public void getAllCategory(RealmDatabseManagerInterface.Sync interfaceSync) {
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
              for (CategoryModel cigretteeModel : productCategory.getCigarette()) {
                realMProductCategory.getCigrettee().add(cigretteeModel);
              }
              //Store all bidi list
              for (CategoryModel bidiModel : productCategory.getBidi()) {
                realMProductCategory.getBidi().add(bidiModel);
              }
              //Store all match list
              for (CategoryModel matchModel : productCategory.getMatch()) {
                realMProductCategory.getMatch().add(matchModel);
              }
            }
          }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
              Toast.makeText(context, "ডাটা হালনাগাদ হয়েছে", Toast.LENGTH_SHORT).show();
              if (interfaceSync != null) {
                interfaceSync.onSuccess();
              }
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

  public void insertInvoice(InvoiceRequest invoiceRequest) {

    try {
      updateCategoryProduct(invoiceRequest.getInvoiceProducts());
    } catch (Exception e) {
      e.printStackTrace();
    }


    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(Realm bgRealm) {
        try {
          bgRealm.copyToRealm(invoiceRequest);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }, new Realm.Transaction.OnSuccess() {
      @Override
      public void onSuccess() {
        Toast.makeText(context, "Invoice Added", Toast.LENGTH_SHORT).show();
      }
    });

  }

  public void updateCategoryProduct(RealmList<InvoiceProduct> invoiceProducts) {
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {

        for (int i = 0; i < invoiceProducts.size(); i++) {
          CategoryModel categoryModel = realm.where(CategoryModel.class).equalTo("productId", invoiceProducts.get(i).getProductId()).findFirst();
          if (categoryModel != null) {
            categoryModel.setSalesQty(categoryModel.getSalesQty() + invoiceProducts.get(i).getProductQty());
          }
        }
      }
    });

  }

  public void updateSingleInvoice(String invoiceId, InvoiceProduct invoiceProduct, int updateQty) {
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        CategoryModel categoryModel = realm.where(CategoryModel.class).equalTo("productId",
            invoiceProduct.getProductId()).findFirst();
        if (categoryModel != null) {
          categoryModel.setSalesQty(categoryModel.getSalesQty() + updateQty);
        }

        InvoiceRequest invoiceRequest = realm.where(InvoiceRequest.class).equalTo("invoiceId",
            invoiceId).findFirst();

        if (invoiceRequest != null) {
          for (InvoiceProduct product : invoiceRequest.getInvoiceProducts()) {
            if (product.getProductId() == invoiceProduct.getProductId()) {
              product.setProductQty(product.getProductQty() + updateQty);
            }
          }
        }
      }
    });
  }

  public void updateInvoiceRequest(InvoiceRequest invoiceRequest, double amount,
                                   RealmDatabseManagerInterface.Sync sync) {

    Log.d("updateInvoiceRequest: ", amount + "");

    realm.executeTransactionAsync(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        InvoiceRequest ir = realm.where(InvoiceRequest.class).equalTo("invoiceId",
            invoiceRequest.getInvoiceId()).findFirst();
        if (ir != null) {
          ir.setRecievedAmount(ir.getRecievedAmount() + amount);
        }
      }
    }, new Realm.Transaction.OnSuccess() {
      @Override
      public void onSuccess() {
        if (sync != null) {
          sync.onSuccess();
        }
      }
    });

  }

}

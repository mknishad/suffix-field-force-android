package com.suffix.fieldforce.akg.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.CustomArrayAdapter;
import com.suffix.fieldforce.akg.adapter.MemoListAdapter;
import com.suffix.fieldforce.akg.adapter.ProductCategoryListAdapter;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.model.AbulLoginResponse;
import com.suffix.fieldforce.akg.model.StoreModel;
import com.suffix.fieldforce.akg.model.product.CategoryModel;
import com.suffix.fieldforce.akg.model.product.ProductCategory;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleActivity extends AppCompatActivity {

  @BindView(R.id.recyclerViewCigrettee)
  RecyclerView recyclerViewCigrettee;

  @BindView(R.id.spinnerUsers)
  Spinner spinnerUsers;

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private ArrayAdapter<StoreModel> spinnerAdapter;
  private ProductCategoryListAdapter productCategoryListAdapter;
  private List<CategoryModel> categoryModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sale);
    ButterKnife.bind(this);

    categoryModel = new ArrayList<>();

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    spinnerAdapter = new CustomArrayAdapter(this, R.layout.spinner_item, getList());
    spinnerUsers.setAdapter(spinnerAdapter);

    GridLayoutManager manager = new GridLayoutManager(this, 2);
    recyclerViewCigrettee.setLayoutManager(manager);
    productCategoryListAdapter = new ProductCategoryListAdapter(this, categoryModel);
    recyclerViewCigrettee.setAdapter(productCategoryListAdapter);

    getAllCategory();

  }

  private void getAllCategory() {

    AbulLoginResponse loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AbulLoginResponse.class);
    String basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());

    Call<ProductCategory> call = apiInterface.getAllProduct(basicAuthorization);
    call.enqueue(new Callback<ProductCategory>() {
      @Override
      public void onResponse(Call<ProductCategory> call, Response<ProductCategory> response) {
        if (response.isSuccessful()) {
          ProductCategory productCategory = response.body();
          productCategoryListAdapter.setData(productCategory.getCategoryModel());
        }
      }

      @Override
      public void onFailure(Call<ProductCategory> call, Throwable t) {
        Toast.makeText(SaleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });

  }

  private List<StoreModel> getList() {

    List<StoreModel> myList = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      myList.add(new StoreModel("Solaiman", "Badda, Dhaka"));
    }
    return myList;
  }
}
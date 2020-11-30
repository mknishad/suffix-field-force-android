package com.suffix.fieldforce.akg.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.CustomArrayAdapter;
import com.suffix.fieldforce.akg.adapter.ProductCategoryListAdapter;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.product.ProductCategory;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleActivity extends AppCompatActivity {

  private static final String TAG = "SaleActivity";

  /*@BindView(R.id.layoutKeys)
  LinearLayout layoutKeys;

  @BindView(R.id.toggleGroupOne)
  MaterialButtonToggleGroup toggleGroupOne;

  @BindView(R.id.toggleGroupTwo)
  MaterialButtonToggleGroup toggleGroupTwo;

  @BindView(R.id.toggleGroupThree)
  MaterialButtonToggleGroup toggleGroupThree;

  @BindView(R.id.toggleGroupFour)
  MaterialButtonToggleGroup toggleGroupFour;*/

  @BindView(R.id.imgDropArrow)
  ImageView imgDropArrow;

  @BindView(R.id.recyclerViewCigrettee)
  RecyclerView recyclerViewCigrettee;

  @BindView(R.id.recyclerViewBidi)
  RecyclerView recyclerViewBidi;

  @BindView(R.id.recyclerViewMatch)
  RecyclerView recyclerViewMatch;

  @BindView(R.id.spinnerUsers)
  Spinner spinnerUsers;

  @OnClick(R.id.imgDropArrow)
  public void toggleKeyboard() {
    spinnerUsers.performClick();
  }

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private ArrayAdapter<CustomerData> spinnerAdapter;
  private ProductCategoryListAdapter cigretteListAdapter, bidiListAdapter, matchListAdapter;
  private AkgLoginResponse loginResponse;
  private String basicAuthorization;

  private List<CustomerData> customerDataList;
  private ProductCategory productCategory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sale);
    ButterKnife.bind(this);

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    customerDataList = new ArrayList<>();
    productCategory = new ProductCategory();

    loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());

    manageRecyclerView();
    getAllCustomer();
    getAllCategory();
    //setupToggleButtons();
  }

  private void manageRecyclerView() {
    recyclerViewCigrettee.setLayoutManager(getLayoutManager());
    cigretteListAdapter = new ProductCategoryListAdapter(this, new ArrayList<>());
    recyclerViewCigrettee.setAdapter(cigretteListAdapter);

    recyclerViewBidi.setLayoutManager(getLayoutManager());
    bidiListAdapter = new ProductCategoryListAdapter(this, new ArrayList<>());
    recyclerViewBidi.setAdapter(bidiListAdapter);

    recyclerViewMatch.setLayoutManager(getLayoutManager());
    matchListAdapter = new ProductCategoryListAdapter(this, new ArrayList<>());
    recyclerViewMatch.setAdapter(matchListAdapter);
  }

  /*private void setupToggleButtons() {
    *//*toggleGroupOne.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
      @Override
      public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (isChecked) {
          toggleGroupTwo.clearChecked();
          toggleGroupThree.clearChecked();
          toggleGroupFour.clearChecked();
        }
      }
    });

    toggleGroupTwo.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
      @Override
      public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (isChecked) {
          toggleGroupOne.clearChecked();
          toggleGroupThree.clearChecked();
          toggleGroupFour.clearChecked();
        }
      }
    });*//*

    *//*toggleGroupThree.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
      @Override
      public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (isChecked) {
          toggleGroupOne.clearChecked();
          toggleGroupTwo.clearChecked();
          toggleGroupFour.clearChecked();
        }
      }
    });

    toggleGroupFour.addOnButtonCheckedListener(new MaterialButtonToggleGroup.OnButtonCheckedListener() {
      @Override
      public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (isChecked) {
          toggleGroupOne.clearChecked();
          toggleGroupTwo.clearChecked();
          toggleGroupThree.clearChecked();
        }
      }
    });*//*
  }*/

  private RecyclerView.LayoutManager getLayoutManager() {
    return new GridLayoutManager(this, 2);
  }

  private void getAllCustomer() {
    Call<List<CustomerData>> call = apiInterface.getAllCustomer(basicAuthorization, loginResponse.getData().getId(), 1);
    call.enqueue(new Callback<List<CustomerData>>() {
      @Override
      public void onResponse(Call<List<CustomerData>> call, Response<List<CustomerData>> response) {
        if (response.isSuccessful()) {
          Log.d(TAG, "onResponse: response.body() = " + response.body());
          customerDataList = response.body();
          spinnerAdapter = new CustomArrayAdapter(SaleActivity.this, R.layout.spinner_item, customerDataList);
          spinnerUsers.setAdapter(spinnerAdapter);
        } else {
          System.out.println("error");
        }
      }

      @Override
      public void onFailure(Call<List<CustomerData>> call, Throwable t) {
        Toast.makeText(SaleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });
  }

  private void getAllCategory() {
    Call<ProductCategory> call = apiInterface.getAllProduct(basicAuthorization);
    call.enqueue(new Callback<ProductCategory>() {
      @Override
      public void onResponse(Call<ProductCategory> call, Response<ProductCategory> response) {
        if (response.isSuccessful()) {
          productCategory = response.body();
          cigretteListAdapter.setData(productCategory.getCigrettee());
          bidiListAdapter.setData(productCategory.getBidi());
          matchListAdapter.setData(productCategory.getMatch());
        }
      }

      @Override
      public void onFailure(Call<ProductCategory> call, Throwable t) {
        Toast.makeText(SaleActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
      }
    });
  }
}
package com.suffix.fieldforce.akg.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
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

  @BindView(R.id.toggleGroup)
  SingleSelectToggleGroup toggleGroup;

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
  private List<CustomerData> filteredCustomerList;
  private ProductCategory productCategory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sale);
    ButterKnife.bind(this);

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    customerDataList = new ArrayList<>();
    filteredCustomerList = new ArrayList<>();
    productCategory = new ProductCategory();

    loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());

    manageRecyclerView();
    getAllCustomer();
    getAllCategory();
    //setupToggleButtons();
    setupToggleGroup();
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

  private void setupToggleGroup() {
    toggleGroup.setOnCheckedChangeListener(new SingleSelectToggleGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(SingleSelectToggleGroup group, int checkedId) {
        switch (checkedId) {
          case R.id.choice_a:
            filterCustomers("a");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_b:
            filterCustomers("b");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_c:
            filterCustomers("c");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_d:
            filterCustomers("d");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_e:
            filterCustomers("e");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_f:
            filterCustomers("f");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_g:
            filterCustomers("g");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_h:
            filterCustomers("h");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_i:
            filterCustomers("i");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_j:
            filterCustomers("j");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_k:
            filterCustomers("k");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_l:
            filterCustomers("l");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_m:
            filterCustomers("m");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_n:
            filterCustomers("n");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_o:
            filterCustomers("o");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_p:
            filterCustomers("p");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_q:
            filterCustomers("q");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_r:
            filterCustomers("r");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_s:
            filterCustomers("s");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_t:
            filterCustomers("t");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_u:
            filterCustomers("u");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_v:
            filterCustomers("v");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_w:
            filterCustomers("w");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_x:
            filterCustomers("x");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_y:
            filterCustomers("y");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_z:
            filterCustomers("z");
            spinnerUsers.setSelection(0);
            break;
          case R.id.choice_all:
            filterCustomers("all");
            spinnerUsers.setSelection(0);
            break;
        }
      }
    });
  }

  private void filterCustomers(String start) {
    filteredCustomerList.clear();
    filteredCustomerList.add(new CustomerData("Select Customer"));
    if (start.toLowerCase().equalsIgnoreCase("all")) {
      filteredCustomerList.addAll(customerDataList);
    } else {
      for (CustomerData customerData : customerDataList) {
        if (customerData.getCustomerName().toLowerCase().startsWith(start.toLowerCase())) {
          filteredCustomerList.add(customerData);
        }
      }
    }

    Log.d(TAG, "filterCustomers: filteredCustomerList = " + filteredCustomerList);
    spinnerAdapter.notifyDataSetChanged();
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
          Log.d(TAG, "onResponse: customerDataList = " + customerDataList);
          filteredCustomerList.clear();
          filteredCustomerList.add(new CustomerData("Select Customer"));
          filteredCustomerList.addAll(customerDataList);
          spinnerAdapter = new CustomArrayAdapter(SaleActivity.this, R.layout.spinner_item, filteredCustomerList);
          spinnerUsers.setAdapter(spinnerAdapter);
          spinnerUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              filteredCustomerList.get(position);
            }
          });
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
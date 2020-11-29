package com.suffix.fieldforce.akg.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import butterknife.OnClick;
import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleActivity extends AppCompatActivity {

  @BindView(R.id.layoutKeys)
  LinearLayout layoutKeys;

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
  public void toggleKeyboard(){
    imgDropArrow.setRotation(imgDropArrow.getRotation()+180f);
    if(layoutKeys.getVisibility() == View.VISIBLE){
      layoutKeys.setVisibility(View.GONE);
    }else{
      layoutKeys.setVisibility(View.VISIBLE);
    }
  }

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private ArrayAdapter<StoreModel> spinnerAdapter;
  private ProductCategoryListAdapter cigretteListAdapter, bidiListAdapter, matchListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sale);
    ButterKnife.bind(this);

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    spinnerAdapter = new CustomArrayAdapter(this, R.layout.spinner_item, getList());
    spinnerUsers.setAdapter(spinnerAdapter);

    manageRecyclerView();
    getAllCategory();

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

  private RecyclerView.LayoutManager getLayoutManager() {
    return new GridLayoutManager(this, 2);
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

  private List<StoreModel> getList() {

    List<StoreModel> myList = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      myList.add(new StoreModel("Solaiman", "Badda, Dhaka"));
    }
    return myList;
  }
}
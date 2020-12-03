package com.suffix.fieldforce.akg.activity;

import android.content.Intent;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.ProductCategoryListAdapter;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.product.ProductCategory;
import com.suffix.fieldforce.akg.util.AkgConstants;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class ProductCategoryActivity extends AppCompatActivity {

  private static final String TAG = "ProductCategoryActivity";

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.recyclerViewCigrettee)
  RecyclerView recyclerViewCigrettee;

  @BindView(R.id.recyclerViewBidi)
  RecyclerView recyclerViewBidi;

  @BindView(R.id.recyclerViewMatch)
  RecyclerView recyclerViewMatch;

  @BindView(R.id.btnJachai)
  Button btnJachai;

  @OnClick(R.id.btnJachai)
  public void gotoCheckout() {
    Intent intent = new Intent(ProductCategoryActivity.this, CheckActivity.class);
    intent.putExtra(AkgConstants.CUSTOMER_INFO, selectedCustomer);
    startActivity(intent);
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
  private CustomerData selectedCustomer = null;
  private Realm realm;
  private RealMDatabaseManager realMDatabaseManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_product_category);
    ButterKnife.bind(this);

    selectedCustomer = getIntent().getParcelableExtra(AkgConstants.CUSTOMER_INFO);
    realMDatabaseManager = new RealMDatabaseManager();

    setupToolbar();
    manageRecyclerView();
    getAllCategory();
  }

  private void setupToolbar() {
    setSupportActionBar(toolbar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
    } else {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayShowTitleEnabled(true);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setTitle("Products");
    }

    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onBackPressed();
      }
    });

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
      toolbar.getNavigationIcon().setColorFilter(new BlendModeColorFilter(Color.WHITE,
          BlendMode.SRC_ATOP));
    } else {
      toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
    }
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

  private void getAllCategory() {
    productCategory = realMDatabaseManager.prepareCategoryData();
    cigretteListAdapter.setData(productCategory.getCigrettee());
    bidiListAdapter.setData(productCategory.getBidi());
    matchListAdapter.setData(productCategory.getMatch());
  }

  private RecyclerView.LayoutManager getLayoutManager() {
    return new GridLayoutManager(this, 2);
  }
}

package com.suffix.fieldforce.akg.activity;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.StockListAdapter;
import com.suffix.fieldforce.akg.adapter.StockPagerAdapter;
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager;
import com.suffix.fieldforce.akg.model.product.CategoryModel;
import com.suffix.fieldforce.akg.model.product.ProductCategory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StockActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.tabLayout)
  TabLayout tabLayout;
  @BindView(R.id.viewPager)
  ViewPager viewPager;


  private static final String TAG = "StockActivity";

  private StockListAdapter stockListAdapter;
  private List<CategoryModel> products;
  private ProductCategory productCategory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stock);
    ButterKnife.bind(this);

    setupToolbar();


    viewPager.setAdapter(new StockPagerAdapter(getSupportFragmentManager()));
    tabLayout.setupWithViewPager(viewPager);

    products = new ArrayList<>();
    productCategory = new ProductCategory();

    getCategoryModel();
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
      getSupportActionBar().setTitle("স্টক");
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

  private void getCategoryModel() {
    productCategory = new RealMDatabaseManager().prepareCategoryData();
    if (productCategory.getCigarette().size() > 0) {
      products.addAll(productCategory.getCigarette());
    }
    if (productCategory.getBidi().size() > 0) {
      products.addAll(productCategory.getBidi());
    }
    if (productCategory.getMatch().size() > 0) {
      products.addAll(productCategory.getMatch());
    }
  }

  public List<CategoryModel> getProducts() {
    return products;
  }
}
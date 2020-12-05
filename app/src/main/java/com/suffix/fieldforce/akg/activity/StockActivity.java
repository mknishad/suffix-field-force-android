package com.suffix.fieldforce.akg.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.PrintingInterface;
import com.suffix.fieldforce.akg.adapter.StockListAdapter;
import com.suffix.fieldforce.akg.adapter.StockPagerAdapter;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.Distributor;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.model.product.CategoryModel;
import com.suffix.fieldforce.akg.model.product.ProductCategory;
import com.suffix.fieldforce.akg.util.AkgPrintingService;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class StockActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @BindView(R.id.txtTotalQuantity)
  TextView txtTotalQuantity;

  @BindView(R.id.txtResponse)
  TextView txtResponse;

  @BindView(R.id.txtCurrentQuantity)
  TextView txtCurrentQuantity;

  @BindView(R.id.srNameTextView)
  TextView srNameTextView;

  @BindView(R.id.timeTextView)
  TextView timeTextView;

  @BindView(R.id.layoutScroll)
  NestedScrollView layoutScroll;

  @BindView(R.id.tabLayout)
  TabLayout tabLayout;

  @BindView(R.id.viewPager)
  ViewPager viewPager;

  @OnClick(R.id.btnPrint)
  public void printStock() {
    progressDialog.show();
    Distributor distributor = new Gson().fromJson(preferences.getDistributor(), Distributor.class);
    AkgLoginResponse loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    new AkgPrintingService().printStock(distributor.getData().getDistributorName(),
        distributor.getData().getMobile(), loginResponse, products, new PrintingInterface() {
          @Override
          public void onPrintSuccess(String message) {
            progressDialog.dismiss();
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                //onBackPressed();
              }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
          }

          @Override
          public void onPrintFail(String message) {
            progressDialog.dismiss();
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                //alertDialog.dismiss();
              }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
          }
        });
  }

  private static final String TAG = "StockActivity";

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private AkgLoginResponse loginResponse;
  private StockListAdapter stockListAdapter;
  private List<CategoryModel> products;
  private ProductCategory productCategory;
  private InvoiceRequest invoiceRequest;
  private Realm realm;

  private ProgressDialog progressDialog;
  private AlertDialog.Builder builder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_stock);
    ButterKnife.bind(this);

    setupToolbar();


    viewPager.setAdapter(new StockPagerAdapter(getSupportFragmentManager()));
    tabLayout.setupWithViewPager(viewPager);


    realm = Realm.getDefaultInstance();

    progressDialog = new ProgressDialog(this);
    progressDialog.setMessage("Printing...");

    builder = new AlertDialog.Builder(this);

    products = new ArrayList<>();
    productCategory = new ProductCategory();

    preferences = new FieldForcePreferences(this);
    loginResponse = new Gson().fromJson(preferences.getLoginResponse(), AkgLoginResponse.class);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    LinearLayoutManager manager = new LinearLayoutManager(this);
    recyclerView.setLayoutManager(manager);

    stockListAdapter = new StockListAdapter(this, products);
    recyclerView.setAdapter(stockListAdapter);

    getCategoryModel();

    srNameTextView.setText("SR Name: " + loginResponse.getData().getUserName());
    String time = android.text.format.DateFormat.format("hh:mm a", new java.util.Date()).toString();
    timeTextView.setText(time);
    int totalQuantity = 0;
    int currentQuantity = 0;
    for (CategoryModel categoryModel : products) {
      totalQuantity += categoryModel.getInHandQty();
      currentQuantity += categoryModel.getInHandQty() - categoryModel.getSalesQty();
    }
    //txtTotalQuantity.setText(String.valueOf(totalQuantity));
    txtCurrentQuantity.setText(String.valueOf(currentQuantity));
//    stockBodyListAdapter.setData(invoiceRequest.getInvoiceProducts());
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

    if(products.size() > 0){
      txtResponse.setVisibility(View.GONE);
      layoutScroll.setVisibility(View.VISIBLE);
      stockListAdapter.setData(products);
    } else {
      txtResponse.setVisibility(View.VISIBLE);
      layoutScroll.setVisibility(View.GONE);
    }
  }
}
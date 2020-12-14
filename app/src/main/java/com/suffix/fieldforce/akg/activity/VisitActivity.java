package com.suffix.fieldforce.akg.activity;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.ProductCategoryListAdapter;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.manager.SyncManager;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.StoreVisitRequest;
import com.suffix.fieldforce.akg.model.product.ProductCategory;
import com.suffix.fieldforce.akg.util.NetworkUtils;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import okhttp3.Credentials;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.radioGroup)
  RadioGroup radioGroup;

  @BindView(R.id.layoutSubmit)
  LinearLayout layoutSubmit;

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
  private int customerID;
  private String status = "0";
  double lat;
  double lon;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_visit);
    ButterKnife.bind(this);

    customerID = getIntent().getIntExtra("customerID", 0);

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());

    setupToolbar();
    initView();
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
      getSupportActionBar().setTitle("ভিজিট");
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

  @OnClick(R.id.layoutSubmit)
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.layoutSubmit:
        visitStore();
        break;
    }
  }

  private void initView() {

    SmartLocation.with(this).location()
        .oneFix()
        .start(new OnLocationUpdatedListener() {
          @Override
          public void onLocationUpdated(Location location) {
            preferences.putLocation(location);
            layoutSubmit.setVisibility(View.VISIBLE);
            lat = location.getLatitude();
            lon = location.getLongitude();
          }
        });

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
          case R.id.radioBtnAis:
            status = "2";
            break;
          case R.id.radioBtnClosed:
            status = "3";
            break;
          case R.id.radioBtnOthers:
            status = "4";
            break;
        }
      }
    });
  }

  private void visitStore() {
    if (!status.equals("0")) {
      StoreVisitRequest storeVisitRequest = new StoreVisitRequest();
      storeVisitRequest.setConsumerId(customerID);
      storeVisitRequest.setEntryTime(System.currentTimeMillis());
      storeVisitRequest.setLat(lat);
      storeVisitRequest.setLng(lon);
      storeVisitRequest.setSalesRepId(loginResponse.getData().getId());
      storeVisitRequest.setStatus(status);

      if (!NetworkUtils.isNetworkConnected(this)) {
        //TODO: save to database
        storeVisitRequest.setSynced(false);
        new SyncManager(this).insertStoreVisit(storeVisitRequest, null);
      } else {
        Call<ResponseBody> call = apiInterface.visitStore(basicAuthorization, storeVisitRequest);
        call.enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
              storeVisitRequest.setSynced(true);
              new SyncManager(VisitActivity.this).insertStoreVisit(storeVisitRequest, null);
              finish();
            } else {
              storeVisitRequest.setSynced(false);
              new SyncManager(VisitActivity.this).insertStoreVisit(storeVisitRequest, null);
              Toast.makeText(VisitActivity.this, response.message(), Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
            storeVisitRequest.setSynced(false);
            new SyncManager(VisitActivity.this).insertStoreVisit(storeVisitRequest, null);
            Toast.makeText(VisitActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
          }
        });
      }
    } else {
      Toast.makeText(VisitActivity.this, "You must select a customer status first.", Toast.LENGTH_SHORT).show();
    }
  }
}
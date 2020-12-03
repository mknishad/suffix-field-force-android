package com.suffix.fieldforce.akg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suffix.fieldforce.BuildConfig;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.home.MainDashboardActivity;
import com.suffix.fieldforce.akg.adapter.ProductCategoryListAdapter;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.StoreVisitRequest;
import com.suffix.fieldforce.akg.model.product.ProductCategory;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import io.realm.Realm;
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
  private int salesRepId;
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
  public void onViewClicked(View view){
    switch (view.getId()){
      case R.id.layoutSubmit:
        visitStore();
        break;
    }
  }

  private void initView(){

    SmartLocation.with(this).location()
        .oneFix()
        .start(new OnLocationUpdatedListener() {
          @Override
          public void onLocationUpdated(Location location) {
            preferences.putLocation(location);
            SmartLocation.with(VisitActivity.this).geocoding()
                .reverse(location, new OnReverseGeocodingListener() {
                  @Override
                  public void onAddressResolved(Location location, List<Address> list) {

                    lat = location.getAltitude();
                    lon = location.getLongitude();

                  }
                });
          }
        });

    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
          case R.id.radioBtnAis:
            salesRepId = 2;
            break;
          case R.id.radioBtnClosed:
            salesRepId = 3;
            break;
          case R.id.radioBtnOthers:
            salesRepId = 4;
            break;
        }
      }
    });
  }

  private void visitStore(){
    Date date = new Date();
    //This method returns the time in millis
    long timeMilli = date.getTime();
    StoreVisitRequest storeVisitRequest = new StoreVisitRequest();
    storeVisitRequest.setConsumerId(customerID);
    storeVisitRequest.setEntryTime(timeMilli);
    storeVisitRequest.setLat(lat);
    storeVisitRequest.setLng(lon);
    storeVisitRequest.setSalesRepId(salesRepId);
    storeVisitRequest.setStatus(salesRepId);

    Call<ResponseBody> call = apiInterface.visitStore(basicAuthorization, storeVisitRequest);
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//        Toast.makeText(VisitActivity.this, "Success", Toast.LENGTH_SHORT).show();
          finish();
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {

      }
    });


  }

}
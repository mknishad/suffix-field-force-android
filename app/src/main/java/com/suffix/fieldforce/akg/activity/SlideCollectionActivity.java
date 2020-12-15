package com.suffix.fieldforce.akg.activity;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.nex3z.togglebuttongroup.SingleSelectToggleGroup;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.CustomArrayAdapter;
import com.suffix.fieldforce.akg.adapter.GiftListAdapter;
import com.suffix.fieldforce.akg.adapter.GiftListAdapterInterface;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager;
import com.suffix.fieldforce.akg.database.manager.SyncManager;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.Gift;
import com.suffix.fieldforce.akg.model.GiftInvoiceRequest;
import com.suffix.fieldforce.akg.model.GlobalSettings;
import com.suffix.fieldforce.akg.model.Slider;
import com.suffix.fieldforce.akg.model.product.GiftModel;
import com.suffix.fieldforce.akg.util.LocationUtils;
import com.suffix.fieldforce.akg.util.NetworkUtils;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.realm.RealmList;
import okhttp3.Credentials;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SlideCollectionActivity extends AppCompatActivity {

  private static final String TAG = "SlideCollectionActivity";

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @BindView(R.id.toggleGroup)
  SingleSelectToggleGroup toggleGroup;

  @BindView(R.id.spinnerUsers)
  Spinner spinnerUsers;

  @BindView(R.id.btnSubmit)
  Button btnSubmit;

  @BindView(R.id.txtQuantity)
  TextInputLayout txtQuantity;

  @BindView(R.id.progress_bar)
  ProgressBar progressBar;

  private FieldForcePreferences preferences;
  private AkgLoginResponse loginResponse;
  private ArrayAdapter<CustomerData> spinnerAdapter;
  private List<CustomerData> customerDataList;
  private List<CustomerData> filteredCustomerList;
  private RealMDatabaseManager realMDatabaseManager;
  private CustomerData selectedCustomer;
  private AkgApiInterface apiInterface;
  private String basicAuthorization;
  private GiftListAdapter giftListAdapter;
  private List<GiftModel> giftModelList;

  private boolean isInputChangedByUser = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_slide_collection);
    ButterKnife.bind(this);

    setupToolbar();

    realMDatabaseManager = new RealMDatabaseManager();
    preferences = new FieldForcePreferences(this);
    loginResponse = new Gson().fromJson(preferences.getLoginResponse(), AkgLoginResponse.class);
    customerDataList = new ArrayList<>();
    giftModelList = new ArrayList<>();
    filteredCustomerList = new ArrayList<>();
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);

    Log.d(TAG, "onCreate: " + loginResponse.getData().getPassword() + " " + loginResponse.getData().getUserId());

    basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());

    getAllCustomer();
    setupToggleGroup();

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    giftListAdapter = new GiftListAdapter(this, giftModelList, new GiftListAdapterInterface() {
      @Override
      public void onPlusClicked(int position) {
        isInputChangedByUser = false;
        if (giftModelList.get(position).getSliderQty() <= Integer.parseInt(txtQuantity.getEditText().getText().toString())) {
          txtQuantity.getEditText().setText(String.valueOf(Integer.parseInt(txtQuantity.getEditText().getText().toString()) - giftModelList.get(position).getSliderQty()));
          giftModelList.get(position).setProductQuantity(giftModelList.get(position).getProductQuantity() + 1);
          giftListAdapter.setData(giftModelList);
        }else{
          Toast.makeText(SlideCollectionActivity.this, "পর্যাপ্ত স্লাইড নেই", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onMinusClicked(int position) {
        isInputChangedByUser = false;
        if (giftModelList.get(position).getProductQuantity() <= 0) {

        } else {
          txtQuantity.getEditText().setText(String.valueOf(Integer.parseInt(txtQuantity.getEditText().getText().toString()) + giftModelList.get(position).getSliderQty()));
          giftModelList.get(position).setProductQuantity(giftModelList.get(position).getProductQuantity() - 1);
          giftListAdapter.setData(giftModelList);
        }
      }
    });
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(giftListAdapter);

    getGifts();

    txtQuantity.getEditText().setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        isInputChangedByUser = true;
      }
    });

    txtQuantity.getEditText().addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(txtQuantity.getEditText().getText()) && isInputChangedByUser) {
          getGifts();
        }
      }
    });
  }

  private void getGifts() {
    giftModelList = new RealMDatabaseManager().prepareGiftModel();
    giftListAdapter.setData(giftModelList);
  }

  private void setupToolbar() {
    setSupportActionBar(toolbar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
    } else {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getResources().getString(R.string.slider_collect));
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

  @Override
  protected void onStart() {
    super.onStart();

    btnSubmit.setOnClickListener(view -> {
      if (selectedCustomer == null) {
        Toast.makeText(this, getResources().getString(R.string.msg_customer_select_error), Toast.LENGTH_SHORT).show();
      } else if (TextUtils.isEmpty(txtQuantity.getEditText().getText())) {
        txtQuantity.setError(getResources().getString(R.string.msg_packet_quantity_not_given));
      } else {
        progressBar.setVisibility(View.VISIBLE);
        SmartLocation.with(SlideCollectionActivity.this).location()
            .oneFix()
            .start(new OnLocationUpdatedListener() {
              @Override
              public void onLocationUpdated(Location location) {

                Log.d(TAG, "current location: " + location.getLatitude() + ":" + location.getLatitude());
                Log.d(TAG, "customer location: " + selectedCustomer.getLat() + ":" + selectedCustomer.getLng());

                double distance = LocationUtils.getDistance(selectedCustomer.getLat(), selectedCustomer.getLng(),
                    location.getLatitude(), location.getLongitude());

                double distanceThreshold = 10.0;
                for (GlobalSettings settings : loginResponse.getData().getGlobalSettingList()) {
                  if (settings.getAttributeName().equalsIgnoreCase("DISTANCE_RANGE")) {
                    distanceThreshold = Double.parseDouble(settings.getAttributeValue());
                  }
                }
                Log.d(TAG, "cur dis: " + distance);
                Log.d(TAG, "global distance:" + distanceThreshold);
                if (distance > distanceThreshold) {
                  Toast.makeText(SlideCollectionActivity.this, "আপনি কাস্টমার থেকে দূরে অবস্থান করছেন!",
                      Toast.LENGTH_SHORT).show();
                  progressBar.setVisibility(View.GONE);
                } else {
                  sendSliderInfo();
                }
              }
            });
      }
    });
  }

  public void sendSliderInfo() {

    Slider slider = new Slider();
    slider.setCollectionDate(new Date().getTime());
    slider.setCustomerId(selectedCustomer.getId());
    slider.setQuantity(Integer.valueOf(txtQuantity.getEditText().getText().toString()));
    slider.setSalesRepId(loginResponse.getData().getId());

    RealmList<Gift> gifts = new RealmList<>();
    for (GiftModel giftModel : giftModelList) {
      if (giftModel.getProductQuantity() > 0) {
        gifts.add(new Gift(giftModel.getProductId(), giftModel.getProductQuantity()));
      }
    }

    long currentTimeMillis = System.currentTimeMillis();

    GiftInvoiceRequest giftInvoiceRequest = new GiftInvoiceRequest(
        selectedCustomer.getId(),
        currentTimeMillis,
        selectedCustomer.getId() + "_" + currentTimeMillis,
        gifts,
        "gift",
        loginResponse.getData().getId()
    );

    if (!NetworkUtils.isNetworkConnected(this)) {
      //TODO: save to database
      giftInvoiceRequest.setSynced(false);
      new SyncManager(this).insertGiftInvoice(giftInvoiceRequest, null);
      progressBar.setVisibility(View.GONE);
      Toast.makeText(SlideCollectionActivity.this, getResources().getString(R.string.msg_slider_collect_success), Toast.LENGTH_SHORT).show();
      finish();
    } else {
      Call<ResponseBody> call = apiInterface.createGiftInvoice(basicAuthorization, giftInvoiceRequest);
      call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          if (response.isSuccessful()) {
            giftInvoiceRequest.setSynced(true);
            new SyncManager(SlideCollectionActivity.this).insertGiftInvoice(giftInvoiceRequest, null);
            Toast.makeText(SlideCollectionActivity.this, getResources().getString(R.string.msg_slider_collect_success), Toast.LENGTH_SHORT).show();
          } else {
            giftInvoiceRequest.setSynced(false);
            new SyncManager(SlideCollectionActivity.this).insertGiftInvoice(giftInvoiceRequest, null);
            Toast.makeText(SlideCollectionActivity.this, "Error:" + response.message(), Toast.LENGTH_SHORT).show();
          }
          progressBar.setVisibility(View.GONE);
          finish();
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
          giftInvoiceRequest.setSynced(true);
          new SyncManager(SlideCollectionActivity.this).insertGiftInvoice(giftInvoiceRequest, null);
          Toast.makeText(SlideCollectionActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
          call.cancel();
          progressBar.setVisibility(View.GONE);
        }
      });
    }

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

  private void getAllCustomer() {
    customerDataList = realMDatabaseManager.prepareCustomerData();
    filteredCustomerList.clear();
    filteredCustomerList.add(new CustomerData(getResources().getString(R.string.customer_select)));
    filteredCustomerList.addAll(customerDataList);
    spinnerAdapter = new CustomArrayAdapter(SlideCollectionActivity.this, R.layout.spinner_item_no_back, filteredCustomerList);
    spinnerUsers.setAdapter(spinnerAdapter);
    spinnerUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
          CustomerData customerData = filteredCustomerList.get(position);
          selectedCustomer = customerData;
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

}
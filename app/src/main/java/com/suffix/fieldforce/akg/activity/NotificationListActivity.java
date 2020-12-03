package com.suffix.fieldforce.akg.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.akg.adapter.NotificationListAdapter;
import com.suffix.fieldforce.akg.adapter.NotificationListInterface;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.model.ActiveCustomerRequest;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import okhttp3.Credentials;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationListActivity extends AppCompatActivity {

  @BindView(R.id.recyclerViewNotification)
  RecyclerView recyclerViewNotification;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.noti_progress)
  ProgressBar progressBar;

  @BindView(R.id.layout_no_item)
  LinearLayout noItemLayout;

  private String basicAuthorization;
  List<CustomerData> customerDataList;

  private FieldForcePreferences preferences;
  public AkgApiInterface apiInterface;
  NotificationListAdapter notificationListAdapter;
  private AkgLoginResponse loginResponse;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notififation_list);
    ButterKnife.bind(this);

    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);

    loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());

    customerDataList = new ArrayList<>();

    notificationListAdapter = new NotificationListAdapter(NotificationListActivity.this, customerDataList);



    LinearLayoutManager manager = new LinearLayoutManager(NotificationListActivity.this);
    recyclerViewNotification.setLayoutManager(manager);
    recyclerViewNotification.setAdapter(notificationListAdapter);

    setupToolbar();
    getAllNotification();

  }

  private void setupToolbar() {
    setSupportActionBar(toolbar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
    } else {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle(getResources().getString(R.string.notification_activity_title));
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


  private void getAllNotification() {
    progressBar.setVisibility(View.VISIBLE);
    Call<List<CustomerData>> call = apiInterface.getAllCustomer(basicAuthorization, loginResponse.getData().getId(), 0);
    call.enqueue(new Callback<List<CustomerData>>() {
      @Override
      public void onResponse(Call<List<CustomerData>> call, Response<List<CustomerData>> response) {
        if (response.isSuccessful()) {
          customerDataList = response.body();
          if (customerDataList.size() == 0) {
            noItemLayout.setVisibility(View.VISIBLE);
          }
          notificationListAdapter.setData(customerDataList);
        }else{
          Toast.makeText(NotificationListActivity.this, "Error:"+response.message(), Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);
      }

      @Override
      public void onFailure(Call<List<CustomerData>> call, Throwable t) {
        Toast.makeText(NotificationListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
        progressBar.setVisibility(View.GONE);
      }
    });
  }

  @Override
  protected void onStart() {
    super.onStart();

    notificationListAdapter.setMemoListInterface(new NotificationListInterface() {
      @Override
      public void onItemClick(int position, int customerId) {

        progressBar.setVisibility(View.VISIBLE);

        SmartLocation.with(NotificationListActivity.this).location()
            .oneFix()
            .start(new OnLocationUpdatedListener() {
              @Override
              public void onLocationUpdated(Location location) {

                ActiveCustomerRequest activeCustomerRequest = new ActiveCustomerRequest(customerId,
                    location.getLatitude(), location.getLongitude());

                activeCustomer(activeCustomerRequest, position);
              }
            });
      }
    });

  }

  public void activeCustomer(ActiveCustomerRequest activeCustomerRequest, int listPosition) {
    Call<ResponseBody> call = apiInterface.activeCustomer(basicAuthorization,
        activeCustomerRequest);

    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.code() == 200) {
          Toast.makeText(NotificationListActivity.this, "Customer activated successfully!", Toast.LENGTH_SHORT).show();
          customerDataList.remove(listPosition);
          notificationListAdapter.notifyDataSetChanged();
        } else {
          Toast.makeText(NotificationListActivity.this, "Error! Customer activation failed.", Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.GONE);
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Toast.makeText(NotificationListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        call.cancel();
        progressBar.setVisibility(View.GONE);
      }
    });

  }
}
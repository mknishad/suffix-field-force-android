package com.suffix.fieldforce.activity.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.suffix.fieldforce.BuildConfig;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.bill.BillDashboardActivity;
import com.suffix.fieldforce.activity.chat.ChatDashboardActivity;
import com.suffix.fieldforce.activity.inventory.InventoryDashboardActivity;
import com.suffix.fieldforce.activity.location.LocationListener;
import com.suffix.fieldforce.activity.roster.RosterManagementActivity;
import com.suffix.fieldforce.activity.task.TaskDashboard;
import com.suffix.fieldforce.activity.transport.TransportRequasitionListActivity;
import com.suffix.fieldforce.akg.activity.MemoListActivity;
import com.suffix.fieldforce.akg.activity.NotificationListActivity;
import com.suffix.fieldforce.akg.activity.SaleActivity;
import com.suffix.fieldforce.akg.activity.SlideCollectionActivity;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager;
import com.suffix.fieldforce.akg.database.manager.SyncManager;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.AttendenceRequest;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.util.CustomProgress;
import com.suffix.fieldforce.akg.util.NetworkUtils;
import com.suffix.fieldforce.location.LocationUpdatesBroadcastReceiver;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import okhttp3.Credentials;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDashboardActivity extends AppCompatActivity implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.imgUserProfile)
  ImageView imgUserProfile;

  @BindView(R.id.imgNotification)
  ImageView imgNotification;

  @BindView(R.id.txtUserName)
  TextView txtUserName;

  @BindView(R.id.txtUserAddress)
  TextView txtUserAddress;

  @BindView(R.id.layoutAttendance)
  LinearLayout layoutAttendance;

  @BindView(R.id.layoutExit)
  LinearLayout layoutExit;

  @BindView(R.id.layoutTask)
  LinearLayout layoutTask;

  @BindView(R.id.layoutRoster)
  LinearLayout layoutRoster;

  @BindView(R.id.layoutBilling)
  LinearLayout layoutBilling;

  @BindView(R.id.layoutInventory)
  LinearLayout layoutInventory;

  @BindView(R.id.layoutChat)
  LinearLayout layoutChat;

  @BindView(R.id.layoutSiteMap)
  LinearLayout layoutSiteMap;

  @BindView(R.id.layoutGIS)
  LinearLayout layoutGIS;

  @BindView(R.id.layoutSync)
  LinearLayout layoutSync;

  @BindView(R.id.layoutClosing)
  LinearLayout layoutClosing;

  @BindView(R.id.badge)
  View badge;

  private static final String TAG = "MainDashboardActivity";

  private final String ENTRY_TYPE_IN = "i";
  private final String ENTRY_TYPE_OUT = "o";

  private static int REQUEST_CHECK_SETTINGS = 1000;
  private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
  private static final long UPDATE_INTERVAL = 10 * 60 * 1000;
  private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
  private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;

  private LocationRequest mLocationRequest;
  private GoogleApiClient mGoogleApiClient;

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private AkgLoginResponse loginResponse;

  private ProgressDialog progress;

  @OnClick({R.id.layoutAttendance, R.id.layoutExit, R.id.layoutTask, R.id.layoutRoster, R.id.layoutBilling, R.id.layoutInventory, R.id.layoutChat, R.id.layoutSiteMap, R.id.layoutGIS, R.id.imgNotification, R.id.layoutSync, R.id.layoutClosing})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.layoutAttendance:
        geoAttendance();
        break;
      case R.id.layoutExit:
        if (preferences.getOnline()) {
          geoExit();
        } else {
          Toast.makeText(MainDashboardActivity.this, "You can't exit without making attendance", Toast.LENGTH_SHORT).show();
        }
        break;
      case R.id.layoutTask:
        //openTask();
        openSales();
        break;
      case R.id.layoutRoster:
        //openRoster();
        openMemo();
        break;
      case R.id.layoutBilling:
        openBills();
        break;
      case R.id.layoutInventory:
        openInventory();
        break;
      case R.id.layoutChat:
        openMessage();
        break;
      case R.id.layoutSiteMap:
        openTR();
        break;
      case R.id.layoutGIS:
        openGIS();
        break;
      case R.id.imgNotification:
        openNotification();
        break;
      case R.id.layoutSync:
        syncData();
        break;
      case R.id.layoutClosing:
        closeSales();
        break;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_dashboard_type_two);
    ButterKnife.bind(this);

    toolbar.setVisibility(View.GONE);
    init();
  }

  private void init() {
    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);
    loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    txtUserName.setText(loginResponse.getData().getUserName());

    // Check if the user revoked runtime permissions.
    if (!checkPermissions()) {
      requestPermissions();
    }

    buildGoogleApiClient();
    getDeviceLocation(new LocationListener() {
      @Override
      public void onLocationUpdate(Location location) {

      }
    }, "");
    //initLocationSettings();

    if (preferences.getOnline()) {
      txtUserAddress.setText(preferences.getAddress());
      badge.setBackground(ContextCompat.getDrawable(MainDashboardActivity.this, R.drawable.circular_badge_online));
    } else {
      badge.setBackground(ContextCompat.getDrawable(MainDashboardActivity.this, R.drawable.circular_badge_offline));
    }
  }

  private void initLocationSettings() {
    LocationRequest locationRequest = new LocationRequest();
    locationRequest.setInterval(UPDATE_INTERVAL);
    locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest);
    SettingsClient client = LocationServices.getSettingsClient(this);
    Task task = client.checkLocationSettings(builder.build());

    task.addOnSuccessListener(o -> {
      //buildGoogleApiClient();
      if (preferences.getOnline()) {
        goOffline();
      } else {
        goOnline();
      }
    }).addOnFailureListener(e -> {
      if (e instanceof ResolvableApiException) {
        try {
          ((ResolvableApiException) e).startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
        } catch (IntentSender.SendIntentException ex) {
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  private void getDeviceLocation(LocationListener locationListener, String text) {
    try {
      SmartLocation.with(this).location()
          .oneFix()
          .start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
              locationListener.onLocationUpdate(location);
            }
          });
    } catch (Exception e) {
      Log.e(TAG, "getDeviceLocation: ", e);
    }
  }

  private void goOnline() {
    //requestLocationUpdates();
    preferences.putOnline(true);
  }

  private void goOffline() {
    //removeLocationUpdates();
    preferences.putOnline(false);
  }

  private void createLocationRequest() {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
  }

  private void buildGoogleApiClient() {
    if (mGoogleApiClient != null) {
      return;
    }
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .enableAutoManage(this, this)
        .addApi(LocationServices.API)
        .build();
    createLocationRequest();
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    Log.i(TAG, "GoogleApiClient connected");
  }

  private PendingIntent getPendingIntent() {
    Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
    intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
    return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }

  @Override
  public void onConnectionSuspended(int i) {
    final String text = "Connection suspended";
    Log.w(TAG, text + ": Error code: " + i);
    showSnackbar("Connection suspended");
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    final String text = "Exception while connecting to Google Play services";
    Log.w(TAG, text + ": " + connectionResult.getErrorMessage());
    showSnackbar(text);
  }

  private void showSnackbar(final String text) {
    View container = findViewById(R.id.userRelativeLayout);
    if (container != null) {
      Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
    }
  }

  private boolean checkPermissions() {
    int permissionState = ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION);
    return permissionState == PackageManager.PERMISSION_GRANTED;
  }

  private void requestPermissions() {
    boolean shouldProvideRationale =
        ActivityCompat.shouldShowRequestPermissionRationale(this,
            Manifest.permission.ACCESS_FINE_LOCATION);

    if (shouldProvideRationale) {
      Log.i(TAG, "Displaying permission rationale to provide additional context.");
      Snackbar.make(
          findViewById(R.id.userRelativeLayout),
          R.string.permission_rationale,
          Snackbar.LENGTH_INDEFINITE)
          .setAction(R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // Request permission
              ActivityCompat.requestPermissions(MainDashboardActivity.this,
                  new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                  REQUEST_PERMISSIONS_REQUEST_CODE);
            }
          })
          .show();
    } else {
      Log.i(TAG, "Requesting permission");
      ActivityCompat.requestPermissions(MainDashboardActivity.this,
          new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
          REQUEST_PERMISSIONS_REQUEST_CODE);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    Log.i(TAG, "onRequestPermissionResult");
    if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
      if (grantResults.length <= 0) {
        Log.i(TAG, "User interaction was cancelled.");
      } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        buildGoogleApiClient();
      } else {
        Snackbar.make(
            findViewById(R.id.userRelativeLayout),
            R.string.permission_denied_explanation,
            Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.settings, new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                // Build intent that displays the App settings screen.
                Intent intent = new Intent();
                intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",
                    BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
              }
            })
            .show();
      }
    }
  }

  public void requestLocationUpdates() {
    try {
      Log.i(TAG, "Starting location updates");
      LocationServices.FusedLocationApi.requestLocationUpdates(
          mGoogleApiClient, mLocationRequest, getPendingIntent());
    } catch (SecurityException e) {
      e.printStackTrace();
    }
  }

  public void removeLocationUpdates() {
    Log.i(TAG, "Removing location updates");
    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
        getPendingIntent());
  }

  @SuppressLint("RestrictedApi")
  private void geoAttendance() {

    BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
        .setTitle("ENTRY?")
        .setMessage("Are you sure want to make attendance?")
        .setCancelable(false)
        .setPositiveButton("Yes", R.drawable.ic_tik, new BottomSheetMaterialDialog.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int which) {
            getDeviceLocation(new LocationListener() {
              @Override
              public void onLocationUpdate(Location location) {

                //requestLocationUpdates();

                AkgLoginResponse loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
                    AkgLoginResponse.class);

                String basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
                    preferences.getPassword());

                Log.d(TAG, "onLocationUpdate: basicAuthorization = " + basicAuthorization);

                AttendenceRequest attendenceRequest = new AttendenceRequest(
                    System.currentTimeMillis(),
                    location.getLatitude(),
                    location.getLongitude(),
                    ENTRY_TYPE_IN,
                    loginResponse.getData().getId()
                );

                Call<ResponseBody> attendanceEntry = apiInterface.attendanceEntry(
                    basicAuthorization, attendenceRequest);

                attendanceEntry.enqueue(new Callback<ResponseBody>() {
                  @Override
                  public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                      Toast.makeText(MainDashboardActivity.this, "Entered!", Toast.LENGTH_SHORT).show();
                      showAddressName(location);
                      preferences.putOnline(true);
                      badge.setBackground(ContextCompat.getDrawable(MainDashboardActivity.this, R.drawable.circular_badge_online));
                    } else {
                      Toast.makeText(MainDashboardActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                  }

                  @Override
                  public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(MainDashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                  }
                });
              }
            }, " ENTERED  : ");

            dialogInterface.dismiss();
          }
        })
        .setNegativeButton("No", R.drawable.ic_delete, new BottomSheetMaterialDialog.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int which) {
            Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
            dialogInterface.dismiss();
          }
        })
        .build();

    // Show Dialog
    mBottomSheetDialog.show();
  }

  private void showAddressName(Location location) {

    SmartLocation.with(MainDashboardActivity.this).geocoding()
        .reverse(location, new OnReverseGeocodingListener() {
          @Override
          public void onAddressResolved(Location location, List<Address> list) {
            String result = null;

            if (list != null && list.size() > 0) {
              Address address = list.get(0);
              // sending back first address line and locality
              result = address.getAddressLine(0) + ", " + address.getLocality();
            } else {
              result = "No address found for this location";
            }
            preferences.putAddress(result);
            txtUserAddress.setText(result);
          }
        });
  }

  @SuppressLint("RestrictedApi")
  private void geoExit() {

    BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
        .setTitle("EXIT?")
        .setMessage("Are you sure you want to exit?")
        .setCancelable(false)
        .setPositiveButton("Yes", R.drawable.ic_tik, new BottomSheetMaterialDialog.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int which) {

            getDeviceLocation(new LocationListener() {
              @Override
              public void onLocationUpdate(Location location) {
                //requestLocationUpdates();

                AkgLoginResponse loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
                    AkgLoginResponse.class);

                String basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
                    preferences.getPassword());

                Log.d(TAG, "onLocationUpdate: basicAuthorization = " + basicAuthorization);

                AttendenceRequest attendenceRequest = new AttendenceRequest(
                    System.currentTimeMillis(),
                    location.getLatitude(),
                    location.getLongitude(),
                    ENTRY_TYPE_OUT,
                    loginResponse.getData().getId()
                );

                Call<ResponseBody> attendanceEntry = apiInterface.attendanceEntry(
                    basicAuthorization, attendenceRequest);

                attendanceEntry.enqueue(new Callback<ResponseBody>() {
                  @Override
                  public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                      preferences.putOnline(false);
                      badge.setBackground(ContextCompat.getDrawable(MainDashboardActivity.this, R.drawable.circular_badge_offline));
                      Toast.makeText(MainDashboardActivity.this, "Exited!", Toast.LENGTH_SHORT).show();
                    } else {
                      Toast.makeText(MainDashboardActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                  }

                  @Override
                  public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(MainDashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                  }
                });
              }
            }, " EXIT  : ");
            dialogInterface.dismiss();
          }
        })
        .setNegativeButton("No", R.drawable.ic_delete, new BottomSheetMaterialDialog.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int which) {
            Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
            dialogInterface.dismiss();
          }
        })
        .build();

    // Show Dialog
    mBottomSheetDialog.show();
  }

  public void openTask() {
    Intent intent = new Intent(MainDashboardActivity.this, TaskDashboard.class);
    startActivity(intent);
  }

  public void openSales() {
    Intent intent = new Intent(MainDashboardActivity.this, SaleActivity.class);
    startActivity(intent);
  }

  public void openMemo() {
    Intent intent = new Intent(MainDashboardActivity.this, MemoListActivity.class);
    startActivity(intent);
  }

  public void openRoster() {
    Intent intent = new Intent(MainDashboardActivity.this, RosterManagementActivity.class);
    startActivity(intent);
  }

  public void openBills() {
    Intent intent = new Intent(MainDashboardActivity.this, BillDashboardActivity.class);
    startActivity(intent);
  }

  public void openInventory() {
    Intent intent = new Intent(MainDashboardActivity.this, InventoryDashboardActivity.class);
    startActivity(intent);
  }

  public void openMessage() {
    Intent intent = new Intent(MainDashboardActivity.this, ChatDashboardActivity.class);
    startActivity(intent);
  }

  private void openTR() {
    Intent intent = new Intent(MainDashboardActivity.this, TransportRequasitionListActivity.class);
    startActivity(intent);
  }

  private void openGIS() {
    Intent intent = new Intent(MainDashboardActivity.this, SlideCollectionActivity.class);
    startActivity(intent);
  }

  private void openNotification() {
    Intent intent = new Intent(MainDashboardActivity.this, NotificationListActivity.class);
    startActivity(intent);
  }

  private void syncData() {
    Log.d("Realm", "Sync Data Called");

    SyncManager syncManager = new SyncManager(MainDashboardActivity.this);
    syncManager.getAllCustomer();

//    new RealMDatabaseManager().deleteAllCustomer(new RealmDatabseManagerInterface.Customer() {
//      @Override
//      public void onCustomerDelete(boolean OnSuccess) {
//        SyncManager syncManager = new SyncManager(MainDashboardActivity.this);
//        syncManager.getAllCustomer();
//      }
//    });
  }

  private List<InvoiceRequest> invoiceRequestList;
  private List<InvoiceRequest> failedInvoices;

  public void closeSales() {
    if (!NetworkUtils.isNetworkConnected(this)) {
      Toast.makeText(this, "ইন্টারনেট সংযোগ নেই!", Toast.LENGTH_SHORT).show();
      return;
    }

    invoiceRequestList = new RealMDatabaseManager().prepareInvoiceRequest();
    if (invoiceRequestList.size() > 0) {
      failedInvoices = new ArrayList<>();
      for (InvoiceRequest invoiceRequest : invoiceRequestList) {
        if (!invoiceRequest.getStatus()) {
          failedInvoices.add(invoiceRequest);
        }
      }
      Log.d(TAG, "closeSales: failedInvoices.size() = " + failedInvoices.size());

      if (failedInvoices.size() > 0) {
        progress = new ProgressDialog(this);
        progress.setMessage("Closing Sales");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.show();
        syncFailedInvoices();
      } else {
        Toast.makeText(this, "ডাটা হালনাগাদ হয়েছে!", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(this, "ডাটা হালনাগাদ হয়েছে!", Toast.LENGTH_SHORT).show();
    }
  }

  private void syncFailedInvoices() {
    String basicAuthorization = Credentials.basic(loginResponse.getData().getUserId(),
        preferences.getPassword());

    //for (int i = 0; i < failedInvoices.size(); i++) {
    InvoiceRequest invoiceRequest = failedInvoices.get(0);
    Call<ResponseBody> call = apiInterface.createInvoice(basicAuthorization, invoiceRequest);
    //int finalI = i;
    call.enqueue(new Callback<ResponseBody>() {
      @Override
      public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
          failedInvoices.remove(0);
        }
        if (failedInvoices.size() > 0) {
          syncFailedInvoices();
        } else {
          new RealMDatabaseManager().deleteAllInvoice();
          progress.dismiss();
          Toast.makeText(MainDashboardActivity.this, "ডাটা হালনাগাদ হয়েছে!", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<ResponseBody> call, Throwable t) {
        Log.e(TAG, "onFailure: ", t);
        //Toast.makeText(MainDashboardActivity.this, "ডাটা হালনাগাদ হয়নি! আবার চেষ্টা করুন", Toast.LENGTH_SHORT).show();
        /*if (finalI == failedInvoices.size() - 1) {
          if (failedInvoices.size() > 0) {
            new AlertDialog.Builder(MainDashboardActivity.this)
                .setMessage("ডাটা হালনাগাদ হয়নি! আবার চেষ্টা করুন")
                .setPositiveButton(android.R.string.ok, new android.content.DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(android.content.DialogInterface dialog, int which) {
                    closeSales();
                  }
                })
                .setCancelable(false)
                .show();
          } else {
            progress.dismiss();
            Toast.makeText(MainDashboardActivity.this, "ডাটা হালনাগাদ হয়েছে!", Toast.LENGTH_SHORT).show();
          }
        }*/
      }
    });
  }

  private void showProgress() {
    CustomProgress customProgress = new CustomProgress(this);
    customProgress.show("Loading...");
  }
}

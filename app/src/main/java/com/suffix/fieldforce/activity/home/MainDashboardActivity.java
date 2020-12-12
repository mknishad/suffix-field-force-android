package com.suffix.fieldforce.activity.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.bill.BillDashboardActivity;
import com.suffix.fieldforce.activity.chat.ChatDashboardActivity;
import com.suffix.fieldforce.activity.location.LocationListener;
import com.suffix.fieldforce.activity.roster.RosterManagementActivity;
import com.suffix.fieldforce.activity.task.TaskDashboard;
import com.suffix.fieldforce.activity.transport.TransportRequasitionListActivity;
import com.suffix.fieldforce.akg.activity.MemoListActivity;
import com.suffix.fieldforce.akg.activity.NotificationListActivity;
import com.suffix.fieldforce.akg.activity.SaleActivity;
import com.suffix.fieldforce.akg.activity.SlideCollectionActivity;
import com.suffix.fieldforce.akg.activity.StockActivity;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.database.RealmDatabseManagerInterface;
import com.suffix.fieldforce.akg.database.manager.RealMDatabaseManager;
import com.suffix.fieldforce.akg.database.manager.SyncManager;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.AttendenceRequest;
import com.suffix.fieldforce.akg.model.CustomerData;
import com.suffix.fieldforce.akg.model.InvoiceRequest;
import com.suffix.fieldforce.akg.model.product.CategoryModel;
import com.suffix.fieldforce.akg.model.product.ProductCategory;
import com.suffix.fieldforce.akg.service.AkgLocationService;
import com.suffix.fieldforce.akg.util.AkgConstants;
import com.suffix.fieldforce.akg.util.CustomProgress;
import com.suffix.fieldforce.akg.util.NetworkUtils;
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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainDashboardActivity extends AppCompatActivity {

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @BindView(R.id.imgUserProfile)
  ImageView imgUserProfile;

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

  //@BindView(R.id.layoutSiteMap)
  //LinearLayout layoutSiteMap;

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

  private static final int SETTINGS_REQUEST_CODE = 1000;
  private static final int PERMISSION_REQUEST_CODE = 34;
  private static final long UPDATE_INTERVAL = 10 * 60 * 1000;
  private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;

  private static final int LOCATION_JOB_ID = 1;
  private JobScheduler jobScheduler;

  private FieldForcePreferences preferences;
  private AkgApiInterface apiInterface;
  private AkgLoginResponse loginResponse;

  private ProgressDialog progress;
  private CustomProgress customProgress;

  @SuppressLint("RestrictedApi")
  @OnClick({R.id.layoutAttendance, R.id.layoutExit, R.id.layoutTask, R.id.layoutRoster,
      R.id.layoutBilling, R.id.layoutInventory, R.id.layoutChat, R.id.layoutGIS, R.id.layoutSync,
      R.id.layoutClosing, R.id.dam_collection_layout})
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
        if (!isDataAvailable()) {
          Toast.makeText(this, "আগে ডাটা সিংক করুন!", Toast.LENGTH_SHORT).show();
          return;
        }
        openSales();
        break;
      case R.id.dam_collection_layout:
        if (!isDataAvailable()) {
          Toast.makeText(this, "আগে ডাটা সিংক করুন!", Toast.LENGTH_SHORT).show();
          return;
        }
        openDampCollection();
        break;
      case R.id.layoutRoster:
        //openRoster();
        if (!isDataAvailable()) {
          Toast.makeText(this, "আগে ডাটা সিংক করুন!", Toast.LENGTH_SHORT).show();
          return;
        }
        openMemo();
        break;
      case R.id.layoutBilling:
        openBills();
        break;
      case R.id.layoutInventory:
        if (!isDataAvailable()) {
          Toast.makeText(this, "আগে ডাটা সিংক করুন!", Toast.LENGTH_SHORT).show();
          return;
        }
        openInventory();
        break;
      case R.id.layoutChat:
        openMessage();
        break;
//      case R.id.layoutSiteMap:
//        openTR();
//        break;
      case R.id.layoutGIS:
        if (!isDataAvailable()) {
          Toast.makeText(this, "আগে ডাটা সিংক করুন!", Toast.LENGTH_SHORT).show();
          return;
        }
        openGIS();
        break;
      case R.id.layoutSync:
        syncData();
        break;
      case R.id.layoutClosing:
        BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
            .setTitle("সেলস ক্লোজিং")
            .setMessage("দিনে শুধুমাত্র একবার সেলস ক্লোজ করতে পারবেন। সেলস ক্লোজ করার পর সব ডাটা মুছে যাবে। আপনি কি সেলস ক্লোজ করতে চান?")
            .setCancelable(false)
            .setPositiveButton("হ্যা", R.drawable.ic_tik, new BottomSheetMaterialDialog.OnClickListener() {
              @Override
              public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                closeSales();
                dialogInterface.dismiss();
              }
            })
            .setNegativeButton("না", R.drawable.ic_delete, new BottomSheetMaterialDialog.OnClickListener() {
              @Override
              public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                //Toast.makeText(getApplicationContext(), "Cancelled!", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
              }
            })
            .build();

        // Show Dialog
        mBottomSheetDialog.show();
        break;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_dashboard_type_two);
    ButterKnife.bind(this);

    setupToolbar();

    init();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  private void setupToolbar() {
    setSupportActionBar(toolbar);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, null));
    } else {
      toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    if (getSupportActionBar() != null) {
      getSupportActionBar().setTitle("");
      toolbar.getOverflowIcon().setColorFilter(getResources().getColor(R.color.color_white),
          PorterDuff.Mode.SRC_ATOP);
    }
  }

  private void init() {
    customProgress = new CustomProgress(this);
    preferences = new FieldForcePreferences(this);
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface.class);
    loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse.class);
    txtUserName.setText(loginResponse.getData().getUserName());
    jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
    checkLocationPermission();
  }

  private void checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED) {
      initLocationSettings();
    } else {
      ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
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
      /*if (preferences.getOnline()) {
        goOffline();
      } else {
        goOnline();
      }*/
      if (preferences.getOnline()) {
        startBackgroundService();
        txtUserAddress.setText(preferences.getAddress());
        badge.setBackground(ContextCompat.getDrawable(MainDashboardActivity.this, R.drawable.circular_badge_online));
      } else {
        stopBackGroundService();
        badge.setBackground(ContextCompat.getDrawable(MainDashboardActivity.this, R.drawable.circular_badge_offline));
      }
    }).addOnFailureListener(e -> {
      if (e instanceof ResolvableApiException) {
        try {
          ((ResolvableApiException) e).startResolutionForResult(this, SETTINGS_REQUEST_CODE);
        } catch (IntentSender.SendIntentException ex) {
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    Log.i(TAG, "onRequestPermissionResult");

    switch (requestCode) {
      case PERMISSION_REQUEST_CODE:
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          initLocationSettings();
        } else {
          checkLocationPermission();
        }
        break;
      case SETTINGS_REQUEST_CODE:
        if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
          if (preferences.getOnline()) {
            startBackgroundService();
            txtUserAddress.setText(preferences.getAddress());
            badge.setBackground(ContextCompat.getDrawable(MainDashboardActivity.this,
                R.drawable.circular_badge_online));
          } else {
            stopBackGroundService();
            badge.setBackground(ContextCompat.getDrawable(MainDashboardActivity.this,
                R.drawable.circular_badge_offline));
          }
        } else {
          initLocationSettings();
        }
        break;
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case SETTINGS_REQUEST_CODE:
        switch (resultCode) {
          case Activity.RESULT_OK:
            if (preferences.getOnline()) {
              startBackgroundService();
              txtUserAddress.setText(preferences.getAddress());
              badge.setBackground(ContextCompat.getDrawable(MainDashboardActivity.this,
                  R.drawable.circular_badge_online));
            } else {
              stopBackGroundService();
              badge.setBackground(ContextCompat.getDrawable(MainDashboardActivity.this,
                  R.drawable.circular_badge_offline));
            }
            break;
          case Activity.RESULT_CANCELED:
            initLocationSettings();
        }
    }
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

  private void showSnackbar(final String text) {
    View container = findViewById(R.id.userRelativeLayout);
    if (container != null) {
      Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
    }
  }

  @SuppressLint("RestrictedApi")
  private void geoAttendance() {

    BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
        .setTitle("ENTRY?")
        .setMessage("Are you sure want to make attendance?")
        .setCancelable(false)
        .setPositiveButton("Yes", R.drawable.ic_tik, new BottomSheetMaterialDialog.OnClickListener() {
          @Override
          public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
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
                      startBackgroundService();
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
          public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
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
          public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {

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
                      stopBackGroundService();
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
          public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
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
    intent.putExtra(AkgConstants.INVOICE_TYPE, AkgConstants.SALE);
    startActivity(intent);
  }

  public void openDampCollection() {
    Intent intent = new Intent(MainDashboardActivity.this, SaleActivity.class);
    intent.putExtra(AkgConstants.INVOICE_TYPE, AkgConstants.DAMP);
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
    Intent intent = new Intent(MainDashboardActivity.this, StockActivity.class);
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
    showProgress();
    Log.d("Realm", "Sync Data Called");

    SyncManager syncManager = new SyncManager(MainDashboardActivity.this);
    syncManager.getAllCustomer(new RealmDatabseManagerInterface.Sync() {
      @Override
      public void onSuccess() {
        customProgress.dismiss();
      }
    });
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
        new RealMDatabaseManager().deleteAllInvoice();
      }
    } else {
      Toast.makeText(this, "ডাটা হালনাগাদ হয়েছে!", Toast.LENGTH_SHORT).show();
      new RealMDatabaseManager().deleteAllInvoice();
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
    customProgress.show("Loading...");
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.menu_logout:
        //logout
        preferences.putLoginResponse("");
        preferences.putDistributor("");
        startActivity(new Intent(MainDashboardActivity.this, LoginActivity.class));
        finishAffinity();
        return true;
      case R.id.menu_notification:
        Intent intent = new Intent(MainDashboardActivity.this, NotificationListActivity.class);
        startActivity(intent);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private boolean isDataAvailable() {
    RealMDatabaseManager databaseManager = new RealMDatabaseManager();
    ProductCategory productCategory = databaseManager.prepareCategoryData();
    List<CustomerData> customerDataList = databaseManager.prepareCustomerData();
    List<CategoryModel> products = new ArrayList<>();
    try {
      if (productCategory.getCigarette().size() > 0) {
        products.addAll(productCategory.getCigarette());
      }
      if (productCategory.getBidi().size() > 0) {
        products.addAll(productCategory.getBidi());
      }
      if (productCategory.getMatch().size() > 0) {
        products.addAll(productCategory.getMatch());
      }
    } catch (Exception e) {
      return false;
    }

    Log.d(TAG, "isDataAvailable: customerDataList.size() = " + customerDataList.size() +
        "\nproducts.size() = " + products.size());

    if (customerDataList.size() <= 0 || products.size() <= 0) {
      return false;
    }
    return true;
  }

  public void startBackgroundService() {
    ComponentName componentName = new ComponentName(getApplicationContext(), AkgLocationService.class);
    //10 sec interval
    JobInfo jobInfo = new JobInfo.Builder(LOCATION_JOB_ID, componentName)
        .setMinimumLatency(10000) //10 sec interval
        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setRequiresCharging(false).build();
    jobScheduler.schedule(jobInfo);
  }

  public void stopBackGroundService() {
    jobScheduler.cancel(LOCATION_JOB_ID);
    stopService(new Intent(MainDashboardActivity.this, AkgLocationService.class));
  }
}

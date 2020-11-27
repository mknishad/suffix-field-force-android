package com.suffix.fieldforce.activity.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.shreyaspatil.MaterialDialog.BottomSheetMaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;
import com.suffix.fieldforce.BuildConfig;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.abul.NotificationListActivity;
import com.suffix.fieldforce.activity.bill.BillDashboardActivity;
import com.suffix.fieldforce.activity.chat.ChatDashboardActivity;
import com.suffix.fieldforce.activity.gis.CreateGISDataActivity;
import com.suffix.fieldforce.activity.inventory.InventoryDashboardActivity;
import com.suffix.fieldforce.activity.location.LocationListener;
import com.suffix.fieldforce.activity.roster.RosterManagementActivity;
import com.suffix.fieldforce.activity.task.TaskDashboard;
import com.suffix.fieldforce.activity.transport.TransportRequasitionListActivity;
import com.suffix.fieldforce.location.LocationUpdatesBroadcastReceiver;
import com.suffix.fieldforce.model.LocationResponse;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
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

  private static final String TAG = "MainDashboardActivity";

  private final String ENTRY_TYPE_IN = "I";
  private final String ENTRY_TYPE_OUT = "O";

  private static int REQUEST_CHECK_SETTINGS = 1000;
  private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
  private static final long UPDATE_INTERVAL = 10 * 60 * 1000;
  private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
  private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;

  private LocationRequest mLocationRequest;
  private GoogleApiClient mGoogleApiClient;

  private FieldForcePreferences preferences;
  private APIInterface apiInterface;

  @OnClick({R.id.layoutAttendance, R.id.layoutExit, R.id.layoutTask, R.id.layoutRoster, R.id.layoutBilling, R.id.layoutInventory, R.id.layoutChat, R.id.layoutSiteMap, R.id.layoutGIS, R.id.imgNotification})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.layoutAttendance:
        geoAttendance();
        break;
      case R.id.layoutExit:
        geoExit();
        break;
      case R.id.layoutTask:
        openTask();
        break;
      case R.id.layoutRoster:
        openRoster();
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
    apiInterface = APIClient.getApiClient().create(APIInterface.class);
    //txtUserName.setText(preferences.getUser().getUserName());

    initProgressBar();

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

  private void initProgressBar() {
//    progressBar.setOnClickListener(v -> {
//      initLocationSettings();
//      /*if (preferences.getOnline()) {
//        goOffline();
//      } else {
//        goOnline();
//      }*/
//    });
  }

  private void getDeviceLocation(LocationListener locationListener, String text) {
    try {
//            Task task = fusedLocationProviderClient.getLastLocation();
//            task.addOnCompleteListener(task1 -> {
//                if (task1.isSuccessful()) {
//                    if (task1 != null) {
//                        preferences.putLocation((Location) task1.getResult());
//                        new GetAddressTask(MainDashboardActivity.this).execute((Location) task1.getResult());
//                    } else {
//                        getDeviceLocation();
//                    }
//                } else {
//                    getDeviceLocation();
//                }
//            });
      SmartLocation.with(this).location()
          .oneFix()
          .start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {

              preferences.putLocation(location);
              locationListener.onLocationUpdate(location);
              SmartLocation.with(MainDashboardActivity.this).geocoding()
                  .reverse(location, new OnReverseGeocodingListener() {
                    @Override
                    public void onAddressResolved(Location location, List<Address> list) {

                      if (BuildConfig.DEBUG) {
                        //Toast.makeText(MainDashboardActivity.this, "Total Location : " + list.size(), Toast.LENGTH_SHORT).show();
                      }

                      if (list.size() > 0) {
                        Address result = list.get(0);
                        StringBuilder builder = new StringBuilder();
                        builder.append(text);
                        List<String> addressElements = new ArrayList<>();
                        for (int i = 0; i <= result.getMaxAddressLineIndex(); i++) {
                          addressElements.add(result.getAddressLine(i));
                        }
                        if (BuildConfig.DEBUG) {
                          //Toast.makeText(MainDashboardActivity.this, "Total AddressLine : " + result.getMaxAddressLineIndex(), Toast.LENGTH_SHORT).show();
                        }
                        builder.append(TextUtils.join(", ", addressElements));

                        SpannableString spannableString = new SpannableString(builder.toString());

                        if (text.length() > 0) {
                          if (text.toLowerCase().contains("entered")) {
                            Object bgGreenSpan = new BackgroundColorSpan(Color.parseColor("#6CBD6E"));
                            spannableString.setSpan(bgGreenSpan, 0, text.length() - 3, 0);
                          } else {
                            Object bgRed = new BackgroundColorSpan(Color.parseColor("#DA4453"));
                            spannableString.setSpan(bgRed, 0, text.length() - 3, 0);
                          }
                        }
                        txtUserAddress.setText(spannableString);
                      } else {
                        txtUserAddress.setText("No Address Found");
                      }
                    }
                  });
            }
          });
    } catch (SecurityException e) {
      Log.e(TAG, "getDeviceLocation: ", e);
    }
  }

  private void goOnline() {
//    progressBar.setForegroundStrokeColor(getResources().getColor(R.color.green));
//    progressBar.setProgressAnimationDuration(1000);
//    progressBar.setProgress(0f);
//    progressBar.setProgressAnimationDuration(1000);
//    progressBar.setProgress(100f);
    //getDeviceLocation("Entered : ");
    requestLocationUpdates();
//    txtUserStatus.setText(getResources().getString(R.string.entered));
//    txtUserStatus.setBackgroundColor(getResources().getColor(R.color.green));
    preferences.putOnline(true);
  }

  private void goOffline() {
//    progressBar.setProgressAnimationDuration(1000);
//    progressBar.setProgress(0f);
//    progressBar.setForegroundStrokeColor(getResources().getColor(R.color.colorGrapeFruit));
//    progressBar.setProgressAnimationDuration(1000);
//    progressBar.setProgress(100f);
    //getDeviceLocation("Exit : ");
    removeLocationUpdates();
//    txtUserStatus.setText(getResources().getString(R.string.exit));
//    txtUserStatus.setBackgroundColor(getResources().getColor(R.color.colorGrapeFruit));
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

                requestLocationUpdates();
                preferences.putOnline(true);

                Call<LocationResponse> attendanceEntry = apiInterface.attendanceEntry(
                    Constants.INSTANCE.KEY,
                    preferences.getUser().getUserId(),
                    String.valueOf(location.getLatitude()),
                    String.valueOf(location.getLongitude()),
                    ENTRY_TYPE_IN
                );
                attendanceEntry.enqueue(new Callback<LocationResponse>() {
                  @Override
                  public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                    if (response.isSuccessful()) {
                      LocationResponse locationResponse = response.body();
                      Toast.makeText(MainDashboardActivity.this, "Entered", Toast.LENGTH_SHORT).show();
                    }
                  }

                  @Override
                  public void onFailure(Call<LocationResponse> call, Throwable t) {
                    Toast.makeText(MainDashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    call.cancel();
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

  @SuppressLint("RestrictedApi")
  private void geoExit() {

    BottomSheetMaterialDialog mBottomSheetDialog = new BottomSheetMaterialDialog.Builder(this)
        .setTitle("EXIT?")
        .setMessage("Are you sure want to exit?")
        .setCancelable(false)
        .setPositiveButton("Yes", R.drawable.ic_tik, new BottomSheetMaterialDialog.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int which) {

            getDeviceLocation(new LocationListener() {
              @Override
              public void onLocationUpdate(Location location) {
                requestLocationUpdates();
                preferences.putOnline(true);

                Call<LocationResponse> attendanceEntry = apiInterface.attendanceEntry(
                    Constants.INSTANCE.KEY,
                    preferences.getUser().getUserId(),
                    String.valueOf(preferences.getLocation().getLatitude()),
                    String.valueOf(preferences.getLocation().getLongitude()),
                    ENTRY_TYPE_OUT
                );

                attendanceEntry.enqueue(new Callback<LocationResponse>() {
                  @Override
                  public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                    if (response.isSuccessful()) {
                      LocationResponse locationResponse = response.body();
                      Toast.makeText(MainDashboardActivity.this, "Entered : ", Toast.LENGTH_SHORT).show();
                    }
                  }

                  @Override
                  public void onFailure(Call<LocationResponse> call, Throwable t) {
                    Toast.makeText(MainDashboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    call.cancel();
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
    Intent intent = new Intent(MainDashboardActivity.this, CreateGISDataActivity.class);
    startActivity(intent);
  }

  private void openNotification() {
    Intent intent = new Intent(MainDashboardActivity.this, NotificationListActivity.class);
    startActivity(intent);
  }

}

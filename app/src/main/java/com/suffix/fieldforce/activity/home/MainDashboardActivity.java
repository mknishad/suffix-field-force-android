package com.suffix.fieldforce.activity.home;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.suffix.fieldforce.BuildConfig;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.bill.BillDashboardActivity;
import com.suffix.fieldforce.activity.chat.UserListActivity;
import com.suffix.fieldforce.activity.inventory.InventoryDashboardActivity;
import com.suffix.fieldforce.activity.roster.RosterManagementActivity;
import com.suffix.fieldforce.activity.task.TaskDashboard;
import com.suffix.fieldforce.location.LocationUpdatesBroadcastReceiver;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;

public class MainDashboardActivity extends AppCompatActivity implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

  private static final String TAG = "MainDashboardActivity";
  private static int REQUEST_CHECK_SETTINGS = 1000;
  private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
  private static final long UPDATE_INTERVAL = 10 * 1000;
  private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
  private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;

  private LocationRequest mLocationRequest;
  private GoogleApiClient mGoogleApiClient;

  private FieldForcePreferences preferences;

  @BindView(R.id.imgDrawer)
  ImageView imgDrawer;
  @BindView(R.id.imgActivation)
  ImageView imgActivation;
  @BindView(R.id.txtUserName)
  TextView txtUserName;
  @BindView(R.id.txtUserStatus)
  TextView txtUserStatus;
  @BindView(R.id.txtUserAddress)
  TextView txtUserAddress;
  @BindView(R.id.cardTask)
  CardView cardTask;
  @BindView(R.id.cardBills)
  CardView cardBills;
  @BindView(R.id.cardRosterManagement)
  CardView cardRosterManagement;
  @BindView(R.id.cardHistory)
  CardView cardHistory;
  @BindView(R.id.cardInventory)
  CardView cardInventory;
  @BindView(R.id.cardSiteMap)
  CardView cardSiteMap;
  @BindView(R.id.imgUserProfile)
  CircleImageView imgUserProfile;
  @BindView(R.id.progressBar)
  CircularProgressBar progressBar;

  @OnClick(R.id.cardTask)
  public void openTask() {
    Intent intent = new Intent(MainDashboardActivity.this, TaskDashboard.class);
    startActivity(intent);
  }

  @OnClick(R.id.cardRosterManagement)
  public void openRoster() {
    Intent intent = new Intent(MainDashboardActivity.this, RosterManagementActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.cardBills)
  public void openBills() {
    Intent intent = new Intent(MainDashboardActivity.this, BillDashboardActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.cardInventory)
  public void openInventory() {
    Intent intent = new Intent(MainDashboardActivity.this, InventoryDashboardActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.cardSiteMap)
  public void openMessage() {
    Intent intent = new Intent(MainDashboardActivity.this, UserListActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_dashboard);
    ButterKnife.bind(this);

    init();
  }

  private void init() {
    preferences = new FieldForcePreferences(this);
    txtUserName.setText(preferences.getUser().getUserName());

    initProgressBar();

    // Check if the user revoked runtime permissions.
    if (!checkPermissions()) {
      requestPermissions();
    }

    buildGoogleApiClient();
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
    progressBar.setOnClickListener(v -> {
      initLocationSettings();
      /*if (preferences.getOnline()) {
        goOffline();
      } else {
        goOnline();
      }*/
    });
  }

  private void getDeviceLocation(String text) {
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

              SmartLocation.with(MainDashboardActivity.this).geocoding()
                  .reverse(location, new OnReverseGeocodingListener() {
                    @Override
                    public void onAddressResolved(Location location, List<Address> list) {

                      if (BuildConfig.DEBUG) {
                        Toast.makeText(MainDashboardActivity.this, "Total Location : " + list.size(), Toast.LENGTH_SHORT).show();
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
                          Toast.makeText(MainDashboardActivity.this, "Total AddressLine : " + result.getMaxAddressLineIndex(), Toast.LENGTH_SHORT).show();
                        }
                        builder.append(TextUtils.join(", ", addressElements));
                        txtUserAddress.setText(builder.toString());
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
    progressBar.setForegroundStrokeColor(getResources().getColor(R.color.green));
    progressBar.setProgressAnimationDuration(1000);
    progressBar.setProgress(0f);
    progressBar.setProgressAnimationDuration(1000);
    progressBar.setProgress(100f);
    getDeviceLocation("Entered : ");
    requestLocationUpdates();
    txtUserStatus.setText(getResources().getString(R.string.entered));
    txtUserStatus.setBackgroundColor(getResources().getColor(R.color.green));
    preferences.putOnline(true);
  }

  private void goOffline() {
    progressBar.setProgressAnimationDuration(1000);
    progressBar.setProgress(0f);
    progressBar.setForegroundStrokeColor(getResources().getColor(R.color.colorGrapeFruit));
    progressBar.setProgressAnimationDuration(1000);
    progressBar.setProgress(100f);
    getDeviceLocation("Exit : ");
    removeLocationUpdates();
    txtUserStatus.setText(getResources().getString(R.string.exit));
    txtUserStatus.setBackgroundColor(getResources().getColor(R.color.colorGrapeFruit));
    preferences.putOnline(false);
  }

  private void createLocationRequest() {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    // Sets the fastest rate for active location updates. This interval is exact, and your
    // application will never receive updates faster than this value.
    mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    // Sets the maximum time when batched location updates are delivered. Updates may be
    // delivered sooner than this interval.
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

    // Provide an additional rationale to the user. This would happen if the user denied the
    // request previously, but didn't check the "Don't ask again" checkbox.
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
      // Request permission. It's possible this can be auto answered if device policy
      // sets the permission in a given state or the user denied the permission
      // previously and checked "Never ask again".
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
        // If user interaction was interrupted, the permission request is cancelled and you
        // receive empty arrays.
        Log.i(TAG, "User interaction was cancelled.");
      } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        // Permission was granted. Kick off the process of building and connecting
        // GoogleApiClient.
        buildGoogleApiClient();
      } else {
        // Permission denied.

        // Notify the user via a SnackBar that they have rejected a core permission for the
        // app, which makes the Activity useless. In a real app, core permissions would
        // typically be best requested during a welcome-screen flow.

        // Additionally, it is important to remember that a permission might have been
        // rejected without asking the user for permission (device policy or "Never ask
        // again" prompts). Therefore, a user interface affordance is typically implemented
        // when permissions are denied. Otherwise, your app could appear unresponsive to
        // touches or interactions which have required permissions.
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
}

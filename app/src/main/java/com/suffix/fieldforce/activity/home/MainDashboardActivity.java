package com.suffix.fieldforce.activity.home;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.suffix.fieldforce.BuildConfig;
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.bill.BillDashboardActivity;
import com.suffix.fieldforce.activity.inventory.InventoryDashboardActivity;
import com.suffix.fieldforce.activity.roster.RosterManagementActivity;
import com.suffix.fieldforce.activity.task.TaskDashboard;
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
import de.hdodenhof.circleimageview.CircleImageView;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.OnReverseGeocodingListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDashboardActivity extends AppCompatActivity {

    private static final String TAG = "MainDashboardActivity";
    private static int REQUEST_CHECK_SETTINGS = 1000;
    private static int REQUEST_LOCATION_PERMISSION = 1001;

    private boolean isOnline;
    private boolean isLocationUpdateActive;
    private boolean isFirstOpen = true;

    private FieldForcePreferences preferences;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private APIInterface apiInterface;

    private static long UPDATE_INTERVAL = 10 * 1000;
    private static long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
    private static long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        ButterKnife.bind(this);

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isLocationUpdateActive) {
            stopLocationUpdate();
        }
    }

    private void init() {

        preferences = new FieldForcePreferences(this);
        apiInterface = APIClient.getApiClient().create(APIInterface.class);

        initLocationProvider();
        initProgressBar();
        initLocationSettings();
        txtUserName.setText(preferences.getUser().getUserName());
    }

    private void initLocationProvider() {
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    try {

                        preferences.putLocation(location);

                        Call<LocationResponse> call = apiInterface.sendGeoLocation(Constants.INSTANCE.getKEY(),
                                preferences.getUser().getUserId(),
                                String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude()));
                        call.enqueue(new Callback<LocationResponse>() {
                            @Override
                            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                                Log.d(TAG, "onResponse: " + response.toString());
                            }

                            @Override
                            public void onFailure(Call<LocationResponse> call, Throwable t) {
                                Log.e(TAG, "onFailure: " + t.toString(), t);
                            }
                        });

                    } catch (Exception e) {
                        Log.e(TAG, "onLocationResult: " + e.getMessage(), e);
                    }
                }
            }
        };
    }

    private void initProgressBar() {
        goOffline();
        progressBar.setOnClickListener(v -> {
            if (!isOnline) {
                initLocationSettings();
            } else {
                goOffline();
            }
        });
    }

    private void initLocationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getLocationPermission();
        } else {
            createLocationRequest();
        }
    }

    private void getLocationPermission() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED) {
            createLocationRequest();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    REQUEST_LOCATION_PERMISSION
            );
        }
    }

    private void createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(o -> {
            if (isFirstOpen) {
                getDeviceLocation("");
                isFirstOpen = false;
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
//                    .oneFix()
                    .start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {

                            preferences.putLocation(location);

                            SmartLocation.with(MainDashboardActivity.this).geocoding()
                                    .reverse(location, new OnReverseGeocodingListener() {
                                        @Override
                                        public void onAddressResolved(Location location, List<Address> list) {

                                            if (BuildConfig.DEBUG) {
                                                Toast.makeText(MainDashboardActivity.this, "Total Location : "+list.size(), Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(MainDashboardActivity.this, "Total AddressLine : "+result.getMaxAddressLineIndex(), Toast.LENGTH_SHORT).show();
                                                }
                                                builder.append(TextUtils.join(", ", addressElements));
                                                txtUserAddress.setText(builder.toString());
                                            }else{
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
        progressBar.setForegroundStrokeColor(getResources().getColor(R.color.colorTransparentTheme));
        progressBar.setProgressAnimationDuration(1000);
        progressBar.setProgress(0f);
        progressBar.setProgressAnimationDuration(1000);
        progressBar.setProgress(100f);
        isOnline = true;
        getDeviceLocation("Entered : ");
        startLocationUpdate();
        txtUserStatus.setText(getResources().getString(R.string.entered));
        txtUserStatus.setBackgroundColor(getResources().getColor(R.color.colorGrassDark));
    }

    private void goOffline() {
        progressBar.setProgressAnimationDuration(1000);
        progressBar.setProgress(0f);
        progressBar.setForegroundStrokeColor(getResources().getColor(R.color.colorGrapeFruit));
        progressBar.setProgressAnimationDuration(1000);
        progressBar.setProgress(100f);
        isOnline = false;
        getDeviceLocation("Exit : ");
        stopLocationUpdate();
        txtUserStatus.setText(getResources().getString(R.string.exit));
        txtUserStatus.setBackgroundColor(getResources().getColor(R.color.colorGrapeFruit));
    }

    private void startLocationUpdate() {
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                null
        );
        isLocationUpdateActive = true;
    }

    private void stopLocationUpdate() {
        SmartLocation.with(this).location().stop();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        isLocationUpdateActive = false;
    }
}

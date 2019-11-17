package com.suffix.fieldforce.activity.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.suffix.fieldforce.R;
import com.suffix.fieldforce.activity.bill.BillDashboardActivity;
import com.suffix.fieldforce.activity.task.TaskDashboard;
import com.suffix.fieldforce.model.LocationResponse;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainDashboard extends AppCompatActivity {

    private static final String TAG = "MainDashboard";
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

    private static long INTERVAL = 5 * 60 * 1000;
    private static long FASTEST_INTERVAL = 5 * 60 * 1000;

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
    @BindView(R.id.cardActivityLog)
    CardView cardActivityLog;
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
        Intent intent = new Intent(MainDashboard.this, TaskDashboard.class);
        startActivity(intent);
    }

    @OnClick(R.id.cardBills)
    public void openBills() {
        Intent intent = new Intent(MainDashboard.this, BillDashboardActivity.class);
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
    }

    private void initLocationProvider() {
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
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
                        new GetAddressTask(MainDashboard.this).execute(location);
                        Call<LocationResponse> call = apiInterface.sendGeoLocation(Constants.INSTANCE.getKEY(),
                                Constants.INSTANCE.getUSER_ID(),
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
        locationRequest.setInterval(INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(o -> {
            if (isFirstOpen) {
                getDeviceLocation();
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

    private void getDeviceLocation() {
        try {
            Task task = fusedLocationProviderClient.getLastLocation();
            task.addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    if (task1 != null) {
                        preferences.putLocation((Location) task1.getResult());
                        new GetAddressTask(MainDashboard.this).execute((Location) task1.getResult());
                    } else {
                        getDeviceLocation();
                    }
                } else {
                    getDeviceLocation();
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
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        isLocationUpdateActive = false;
    }

    private class GetAddressTask extends AsyncTask<Location, Void, String> {
        Context mContext;

        GetAddressTask(Context context) {
            super();
            mContext = context;
        }

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder =
                    new Geocoder(mContext, Locale.getDefault());
            Location loc = params[0];
            List<Address> addresses = new ArrayList<>();
            try {
                addresses = geocoder.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
            } catch (IOException e1) {
                Log.e("LocationSampleActivity",
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("IO Exception trying to get address");
            } catch (IllegalArgumentException e2) {
                String errorString = "Illegal arguments " +
                        Double.toString(loc.getLatitude()) +
                        " , " +
                        Double.toString(loc.getLongitude()) +
                        " passed to address service";
                Log.e("LocationSampleActivity", errorString);
                e2.printStackTrace();
                return errorString;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            } else {
                return "No address found";
            }
        }

        @Override
        protected void onPostExecute(String address) {
            if (address != null) {
                txtUserAddress.setText(address);
            }
        }
    }
}

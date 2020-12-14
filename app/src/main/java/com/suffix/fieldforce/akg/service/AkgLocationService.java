package com.suffix.fieldforce.akg.service;


import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.suffix.fieldforce.akg.api.AkgApiClient;
import com.suffix.fieldforce.akg.api.AkgApiInterface;
import com.suffix.fieldforce.akg.model.AkgLoginResponse;
import com.suffix.fieldforce.akg.model.GeoLocationRequest;
import com.suffix.fieldforce.akg.model.GlobalSettings;
import com.suffix.fieldforce.preference.FieldForcePreferences;

import okhttp3.Credentials;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by root on 12/2/18.
 */
@SuppressLint("NewApi")
public class AkgLocationService extends JobService implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener,
    ResultCallback<Status> {

  FieldForcePreferences preferences;
  AkgApiInterface apiInterface ;
  AkgLoginResponse loginResponse;
  String basicAuthorization;
  GeoLocationRequest geoLocationRequest;
  PowerManager.WakeLock wakeLock;

  /**
   * Update interval of location request
   */
  private int UPDATE_INTERVAL = 10 * 60 * 1000;

  /**
   * fastest possible interval of location request
   */
  private int FASTEST_INTERVAL = 10 * 60 * 1000;

  /**
   * The Job scheduler.
   */
  JobScheduler jobScheduler;

  /**
   * The Tag.
   */
  private static final String TAG = "AkgLocationService";

  /**
   * LocationRequest instance
   */
  private LocationRequest locationRequest;

  /**
   * GoogleApiClient instance
   */
  private GoogleApiClient googleApiClient;

  /**
   * Location instance
   */
  private Location lastLocation;

  @Override
  public void onCreate() {
    super.onCreate();

    preferences = new FieldForcePreferences(getApplicationContext());
    apiInterface = AkgApiClient.getApiClient().create(AkgApiInterface .class);
    loginResponse = new Gson().fromJson(preferences.getLoginResponse(),
        AkgLoginResponse .class);
    basicAuthorization = Credentials.basic(String.valueOf(loginResponse.getData().getUserId()),
        preferences.getPassword());

    for (GlobalSettings settings : loginResponse.getData().getGlobalSettingList()) {
      if (settings.getAttributeName().equalsIgnoreCase("GEO_SYNC_INTERVAL")) {
        UPDATE_INTERVAL = Integer.parseInt(settings.getAttributeValue()) * 60 * 1000;
        FASTEST_INTERVAL = Integer.parseInt(settings.getAttributeValue()) * 60 * 1000;
      }
    }
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    PowerManager manager = (PowerManager) getSystemService(POWER_SERVICE);
    wakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "akg:tag");
    wakeLock.acquire();
    return START_STICKY;
  }

  /**
   * Method is called when location is changed
   * @param location - location from fused location provider
   */
  @Override
  public void onLocationChanged(Location location) {
    Log.d(TAG, "onLocationChanged [" + location + "]");
    lastLocation = location;
    writeActualLocation(location);
  }

  /**
   * extract last location if location is not available
   */
  @SuppressLint("MissingPermission")
  private void getLastKnownLocation() {
    lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    if (lastLocation != null) {
      Log.d(TAG, "getLastKnownLocation: Lat: " + lastLocation.getLatitude() + " | Lng: " +
          lastLocation.getLongitude());
      writeLastLocation();
      postGeoLocationToServer(lastLocation);
    } else {
      Log.w(TAG, "No location retrieved yet");

      //here we can show Alert to start location
    }
    startLocationUpdates();
  }

  /**
   * this method writes location to text view or shared preferences
   * @param location - location from fused location provider
   */
  @SuppressLint("SetTextI18n")
  private void writeActualLocation(Location location) {
    //Log.d(TAG, location.getLatitude() + ", " + location.getLongitude());
    Log.d(TAG, "writeActualLocation: " + location.getLatitude() + ", " + location.getLongitude());
    //here in this method you can use web service or any other thing

  }

  /**
   * this method only provokes writeActualLocation().
   */
  private void writeLastLocation() {
    writeActualLocation(lastLocation);
  }


  /**
   * this method fetches location from fused location provider and passes to writeLastLocation
   */
  @SuppressLint("MissingPermission")
  private void startLocationUpdates() {
    Log.d(TAG, "startLocationUpdates()");
    locationRequest = LocationRequest.create()
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(UPDATE_INTERVAL/2)
        .setFastestInterval(FASTEST_INTERVAL/2);

    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
  }

  /**
   * Default method of service
   * @param params - JobParameters params
   * @return boolean
   */
  @Override
  public boolean onStartJob(JobParameters params) {
    startJobAgain();

    createGoogleApi();

    return false;
  }

  /**
   * Create google api instance
   */
  private void createGoogleApi() {
    //Log.d(TAG, "createGoogleApi()");
    if (googleApiClient == null) {
      googleApiClient = new GoogleApiClient.Builder(this)
          .addConnectionCallbacks(this)
          .addOnConnectionFailedListener(this)
          .addApi(LocationServices.API)
          .build();
    }

    //connect google api
    googleApiClient.connect();
  }

  /**
   * disconnect google api
   * @param params - JobParameters params
   * @return result
   */
  @Override
  public boolean onStopJob(JobParameters params) {
    Log.d(TAG, "Job Stopped");
    googleApiClient.disconnect();
    return false;
  }

  /**
   * starting job again
   */
  private void startJobAgain() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      Log.d(TAG, "Job Started");
      ComponentName componentName = new ComponentName(getApplicationContext(),
          AkgLocationService.class);
      jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
      JobInfo jobInfo = new JobInfo.Builder(1, componentName)
          .setMinimumLatency(UPDATE_INTERVAL) //50 sec interval (finally set it to UPDATE_INTERVAL)
          .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setRequiresCharging(false).build();
      jobScheduler.schedule(jobInfo);
    }
  }

  /**
   * this method tells whether google api client connected.
   * @param bundle - to get api instance
   */
  @Override
  public void onConnected(@Nullable Bundle bundle) {
    Log.d(TAG, "onConnected()");
    getLastKnownLocation();
  }

  /**
   * this method returns whether connection is suspended
   * @param i - 0/1
   */
  @Override
  public void onConnectionSuspended(int i) {
    Log.d(TAG,"connection suspended");
  }

  /**
   * this method checks connection status
   * @param connectionResult - connected or failed
   */
  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    Log.d(TAG,"connection failed");
  }

  /**
   * this method tells the result of status of google api client
   * @param status - success or failure
   */
  @Override
  public void onResult(@NonNull Status status) {
    Log.d(TAG,"result of google api client : " + status);
  }

  @Override
  public void onDestroy() {
    Log.d(TAG, "onDestroy: ");
    super.onDestroy();
    if (wakeLock != null) {
      wakeLock.release();
    }
  }

  private void postGeoLocationToServer(Location location) {
    geoLocationRequest = new GeoLocationRequest(location.getLatitude(), location.getLongitude(),
        loginResponse.getData().getId(), System.currentTimeMillis());
    apiInterface.postGeoLocation(basicAuthorization, geoLocationRequest)
        .enqueue(new Callback<ResponseBody>() {
          @Override
          public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Log.d(TAG, "onResponse: response.body() = " + response.body());
          }

          @Override
          public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.e(TAG, "onFailure: ", t);
          }
        });
  }
}
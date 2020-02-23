/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.suffix.fieldforce.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.location.LocationResult;
import com.suffix.fieldforce.model.LocationResponse;
import com.suffix.fieldforce.preference.FieldForcePreferences;
import com.suffix.fieldforce.retrofitapi.APIClient;
import com.suffix.fieldforce.retrofitapi.APIInterface;
import com.suffix.fieldforce.util.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Receiver for handling location updates.
 * <p>
 * For apps targeting API level O
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)} should be used when
 * requesting location updates. Due to limits on background services,
 * {@link android.app.PendingIntent#getService(Context, int, Intent, int)} should not be used.
 * <p>
 * Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 * less frequently than the interval specified in the
 * {@link com.google.android.gms.location.LocationRequest} when the app is no longer in the
 * foreground.
 */
public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
  private static final String TAG = "LUBroadcastReceiver";

  public static final String ACTION_PROCESS_UPDATES =
      "com.google.android.gms.location.sample.backgroundlocationupdates.action" +
          ".PROCESS_UPDATES";

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();
      if (ACTION_PROCESS_UPDATES.equals(action)) {
        LocationResult result = LocationResult.extractResult(intent);
        if (result != null) {
          List<Location> locations = result.getLocations();
          //LocationResultHelper locationResultHelper = new LocationResultHelper(context, locations);
          // Save the location data to SharedPreferences.
          //locationResultHelper.saveResults();
          // Show notification with the location data.
          //locationResultHelper.showNotification();
          //Log.i(TAG, LocationResultHelper.getSavedLocationResult(context));

          if (!locations.isEmpty()) {
            APIInterface apiInterface = APIClient.getApiClient().create(APIInterface.class);
            Call<LocationResponse> call = apiInterface.sendGeoLocation(
                Constants.KEY,
                new FieldForcePreferences(context).getUser().getUserId(),
                String.valueOf(locations.get(0).getLatitude()),
                String.valueOf(locations.get(0).getLongitude()));
            call.enqueue(new Callback<LocationResponse>() {
              @Override
              public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()) {
                  if (response.body().getResponseCode().equals("1")) {
                    Log.d(TAG, "onResponse: Location sent to server");
                  }
                }
              }

              @Override
              public void onFailure(Call<LocationResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage(), t);
              }
            });
          }
        }
      }
    }
  }
}

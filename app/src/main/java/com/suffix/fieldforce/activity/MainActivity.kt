package com.suffix.fieldforce.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.suffix.fieldforce.R
import com.suffix.fieldforce.fragment.HomeFragment
import com.suffix.fieldforce.preference.FieldForcePreferences
import com.suffix.fieldforce.util.Constants
import com.suffix.fieldforce.viewmodel.MainViewModel
import devlight.io.library.ntb.NavigationTabBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.error
import org.jetbrains.anko.info
import java.util.*

const val REQUEST_CHECK_SETTINGS = 1000
const val REQUEST_LOCATION_PERMISSION = 1001

class MainActivity : AppCompatActivity(), AnkoLogger {

    private var online: Boolean = false
    private var locationUpdateActive: Boolean = false
    private var openApp: Boolean = true

    private lateinit var mPreferences: FieldForcePreferences
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mLocationRequest: LocationRequest

    private lateinit var mLocationCallback: LocationCallback
    private val INTERVAL = (60 * 1000).toLong()
    private val FASTEST_INTERVAL = (60 * 1000).toLong()

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_holder,
            HomeFragment()
        ).commit()

        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationUpdateActive) {
            stopLocationUpdate()
        }
    }

    private fun init() {
        mPreferences = FieldForcePreferences(this)

        initBottomNav()
        initLocationProvider()
        initProgressBar()
        initLocationSettings()
    }

    private fun initBottomNav() {
        val colors = resources.getStringArray(R.array.default_preview)

        val models = ArrayList<NavigationTabBar.Model>()
        models.add(
            NavigationTabBar.Model.Builder(
                resources.getDrawable(R.drawable.ic_first),
                Color.parseColor(colors[0])
            )
                .selectedIcon(resources.getDrawable(R.drawable.ic_sixth))
                .title("Heart")
                .badgeTitle("NTB")
                .build()
        )
        models.add(
            NavigationTabBar.Model.Builder(
                resources.getDrawable(R.drawable.ic_second),
                Color.parseColor(colors[1])
            )
                .selectedIcon(resources.getDrawable(R.drawable.ic_eighth))
                .title("Cup")
                .badgeTitle("with")
                .build()
        )
        models.add(
            NavigationTabBar.Model.Builder(
                resources.getDrawable(R.drawable.ic_third),
                Color.parseColor(colors[2])
            )
                .selectedIcon(resources.getDrawable(R.drawable.ic_seventh))
                .title("Diploma")
                .badgeTitle("state")
                .build()
        )
        models.add(
            NavigationTabBar.Model.Builder(
                resources.getDrawable(R.drawable.ic_fourth),
                Color.parseColor(colors[3])
            )
                .selectedIcon(resources.getDrawable(R.drawable.ic_eighth))
                .title("Flag")
                .badgeTitle("icon")
                .build()
        )
        models.add(
            NavigationTabBar.Model.Builder(
                resources.getDrawable(R.drawable.ic_fifth),
                Color.parseColor(colors[4])
            )
                .selectedIcon(resources.getDrawable(R.drawable.ic_eighth))
                .title("Medal")
                .badgeTitle("777")
                .build()
        )

        ntb!!.models = models
    }

    private fun initLocationProvider() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        mLocationRequest = LocationRequest()
        mLocationRequest.interval = INTERVAL
        mLocationRequest.fastestInterval = FASTEST_INTERVAL
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                info { "onLocationResult" }
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    //Update user location
                    try {
                        info { "${location.latitude} ${location.longitude}" }
                        mPreferences.putLocation(location)
                        viewModel.sendGeoLocation(
                            Constants.KEY,
                            "BLA0010",
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    } catch (e: Exception) {
                        error(e.message, e)
                    }
                }
            }
        }
    }

    private fun initProgressBar() {
        goOffline()
        progressBar.setOnClickListener {
            if (!online) {
                initLocationSettings()
            } else {
                goOffline()
            }
        }
    }

    private fun initLocationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getLocationPermission()
        } else {
            createLocationRequest()
        }
    }

    private fun getLocationPermission() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            createLocationRequest()
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION)
        }
    }

    private fun createLocationRequest() {
        val locationRequest = LocationRequest()
        locationRequest.interval = 10 * 60 * 1000
        locationRequest.fastestInterval = 5 * 60 * 1000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            if (openApp) {
                getDeviceLocation()
                openApp = false
            } else {
                goOnline()
            }
        }

        task.addOnFailureListener(this) { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this, REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getDeviceLocation() {
        debug("getDeviceLocation: ")
        try {
            val locationTask = mFusedLocationProviderClient.lastLocation
            locationTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    debug("onComplete: location found")
                    task.result?.let {
                        mPreferences.putLocation(it)
                    }
                } else {
                    error("onComplete: current location is null")
                    getDeviceLocation()
                }
            }
        } catch (e: SecurityException) {
            error("getDeviceLocation: SecurityException: " + e.message, e)
        }
    }

    private fun goOnline() {
        progressBar.foregroundStrokeColor =
            resources.getColor(android.R.color.holo_green_dark)
        progressBar.progressAnimationDuration = 1000
        progressBar.progress = 0f
        progressBar.progressAnimationDuration = 1000
        progressBar.progress = 100f
        online = true
        startLocationUpdate()
    }

    private fun goOffline() {
        progressBar.progressAnimationDuration = 1000
        progressBar.progress = 0f
        progressBar.foregroundStrokeColor =
            resources.getColor(android.R.color.holo_red_light)
        progressBar.progressAnimationDuration = 1000
        progressBar.progress = 100f
        online = false
        stopLocationUpdate()
    }

    private fun startLocationUpdate() {
        mFusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            null
        )
        locationUpdateActive = true
    }

    private fun stopLocationUpdate() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
        locationUpdateActive = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createLocationRequest()
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                if (openApp) {
                    getDeviceLocation()
                    openApp = false
                } else {
                    goOnline()
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}

package com.suffix.fieldforce.preference

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import com.google.gson.Gson
import com.suffix.fieldforce.util.Constants


/**
 * Created by invar on 07-Nov-17.
 */

const val PREFERENCE_TITLE = "FieldForcePreferences"

class FieldForcePreferences(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun putLocation(location: Location) {
        val gson = Gson()
        val json = gson.toJson(location)
        editor.putString(Constants.LOCATION, json)
        editor.apply()
    }

    fun getLocation(): Location {
        val gson = Gson()
        val json = sharedPreferences.getString(Constants.LOCATION, "")
        return gson.fromJson<Location>(json, Location::class.java)
    }
}

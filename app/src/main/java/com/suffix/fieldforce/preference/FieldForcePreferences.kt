package com.suffix.fieldforce.preference

import android.content.Context
import android.content.SharedPreferences
import android.location.Location
import com.google.gson.Gson
import com.suffix.fieldforce.model.User
import com.suffix.fieldforce.util.Constants


/**
 * Created by invar on 07-Nov-17.
 */

const val PREFERENCE_TITLE = "FieldForcePreferences"

class FieldForcePreferences(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_TITLE, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()

    fun putLocation(location: Location?) {
        val gson = Gson()
        val json = gson.toJson(location)
        editor.putString(Constants.LOCATION, json)
        editor.apply()
    }

    fun getLocation(): Location {
        val gson = Gson()
        val json = preferences.getString(Constants.LOCATION, "")
        return gson.fromJson<Location>(json, Location::class.java)
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean(Constants.FIRST_TIME, isFirstTime)
        editor.apply()
    }

    fun isFirstTimeLaunch(): Boolean {
        return preferences.getBoolean(Constants.FIRST_TIME, true)
    }

    fun putPushToken(token: String) {
        editor.putString(Constants.PUSH_TOKEN, token);
        editor.apply()
    }

    fun getPushToken(): String? {
        return preferences.getString(Constants.PUSH_TOKEN, "")
    }

    fun putUser(user: User) {
        val gson = Gson()
        val json = gson.toJson(user)
        editor.putString(Constants.USER, json)
        editor.apply()
    }

    fun getUser(): User {
        val gson = Gson()
        val json = preferences.getString(Constants.USER, "")
        return gson.fromJson<User>(json, User::class.java)
    }
}

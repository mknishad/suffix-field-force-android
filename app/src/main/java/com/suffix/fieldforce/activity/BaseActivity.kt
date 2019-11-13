package com.suffix.fieldforce.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.suffix.fieldforce.preference.FieldForcePreferences

open class BaseActivity : AppCompatActivity() {

    protected lateinit var preferences: FieldForcePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferences = FieldForcePreferences(this)
    }
}

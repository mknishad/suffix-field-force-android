package com.suffix.fieldforce.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.suffix.fieldforce.preference.FieldForcePreferences
import org.jetbrains.anko.AnkoLogger

open class BaseActivity : AppCompatActivity(), AnkoLogger {

  protected lateinit var preferences: FieldForcePreferences

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    preferences = FieldForcePreferences(this)
  }
}

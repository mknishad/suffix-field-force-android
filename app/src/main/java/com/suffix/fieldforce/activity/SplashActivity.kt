package com.suffix.fieldforce.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.suffix.fieldforce.R
import com.suffix.fieldforce.activity.home.LoginActivity
import com.suffix.fieldforce.activity.home.MainDashboardActivity
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    if (Build.VERSION.SDK_INT >= 21) {
      window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    Handler().postDelayed({
      if (preferences.isFirstTimeLaunch()) {
        startActivity<WelcomeActivity>()
        preferences.setFirstTimeLaunch(false)
      } else {
        try {
          val loginResponse = preferences.getLoginResponse()
          Log.d(TAG, "onCreate: loginResponse = $loginResponse")
          if (!TextUtils.isEmpty(loginResponse)) {
            startActivity<MainDashboardActivity>()
          } else {
            startActivity<LoginActivity>()
          }
        } catch (e: Exception) {
          startActivity<LoginActivity>()
        }
      }

      finish()
    }, 1300)
  }

  companion object {
    private const val TAG = "SplashActivity"
  }
}

package com.suffix.fieldforce.activity

import android.os.Bundle
import android.os.Handler
import com.suffix.fieldforce.R
import com.suffix.fieldforce.activity.home.LoginActivity
import com.suffix.fieldforce.activity.home.MainDashboardActivity
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    Handler().postDelayed({
      if (preferences.isFirstTimeLaunch()) {
        startActivity<WelcomeActivity>()
        preferences.setFirstTimeLaunch(false)
      } else {
        try {
          val user = preferences.getUser()
          startActivity<MainDashboardActivity>()
        } catch (e: Exception) {
          startActivity<LoginActivity>()
        }
      }

      finish()
    }, 1300)
  }
}

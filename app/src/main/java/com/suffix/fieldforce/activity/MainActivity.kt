package com.suffix.fieldforce.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.suffix.fieldforce.R
import com.suffix.fieldforce.fragment.HomeFragment
import devlight.io.library.ntb.NavigationTabBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.header.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var online: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_holder,
            HomeFragment()
        ).commit()

        progressBar.setOnClickListener {
            if (!online) {
                progressBar.foregroundStrokeColor = resources.getColor(android.R.color.holo_green_dark)
                progressBar.progressAnimationDuration = 1000
                progressBar.progress = 0f
                progressBar.progressAnimationDuration = 1000
                progressBar.progress = 100f
                online = true
            } else {
                progressBar.progressAnimationDuration = 1000
                progressBar.progress = 0f
                progressBar.foregroundStrokeColor = resources.getColor(android.R.color.holo_red_light)
                progressBar.progressAnimationDuration = 1000
                progressBar.progress = 100f
                online = false
            }
        }

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

}

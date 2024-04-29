package com.yenaly.circularrevealswitch

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.yenaly.circularrevealswitch.Prefs.getPref
import com.yenaly.circularrevealswitch.Prefs.savePref
import com.yenaly.circularrevealswitch.Prefs.sharedPrefs
import com.yenaly.circularrevealswitch.demo.databinding.ActivityMainBinding
import com.yenaly.circularrevealswitch.ext.setDayNightModeSwitcher
import com.yenaly.circularrevealswitch.demo.R
import com.yenaly.circularrevealswitch.impl.ThemeCRSwitch

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        when (sharedPrefs().getString("theme", "default")) {
            "red" -> setTheme(R.style.Theme_CircularRevealSwitch_Red)
            "green" -> setTheme(R.style.Theme_CircularRevealSwitch_Green)
            "blue" -> setTheme(R.style.Theme_CircularRevealSwitch_Blue)
            "yellow" -> setTheme(R.style.Theme_CircularRevealSwitch_Yellow)
            else -> setTheme(R.style.Theme_CircularRevealSwitch)
        }

        AppCompatDelegate.setDefaultNightMode(
            if (applicationContext.getPref(
                    "night", false
                )
            ) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )


        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appbarMain.toolbar)

        binding.appbarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val button =
            navView.getHeaderView(0).findViewById<MaterialButton>(R.id.switch_day_night)
        if (isNightMode) button.setIconResource(R.drawable.baseline_wb_sunny_24)
        else button.setIconResource(R.drawable.baseline_mode_night_24)
        button.setDayNightModeSwitcher {
            applicationContext.savePref("night", !isNightMode)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
package com.example.mixmate2

import android.os.Bundle
import android.view.Menu
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mixmate2.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {



    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    public var userLoggedIn: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_player, R.id.nav_login, R.id.nav_register, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
//        getSupportActionBar()?.hide()
        supportFragmentManager
            .setFragmentResultListener("login", this) { requestKey, bundle ->
                // We use a String here, but any type that can be put in a Bundle is supported.
                val result = bundle.getBoolean("bundleKey2")
                val navigationView = findViewById<NavigationView>(R.id.nav_view)
                val nav_Menu = navigationView.menu

                if (result) {
                    nav_Menu.findItem(R.id.nav_login).setVisible(false)
                    nav_Menu.findItem(R.id.nav_register).setVisible(false)
                    nav_Menu.findItem(R.id.nav_logout).setVisible(true)
                }

                else {
                    nav_Menu.findItem(R.id.nav_login).setVisible(true)
                    nav_Menu.findItem(R.id.nav_register).setVisible(true)
                    nav_Menu.findItem(R.id.nav_logout).setVisible(false)
                    navController.navigate(R.id.nav_home)
                    Toast.makeText(this.applicationContext, "Logout Successful", Toast.LENGTH_SHORT).show()
                }


            }
    }
//        supportFragmentManager
//            .setFragmentResultListener("premium", this) { requestKey, bundle ->
//                // We use a String here, but any type that can be put in a Bundle is supported.
//                val result = bundle.getString(requestKey)
//
//                val navigationView = findViewById<NavigationView>(R.id.nav_view)
//                val nav_Menu = navigationView.menu
//                nav_Menu.findItem(R.id.nav_login).setVisible(false)
//                nav_Menu.findItem(R.id.nav_register).setVisible(false);
//
//
//            }


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
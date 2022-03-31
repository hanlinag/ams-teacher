package com.ucsm.tylersai.amsteacher.ui.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.ui.fragments.CheckInFragment
import com.ucsm.tylersai.amsteacher.ui.fragments.HomeFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var preferenceEditor: SharedPreferences.Editor

    private lateinit var imgProfile: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val LOCATION_REQUEST_CODE = 101
    private val READ_REQUEST_CODE = 102
    private val WRITE_REQUEST_CODE = 103

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences(getString(R.string.teacherPref), Context.MODE_PRIVATE)
        preferenceEditor = sharedPreferences.edit()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        displaySelectedFragment(HomeFragment())
        navView.setCheckedItem(R.id.nav_home)

        val navHeaderMain = navView.getHeaderView(0)

        imgProfile = navHeaderMain.findViewById(R.id.img_profile_head)
        tvName = navHeaderMain.findViewById(R.id.tv_name_head)
        tvEmail = navHeaderMain.findViewById(R.id.tv_email_head)

        tvName.text = sharedPreferences.getString(getString(R.string.prefname), null)
        tvEmail.text = sharedPreferences.getString(getString(R.string.prefemail), null)
        Glide.with(applicationContext).load(sharedPreferences.getString(getString(R.string.prefurl), null))
            .into(imgProfile)

        Log.d("Tyler", "link is: ${sharedPreferences.getString(getString(R.string.prefurl), null)}")


        fatchLastLocation()
    }

    private fun fatchLastLocation() {

        //location , storage r/w permission
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )

            return
        }
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_REQUEST_CODE
            )

            return
        }
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_REQUEST_CODE
            )

            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener {
            if (it != null) {

                preferenceEditor = sharedPreferences.edit()
                preferenceEditor.putString(getString(R.string.prefLat), "${it.latitude}")
                preferenceEditor.putString(getString(R.string.prefLon), "${it.longitude}")
                preferenceEditor.commit()
                Log.d("Tyler", "current location is : ${it.longitude} ${it.latitude}")
            }//end of if
        }
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.action_settings -> {
                val intent = Intent(this, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.action_my_acc -> {
                val intent = Intent(this, MyAccountActivity::class.java)
                startActivity(intent)
            }
        }
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
                val fragment = HomeFragment()
                displaySelectedFragment(fragment)

            }

            R.id.nav_check_in -> {
                val fragment = CheckInFragment()
                displaySelectedFragment(fragment)
                //Snackbar.make(findViewById(R.id.content_main), "Check In", Snackbar.LENGTH_LONG).show()
            }
            R.id.nav_about -> {

                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {

                val intent = Intent(this, ChangePasswordActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_logout -> {

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Confirm")
                    .setMessage("Are you sure?")
                    .setPositiveButton(
                        "Logout",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            // Snackbar.make(findViewById(R.id.content_main), "Yes pressed", Snackbar.LENGTH_LONG).show()
                            dialogInterface.dismiss()
                            preferenceEditor.clear()
                            preferenceEditor.commit()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        })
                    .setNegativeButton("Cancel") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    .show()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displaySelectedFragment(fragment: Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_main, fragment)
        fragmentTransaction.commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fatchLastLocation()
                }
            }
            WRITE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fatchLastLocation()
                }
            }
            READ_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fatchLastLocation()
                }
            }

        }
    }
}

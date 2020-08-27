package com.ucsm.tylersai.amsteacher

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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class Main2Activity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val LOCATION_REQUEST_CODE = 101
    private val CAMERA_REQUEST_CODE = 100
    private val READ_REQUEST_CODE = 102
    private val WRITE_REQUEST_CODE = 103

    lateinit var imgProfile: ImageView
    lateinit var tvName: TextView
    lateinit var tvEmail: TextView

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var preferenceEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "AMS Dean"

        sharedPreferences = getSharedPreferences(getString(R.string.teacherPref), Context.MODE_PRIVATE)
        preferenceEditor = sharedPreferences.edit()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val fab: FloatingActionButton = findViewById(R.id.fab2)

        fab.setOnClickListener { view ->
            var intent = Intent(this@Main2Activity, BroadcastActivity::class.java)
            startActivity(intent)

        }
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
        navView.setCheckedItem(R.id.nav_dashboard2)
        val navHeaderMain = navView.getHeaderView(0)
        fatchLastLocation()

        //set the default fragment
        val fragment = ViewStudentFragment()
        displaySelectedFragment(fragment)

        imgProfile = navHeaderMain.findViewById(R.id.img_profile_head2)
        tvName = navHeaderMain.findViewById(R.id.tv_name_head2)
        tvEmail = navHeaderMain.findViewById(R.id.tv_email_head2)

        tvName.text = sharedPreferences.getString(getString(R.string.prefname), null)
        tvEmail.text = sharedPreferences.getString(getString(R.string.prefemail), null)
        Glide.with(applicationContext).load(sharedPreferences.getString(getString(R.string.prefurl), null))
            .into(imgProfile)

    }


    private fun fatchLastLocation() {

        //location permission
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@Main2Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )

            return
        }
        //camera permission
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@Main2Activity,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )

            return
        }
        //storage r/w permission
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@Main2Activity,
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
                this@Main2Activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_REQUEST_CODE
            )

            return
        }
        var task = fusedLocationProviderClient!!.lastLocation
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
        menuInflater.inflate(R.menu.main2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action2_about -> {
                var intent = Intent(this@Main2Activity, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.action2_account -> {
                var intent = Intent(this@Main2Activity, MyAccountActivity::class.java)
                startActivity(intent)
            }
            R.id.action2_change_password -> {
                var intent = Intent(this@Main2Activity, ChangePasswordActivity::class.java)
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
            R.id.nav_dashboard2 -> {
                val fragment = ViewStudentFragment()
                displaySelectedFragment(fragment)

            }
            R.id.nav_check_in2 -> {
                val fragment = CheckInFragment()
                displaySelectedFragment(fragment)
            }
            /*R.id.nav_record2 -> {
                val fragment = RecordFragment()
                displaySelectedFragment(fragment)
        }*/
            R.id.nav_collect_attendance_2 -> {
                val fragment = CollectAttendanceFragment()
                displaySelectedFragment(fragment)
            }
            /*R.id.nav_view_student2 -> {
                val fragment = ViewStudentFragment()
                displaySelectedFragment(fragment)
        }*/
            R.id.nav_medical_leave2 -> {
                val fragment = MedicalLeaveFragment()
                displaySelectedFragment(fragment)
            }
            R.id.nav_add_student2 -> {
                val fragment = AddStudentFragment()
                displaySelectedFragment(fragment)
            }
            R.id.nav_add_teacher2 -> {
                val fragment = AddTeacherFragment()
                displaySelectedFragment(fragment)
            }
            R.id.nav_add_subject2 -> {

                val fragment = AddSubjectFragment()
                displaySelectedFragment(fragment)
            }
            R.id.nav_add_class2 -> {

                val fragment = AddClassFragment()
                displaySelectedFragment(fragment)
            }
            /*R.id.nav_export_csv2 -> {
                var intent = Intent(this@Main2Activity, ExportCSVActivity::class.java)
                startActivity(intent)
        }*/
            R.id.nav_about2 -> {
                var intent = Intent(this@Main2Activity, AboutActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_change_password2 -> {

                var intent = Intent(this@Main2Activity, ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_log_out2 -> {

                AlertDialog.Builder(this@Main2Activity)
                    .setTitle("Confirm")
                    .setMessage("Are you sure?")
                    .setPositiveButton("Logout", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                        preferenceEditor.clear()
                        preferenceEditor.commit()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })
                    .show()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun displaySelectedFragment(fragment: Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_main2, fragment)
        fragmentTransaction.commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fatchLastLocation()
                }
            }
            CAMERA_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fatchLastLocation()
                }
            }
            READ_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fatchLastLocation()
                }
            }

            WRITE_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fatchLastLocation()
                }
            }

        }
    }
}

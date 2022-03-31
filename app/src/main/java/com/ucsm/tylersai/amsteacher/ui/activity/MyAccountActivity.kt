package com.ucsm.tylersai.amsteacher.ui.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.Teacher

class MyAccountActivity : AppCompatActivity() {
    lateinit var imgProfile: ImageView
    lateinit var tvPhone: TextView
    lateinit var tvEmail: TextView
    lateinit var tvAddress: TextView
    lateinit var tvID: TextView

    lateinit var sharedPreferences: SharedPreferences
    lateinit var teacherTable: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_account)

        val dialog = ProgressDialog(this)
        dialog.setMessage("Please wait...")
        dialog.show()

        sharedPreferences = getSharedPreferences(getString(R.string.teacherPref), Context.MODE_PRIVATE)
        teacherTable = FirebaseDatabase.getInstance().reference.child("ams").child("teacher")

        imgProfile = findViewById(R.id.img_profile_myacc)
        tvPhone = findViewById(R.id.tv_phone_myacc)
        tvEmail = findViewById(R.id.tv_email_myacc)
        tvAddress = findViewById(R.id.tv_address_myacc)
        tvID = findViewById(R.id.tv_id_myacc)

        setSupportActionBar(findViewById(R.id.customized_toolbar))
        supportActionBar!!.title = sharedPreferences.getString(getString(R.string.prefname), null)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        sharedPreferences.getString(getString(R.string.prefid), null)?.let {
            teacherTable.child(it)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(
                            applicationContext,
                            "Error Occurred" + p0.toException().toString(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val teacher = dataSnapshot.getValue(Teacher::class.java)
                        tvPhone.text = teacher!!.phone
                        tvEmail.text = teacher.email
                        tvAddress.text = teacher.address
                        tvID.text = "ID - " + teacher.id

                        Glide.with(applicationContext).load(teacher.profileurl).into(imgProfile)
                        dialog.dismiss()

                    }

                })
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.action_my_acc_edit -> {
                val intent = Intent(this, UpdateMyInfoActivity::class.java)
                startActivity(intent)
            }
        }
        return item.let { super.onOptionsItemSelected(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_acc_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}

package com.ucsm.tylersai.amsteacher.ui.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.adapter.AddMedicalLeaveListViewAdapter
import com.ucsm.tylersai.amsteacher.model.MedicalLeave

class SeeAllStudentMedicalLeaveActivity : AppCompatActivity() {

    lateinit var listViewAllMedicalLeave: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_all_student_medical_leave)

        val progressDialog = ProgressDialog(this@SeeAllStudentMedicalLeaveActivity)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        supportActionBar!!.title = "Medical Leave"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        listViewAllMedicalLeave = findViewById(R.id.list_view_all_medical_leave_dean)


        val medicalLeaveArrayList = ArrayList<MedicalLeave>()

        //getting data from firebase
        val medicalLeaveTable =
            FirebaseDatabase.getInstance().reference.child("ams").child("medicalleave")
        medicalLeaveTable.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(
                    this@SeeAllStudentMedicalLeaveActivity,
                    "Error occurred ${p0.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                medicalLeaveArrayList.clear()
                if (dataSnapshot.exists()) {
                    //add to array list
                    dataSnapshot.children.forEach {
                        val medicalLeaveObj = it.getValue(MedicalLeave::class.java)
                        medicalLeaveArrayList.add(medicalLeaveObj!!)

                        //adapter
                        listViewAllMedicalLeave.adapter = AddMedicalLeaveListViewAdapter(
                            this@SeeAllStudentMedicalLeaveActivity,
                            medicalLeaveArrayList
                        )
                        progressDialog.dismiss()
                    }

                } else {
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@SeeAllStudentMedicalLeaveActivity,
                        "No data!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

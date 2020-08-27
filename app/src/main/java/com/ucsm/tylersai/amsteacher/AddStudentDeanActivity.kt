package com.ucsm.tylersai.amsteacher

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucsm.tylersai.amsteacher.model.PreStudentList

class AddStudentDeanActivity : AppCompatActivity() {

    lateinit var edtMkpt: EditText
    lateinit var edtName: EditText
    lateinit var spinnerMajor: Spinner
    lateinit var majorClassAry: ArrayList<String>

    var mkptStd: String = ""
    var nameStd: String = ""
    var majorStd: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student_dean)

        supportActionBar!!.title = "Add Eligible Student"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)

        edtMkpt = findViewById(R.id.edt_mkpt_add_student_dean)
        edtName = findViewById(R.id.edt_name_add_student_dean)
        spinnerMajor = findViewById(R.id.spinner_major_add_student_dean)
        majorClassAry = ArrayList<String>()
        majorClassAry.clear()

        //getting major data from fireabase
        var sectionClassTable = FirebaseDatabase.getInstance().reference.child("ams").child("sectionclass")
        sectionClassTable.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@AddStudentDeanActivity, "Error occurred ${p0.message}", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //get major class data
                dataSnapshot.children.forEach {
                    var majorName = it.key
                    majorClassAry.add(majorName!!)
                    var spinnerAdapter = ArrayAdapter<String>(
                        this@AddStudentDeanActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        majorClassAry
                    )
                    spinnerMajor.adapter = spinnerAdapter
                    var postition = spinnerAdapter.getPosition(majorClassAry[0])
                    spinnerMajor.setSelection(postition)
                    majorStd = majorClassAry[0]

                    //spinner listener
                    spinnerMajor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }

                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                            majorStd = spinnerMajor.selectedItem.toString()

                        }

                    }//end of spinner listener

                }
            }

        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_student_dean, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.menu_add_student_dean_add -> {
                mkptStd = edtMkpt.text.toString()
                nameStd = edtName.text.toString()

                if (mkptStd.isEmpty() || nameStd.isEmpty()) {
                    AlertDialog.Builder(this@AddStudentDeanActivity)
                        .setTitle("Error")
                        .setMessage("No information is filled! ")
                        .setPositiveButton(
                            "Ok",
                            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                        .show()
                } else if (mkptStd.length < 9 || !mkptStd.contains("mkpt-")) {

                    AlertDialog.Builder(this@AddStudentDeanActivity)
                        .setTitle("Error")
                        .setMessage("Wrong format. Correct format is mkpt-0000. Try again! ")
                        .setPositiveButton(
                            "Ok",
                            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                        .show()
                } else {
                    AlertDialog.Builder(this@AddStudentDeanActivity)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to add this student to database?")
                        .setPositiveButton("Add", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                            //trim and sub string of mkpt
                            var finalMkpt = mkptStd.substring(5)

                            var prestudentTable =
                                FirebaseDatabase.getInstance().reference.child("ams").child("prestudentlist")
                            //check if the student is already exist or not
                            prestudentTable.child(finalMkpt).addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Toast.makeText(
                                        this@AddStudentDeanActivity,
                                        "Error occurred ${p0.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    if (!dataSnapshot.exists()) {
                                        //adding pre-add student to the firebase database

                                        // Log.d("Tyler", "Here it is working.....")
                                        var prestudent = PreStudentList(finalMkpt, majorStd, nameStd)
                                        prestudentTable.child(finalMkpt).setValue(prestudent)
                                        Toast.makeText(
                                            this@AddStudentDeanActivity,
                                            "Student added to database!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        finish()
                                    } else {
                                        AlertDialog.Builder(this@AddStudentDeanActivity)
                                            .setTitle("Error")
                                            .setMessage("Student already exists! ")
                                            .setPositiveButton(
                                                "Ok",
                                                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                                            .show()
                                    }
                                }


                            })


                        })
                        .setNegativeButton(
                            "Cancel",
                            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                        .show()
                }

            }

            android.R.id.home -> {
                finish()
            }
        }


        return super.onOptionsItemSelected(item)
    }
}

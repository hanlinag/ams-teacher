package com.ucsm.tylersai.amsteacher.ui.activity

import android.app.ProgressDialog
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
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.Subject

class AddSubjectDeanActivity : AppCompatActivity() {

    private lateinit var edtSubjectCode: EditText
    private lateinit var edtSubjectName: EditText
    private lateinit var edtSubjectRoom: EditText
    private lateinit var edtSubjectTeacherId: EditText

    lateinit var spinnerDayOfWeek1: Spinner
    lateinit var spinnerStartHour1: Spinner
    lateinit var spinnerEndHour1: Spinner

    lateinit var spinnerDayOfWeek2: Spinner
    lateinit var spinnerStartHour2: Spinner
    lateinit var spinnerEndHour2: Spinner

    lateinit var spinnerDayOfWeek3: Spinner
    lateinit var spinnerStartHour3: Spinner
    lateinit var spinnerEndHour3: Spinner

    lateinit var spinnerDayOfWeek4: Spinner
    lateinit var spinnerStartHour4: Spinner
    lateinit var spinnerEndHour4: Spinner

    lateinit var spinnerDayOfWeek5: Spinner
    lateinit var spinnerStartHour5: Spinner
    lateinit var spinnerEndHour5: Spinner

    var subjectOfDayOfWeek1 = ""
    var startTime1 = ""
    var endTime1 = ""

    var subjectOfDayOfWeek2 = ""
    var startTime2 = ""
    var endTime2 = ""

    var subjectOfDayOfWeek3 = ""
    var startTime3 = ""
    var endTime3 = ""

    var subjectOfDayOfWeek4 = ""
    var startTime4 = ""
    var endTime4 = ""

    var subjectOfDayOfWeek5 = ""
    var startTime5 = ""
    var endTime5 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_subject_dean)

        supportActionBar!!.title = "Add Subject"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        edtSubjectCode = findViewById(R.id.edt_subject_code_add_subject_dean)
        edtSubjectName = findViewById(R.id.edt_subject_name_add_subject_dean)
        edtSubjectRoom = findViewById(R.id.edt_subject_room_add_subject_dean)
        edtSubjectTeacherId = findViewById(R.id.edt_subject_teacher_id_add_subject_dean)

        spinnerDayOfWeek1 = findViewById(R.id.spinner_subject_day_add_subject_dean1)
        spinnerStartHour1 = findViewById(R.id.spinner_subject_start_time_add_subject_dean1)
        spinnerEndHour1 = findViewById(R.id.spinner_subject_end_time_add_subject_dean1)

        val spinnerDayofWeekAdapter1 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.day_of_week,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerDayofWeekAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDayOfWeek1.adapter = spinnerDayofWeekAdapter1
        spinnerDayOfWeek1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                subjectOfDayOfWeek1 = spinnerDayOfWeek1.selectedItem.toString()
            }

        }//end of listener


        val spinnerStartHourAdapter1 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.time_of_day,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerStartHourAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStartHour1.adapter = spinnerStartHourAdapter1
        spinnerStartHour1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                startTime1 = spinnerStartHour1.selectedItem.toString()
            }

        }//end of listener


        val spinnerEndHourAdapter1 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.time_of_day,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerEndHourAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEndHour1.adapter = spinnerEndHourAdapter1
        spinnerEndHour1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                endTime1 = spinnerEndHour1.selectedItem.toString()
            }

        }//end of listener

//--------------------------------------

        spinnerDayOfWeek2 = findViewById(R.id.spinner_subject_day_add_subject_dean2)
        spinnerStartHour2 = findViewById(R.id.spinner_subject_start_time_add_subject_dean2)
        spinnerEndHour2 = findViewById(R.id.spinner_subject_end_time_add_subject_dean2)

        val spinnerDayofWeekAdapter2 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.day_of_week,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerDayofWeekAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDayOfWeek2.adapter = spinnerDayofWeekAdapter2
        spinnerDayOfWeek2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                subjectOfDayOfWeek2 = spinnerDayOfWeek2.selectedItem.toString()
            }

        }//end of listener


        val spinnerStartHourAdapter2 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.time_of_day,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerStartHourAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStartHour2.adapter = spinnerStartHourAdapter2
        spinnerStartHour2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                startTime2 = spinnerStartHour2.selectedItem.toString()
            }

        }//end of listener


        val spinnerEndHourAdapter2 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.time_of_day,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerEndHourAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEndHour2.adapter = spinnerEndHourAdapter2
        spinnerEndHour2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                endTime2 = spinnerEndHour2.selectedItem.toString()
            }

        }//end of listener

//---------------------------------

        spinnerDayOfWeek3 = findViewById(R.id.spinner_subject_day_add_subject_dean3)
        spinnerStartHour3 = findViewById(R.id.spinner_subject_start_time_add_subject_dean3)
        spinnerEndHour3 = findViewById(R.id.spinner_subject_end_time_add_subject_dean3)

        val spinnerDayofWeekAdapter3 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.day_of_week,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerDayofWeekAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDayOfWeek3.adapter = spinnerDayofWeekAdapter3
        spinnerDayOfWeek3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                subjectOfDayOfWeek3 = spinnerDayOfWeek3.selectedItem.toString()
            }

        }//end of listener


        val spinnerStartHourAdapter3 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.time_of_day,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerStartHourAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStartHour3.adapter = spinnerStartHourAdapter3
        spinnerStartHour3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                startTime3 = spinnerStartHour3.selectedItem.toString()
            }

        }//end of listener


        val spinnerEndHourAdapter3 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.time_of_day,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerEndHourAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEndHour3.adapter = spinnerEndHourAdapter3
        spinnerEndHour3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                endTime3 = spinnerEndHour3.selectedItem.toString()
            }

        }//end of listener


        //---------------------------------

        spinnerDayOfWeek4 = findViewById(R.id.spinner_subject_day_add_subject_dean4)
        spinnerStartHour4 = findViewById(R.id.spinner_subject_start_time_add_subject_dean4)
        spinnerEndHour4 = findViewById(R.id.spinner_subject_end_time_add_subject_dean4)

        val spinnerDayofWeekAdapter4 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.day_of_week,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerDayofWeekAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDayOfWeek4.adapter = spinnerDayofWeekAdapter4
        spinnerDayOfWeek4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                subjectOfDayOfWeek4 = spinnerDayOfWeek4.selectedItem.toString()
            }

        }//end of listener


        val spinnerStartHourAdapter4 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.time_of_day,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerStartHourAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStartHour4.adapter = spinnerStartHourAdapter4
        spinnerStartHour4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                startTime4 = spinnerStartHour4.selectedItem.toString()
            }

        }//end of listener


        val spinnerEndHourAdapter4 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.time_of_day,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerEndHourAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEndHour4.adapter = spinnerEndHourAdapter4
        spinnerEndHour4.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                endTime4 = spinnerEndHour4.selectedItem.toString()
            }

        }//end of listener


        //--------------------------------

        spinnerDayOfWeek5 = findViewById(R.id.spinner_subject_day_add_subject_dean5)
        spinnerStartHour5 = findViewById(R.id.spinner_subject_start_time_add_subject_dean5)
        spinnerEndHour5 = findViewById(R.id.spinner_subject_end_time_add_subject_dean5)

        val spinnerDayofWeekAdapter5 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.day_of_week,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerDayofWeekAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDayOfWeek5.adapter = spinnerDayofWeekAdapter5
        spinnerDayOfWeek5.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                subjectOfDayOfWeek5 = spinnerDayOfWeek5.selectedItem.toString()
            }

        }//end of listener


        val spinnerStartHourAdapter5 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.time_of_day,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerStartHourAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStartHour5.adapter = spinnerStartHourAdapter5
        spinnerStartHour5.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                startTime5 = spinnerStartHour5.selectedItem.toString()
            }

        }//end of listener


        val spinnerEndHourAdapter5 = ArrayAdapter.createFromResource(
            applicationContext,
            R.array.time_of_day,
            android.R.layout.simple_expandable_list_item_1
        )
        spinnerEndHourAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEndHour5.adapter = spinnerEndHourAdapter5
        spinnerEndHour5.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                endTime5 = spinnerEndHour5.selectedItem.toString()
            }

        }//end of listener


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_subject_dean, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.menu_add_subject_dean_add -> {

                val subjectCode = edtSubjectCode.text.toString()
                val subjectName = edtSubjectName.text.toString()
                val subjectRoom = edtSubjectRoom.text.toString()
                val subjectTeacherId = edtSubjectTeacherId.text.toString()


                //check if empty
                if (subjectCode.isEmpty() || subjectName.isEmpty() || subjectRoom.isEmpty() || subjectTeacherId.isEmpty()) {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Form not complete!")
                        .setPositiveButton(
                            "Ok",
                            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                        .show()
                } else if (startTime1 == "0" && startTime2 == "0" && startTime3 == "0" && startTime4 == "0" && startTime5 == "0" && endTime1 == "0" && endTime2 == "0" && endTime3 == "0" && endTime4 == "0" && endTime5 == "0"
                ) {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Day & Time interval not selected!")
                        .setPositiveButton(
                            "Ok"
                        ) { dialogInterface, i -> dialogInterface.dismiss() }
                        .show()
                } else {
                    //do actual job

                    var finalday = ""
                    var finalTime = ""

                    if (startTime1 == "0" && endTime1 == "0") {
                        subjectOfDayOfWeek1 = ""
                        startTime1 = ""
                        endTime1 = ""
                    } else {
                        finalday = "$finalday, $subjectOfDayOfWeek1"
                        finalTime = "$finalTime, $startTime1-$endTime1"
                    }

                    if (startTime2 == "0" && endTime2 == "0") {
                        subjectOfDayOfWeek2 = ""
                        startTime2 = ""
                        endTime2 = ""
                    } else {
                        finalday = "$finalday, $subjectOfDayOfWeek2"
                        finalTime = "$finalTime, $startTime2-$endTime2"
                    }

                    if (startTime3 == "0" && endTime3 == "0") {
                        subjectOfDayOfWeek3 = ""
                        startTime3 = ""
                        endTime3 = ""
                    } else {
                        finalday = "$finalday, $subjectOfDayOfWeek3"
                        finalTime = "$finalTime, $startTime3-$endTime3"
                    }

                    if (startTime4 == "0" && endTime4 == "0") {
                        subjectOfDayOfWeek4 = ""
                        startTime4 = ""
                        endTime4 = ""
                    } else {
                        finalday = "$finalday, $subjectOfDayOfWeek4"
                        finalTime = "$finalTime, $startTime4-$endTime4"
                    }

                    if (startTime5 == "0" && endTime5 == "0") {
                        subjectOfDayOfWeek5 = ""
                        startTime5 = ""
                        endTime5 = ""
                    } else {
                        finalday = "$finalday, $subjectOfDayOfWeek5"
                        finalTime = "$finalTime, $startTime5-$endTime5"
                    }


                    finalday = finalday.substring(2)
                    finalTime = finalTime.substring(2)


                    AlertDialog.Builder(this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to add this subject to database?\nSubject Code: $subjectCode\nSubject Name: $subjectName\nRoom: $subjectRoom\nTeacher ID: $subjectTeacherId\nDay: $finalday\nTime: $finalTime")
                        .setNegativeButton(
                            "Cancel"
                        ) { dialogInterface, i -> dialogInterface.dismiss() }
                        .setPositiveButton("Add") { dialogInterface, i ->
                            dialogInterface.dismiss()
                            val progressDialog = ProgressDialog(this)
                            progressDialog.setMessage("Loading...")
                            progressDialog.show()


                            //subject table
                            val subjectTable = FirebaseDatabase.getInstance().reference.child("ams")
                                .child("subject")
                            subjectTable.child(subjectCode)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                        Toast.makeText(
                                            this@AddSubjectDeanActivity,
                                            "Error occurred ${p0.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (!dataSnapshot.exists()) {
                                            //add subject to database
                                            val subTable =
                                                FirebaseDatabase.getInstance().reference.child("ams")
                                                    .child("subject")
                                            val subjectObj = Subject(
                                                finalday,
                                                subjectName,
                                                subjectRoom,
                                                subjectCode,
                                                subjectTeacherId,
                                                finalTime
                                            )
                                            subTable.child(subjectCode).setValue(subjectObj)
                                            progressDialog.dismiss()
                                            Toast.makeText(
                                                this@AddSubjectDeanActivity,
                                                "Subject added to database!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            finish()

                                        } else {
                                            progressDialog.dismiss()
                                            Toast.makeText(
                                                this@AddSubjectDeanActivity,
                                                "Already exist!",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()
                                        }
                                    }

                                })

                        }
                        .show()

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

package com.ucsm.tylersai.amsteacher

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucsm.tylersai.amsteacher.model.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MedicalLeaveActionDeanActivity : AppCompatActivity() {

    var mkpt = ""
    var askingdate = ""
    var imgurl = ""
    var progress = ""
    var key = ""

    val DATEFORMAT = "dd-MM-YYYY HH:mm"

    private val IMAGE_DIRECTORY = "/AMSTeacher"

    lateinit var tvmkpt: TextView
    lateinit var tvname: TextView
    lateinit var tvprogress: TextView
    lateinit var tvaskingdate: TextView

    lateinit var imgMedicalLeaveLetter: ImageView

    lateinit var btAcceptMedicalLeaveActionDean: Button
    lateinit var btDeclineMedicalLeaveActionDean: Button

    var student: Student? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medical_leave_action_dean)

        var progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        supportActionBar!!.title = "Medical Leave Action"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)

        mkpt = intent.getStringExtra("mkpt")
        askingdate = intent.getStringExtra("askingdate")
        imgurl = intent.getStringExtra("imgurl")
        progress = intent.getStringExtra("progress")
        key = intent.getStringExtra("key")

        tvmkpt = findViewById(R.id.tv_mkpt_medical_leave_action_dean)
        tvname = findViewById(R.id.tv_name_medical_leave_action_dean)
        tvprogress = findViewById(R.id.tv_progress_medical_leave_action_dean)
        tvaskingdate = findViewById(R.id.tv_asking_date_medical_leave_action_dean)

        btAcceptMedicalLeaveActionDean = findViewById(R.id.bt_accept_medical_leave_dean_action)
        btDeclineMedicalLeaveActionDean = findViewById(R.id.bt_decline_medical_leave_dean_action)

        imgMedicalLeaveLetter = findViewById(R.id.img_medical_leave_letter_medical_action_dean)

        tvmkpt.text = "mkpt - " + mkpt
        tvprogress.text = "progress - " + progress
        tvaskingdate.text = "asking date - " + askingdate

        Glide.with(this@MedicalLeaveActionDeanActivity).load(imgurl).into(imgMedicalLeaveLetter)

        //get student name corresponding with mkpt
        var studentTable = FirebaseDatabase.getInstance().reference.child("ams").child("student")
        studentTable.child(mkpt).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@MedicalLeaveActionDeanActivity, "Error occurred ${p0.message}", Toast.LENGTH_LONG)
                    .show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                student = dataSnapshot.getValue(Student::class.java)
                tvname.text = student!!.name
                progressDialog.dismiss()
            }

        })

        btAcceptMedicalLeaveActionDean.setOnClickListener {

            //accept
            var pDialog = ProgressDialog(this@MedicalLeaveActionDeanActivity)
            pDialog.setMessage("Loading...")
            pDialog.show()
            //decline submitted medical leave
            var medicalleaveTable = FirebaseDatabase.getInstance().reference.child("ams").child("medicalleave")

            medicalleaveTable.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(
                        this@MedicalLeaveActionDeanActivity,
                        "Error occurred${p0.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {


                    var medicalLeaveObj = dataSnapshot.getValue(MedicalLeave::class.java)
                    //get date and get the corresponding subject in that day and add for attendance
                    var askingDate = medicalLeaveObj!!.askingDate
                    var askingDateAry = askingDate.split(",")



                    for (i in 0 until askingDateAry.size) {

                        if (askingDateAry[i].length > 5) {
                            //get date and check whether it's friday or monday or something
                            //get the current date of day of week
                            val DATEFORMATT = "dd-MM-yyyy"
                            var formatofDate = SimpleDateFormat(DATEFORMATT)
                            var dateForWeekDay = formatofDate.parse(askingDateAry[i])
                            var calendarObj = Calendar.getInstance()
                            calendarObj.time = dateForWeekDay

                            var weekofDay = ""
                            when (calendarObj.get(Calendar.DAY_OF_WEEK)) {

                                Calendar.MONDAY -> {
                                    weekofDay = "Monday"
                                }
                                Calendar.TUESDAY -> {
                                    weekofDay = "Tuesday"
                                }
                                Calendar.WEDNESDAY -> {
                                    weekofDay = "Wednesday"
                                }
                                Calendar.THURSDAY -> {
                                    weekofDay = "Thursday"
                                }
                                Calendar.FRIDAY -> {
                                    weekofDay = "Friday"
                                }
                                Calendar.SATURDAY -> {
                                    weekofDay = "Saturday"
                                }
                                Calendar.SUNDAY -> {
                                    weekofDay = "Sunday"
                                }
                            }//end of when


                            //get all the subject from database add into array list
                            var subjectAry = ArrayList<Subject>()
                            var eligibleSubjectAry = ArrayList<Subject>()
                            var subjectTable = FirebaseDatabase.getInstance().reference.child("ams").child("subject")
                            subjectTable.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Toast.makeText(
                                        this@MedicalLeaveActionDeanActivity,
                                        "Error occurred ${p0.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    dataSnapshot.children.forEach {
                                        subjectAry.add(it.getValue(Subject::class.java)!!)
                                    }

                                    //loop subject array and check the contain day of week
                                    for (j in 0 until subjectAry.size) {
                                        if (subjectAry[j].day.contains(weekofDay)) {
                                            eligibleSubjectAry.add(subjectAry[j])

                                        }//end of if
                                    }//end of for

                                    //loop eligible subject and check if a student is register or not. if so add attendance to db
                                    for (f in 0 until eligibleSubjectAry.size) {

                                        if (student!!.subject.contains(eligibleSubjectAry[f].subjectCode)) {

                                            var dateRaw = askingDateAry[i].split("-")
                                            var realDay = dateRaw[0]
                                            var realMonth = dateRaw[1]
                                            var year = dateRaw[2]


                                            var monthForAttendanceKey = realMonth
                                            var todayForAttendanceKey = "$realDay$realMonth$year"

                                            Log.d("Tyler", "real $monthForAttendanceKey , $todayForAttendanceKey")
                                            var attendanceTable = FirebaseDatabase.getInstance().reference.child("ams")
                                                .child("attendance")
                                            //var attendanceKey = "subcode$subjectCode"
                                            var attendance = Attendance(
                                                todayForAttendanceKey,
                                                "medical leave",
                                                mkpt,
                                                eligibleSubjectAry[f].subjectCode
                                            )
                                            attendanceTable.child(monthForAttendanceKey).child(todayForAttendanceKey)
                                                .child(eligibleSubjectAry[f].subjectCode).child(mkpt)
                                                .setValue(attendance)

                                            pDialog.dismiss()
                                        }//end of if
                                    }//end of for
                                }

                            })

                        }
                    }

                    //update progress
                    medicalLeaveObj.progress = "accepted"
                    //update progress
                    medicalleaveTable.child(key).setValue(medicalLeaveObj)
                    Toast.makeText(
                        this@MedicalLeaveActionDeanActivity,
                        "Medical leave letter of mkpt-$mkpt has been declined!",
                        Toast.LENGTH_LONG
                    ).show()

                    // today date
                    var dateFormat = SimpleDateFormat(DATEFORMAT)
                    var c = Calendar.getInstance().time
                    var today = dateFormat.format(c)
                    var hour = c.hours
                    var min = c.minutes

                    //sending notification
                    var noti = Notifications(
                        "Information",
                        "Your medical leave letter is accpeted by dean!",
                        "$today$hour$min",
                        "mkpt$mkpt$today",
                        mkpt
                    )
                    var notiTable = FirebaseDatabase.getInstance().reference.child("ams").child("notifications")
                    notiTable.child("mkpt$mkpt$today").setValue(noti)
                }//end of action

            })

        }

        btDeclineMedicalLeaveActionDean.setOnClickListener {
            var pDialog = ProgressDialog(this@MedicalLeaveActionDeanActivity)
            pDialog.setMessage("Loading...")
            pDialog.show()
            //decline submitted medical leave
            var medicalleaveTable = FirebaseDatabase.getInstance().reference.child("ams").child("medicalleave")

            medicalleaveTable.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(
                        this@MedicalLeaveActionDeanActivity,
                        "Error occurred${p0.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    var medicalLeaveObj = dataSnapshot.getValue(MedicalLeave::class.java)
                    medicalLeaveObj!!.progress = "declined"
                    //update progress
                    medicalleaveTable.child(key).setValue(medicalLeaveObj)
                    pDialog.dismiss()
                    Toast.makeText(
                        this@MedicalLeaveActionDeanActivity,
                        "Medical leave letter of mkpt-$mkpt has been declined!",
                        Toast.LENGTH_LONG
                    ).show()

                    // today date
                    var dateFormat = SimpleDateFormat(DATEFORMAT)
                    var c = Calendar.getInstance().time
                    var today = dateFormat.format(c)
                    var hour = c.hours
                    var min = c.minutes

                    //sending notification
                    var noti = Notifications(
                        "Information",
                        "Your medical leave letter is declined by dean!",
                        "$today$hour$min",
                        "mkpt$mkpt$today",
                        mkpt
                    )
                    var notiTable = FirebaseDatabase.getInstance().reference.child("ams").child("notifications")
                    notiTable.child("mkpt$mkpt$today").setValue(noti)
                }

            })

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.menu_save_picture_medical_action_dean -> {

                var drawable = imgMedicalLeaveLetter.drawable as BitmapDrawable
                var bitmap = drawable.bitmap
                saveImage(bitmap)
                Toast.makeText(this@MedicalLeaveActionDeanActivity, "Photo Saved!", Toast.LENGTH_LONG).show()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save_medical_leave_action_dean, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        var wallpaperDirectory = File(
            "${Environment.getExternalStorageDirectory()}$IMAGE_DIRECTORY"
        )
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs())
            wallpaperDirectory.mkdirs()
            wallpaperDirectory.createNewFile()
        }

        try {
            val f: File = File(
                wallpaperDirectory,
                "${Calendar.getInstance().timeInMillis}mkpt${mkpt}.jpg"
            )

            f.createNewFile()   //give read write permission
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            //Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.absolutePath

        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""

    }
}

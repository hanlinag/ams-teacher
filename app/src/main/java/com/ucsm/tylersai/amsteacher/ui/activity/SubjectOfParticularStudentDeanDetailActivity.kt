package com.ucsm.tylersai.amsteacher.ui.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.adapter.SubjectDetailListAdapter
import com.ucsm.tylersai.amsteacher.model.Attendance
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class SubjectOfParticularStudentDeanDetailActivity : AppCompatActivity() {

    private var tvmkpt: TextView? = null
    private var tvpercentSemester: TextView? = null
    private var tvmonthlypercent: TextView? = null
    private val SEMESTERMONTH = "05,06,07,08"
    private val DATEFORMATT = "dd-MM-yyyy"
    private val HOLIDAYS: String = "16-07-2019,19-07-2019"
    private var mkpt = ""

    private var attendancePercentage = 0.0

    private var may = "0"
    private var june = "0"
    private var july = "0"
    private var august = "0"


    lateinit var attendanceTable: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_of_particulat_student_dean_detail)

        setSupportActionBar(findViewById(R.id.subject_detail_customized_toolbar))

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.show()


        val code = intent.getStringExtra("subcode")
        //var name = intent.getStringExtra("subname")

        mkpt = intent.getStringExtra("mkpt").toString()
        val subDay = intent.getStringExtra("subdays")
        val subTime = intent.getStringExtra("subtimes")

        tvmkpt = findViewById<TextView>(R.id.detail_mkpt)
        tvpercentSemester = findViewById(R.id.detail_subject_percent)
        tvmonthlypercent = findViewById(R.id.detail_subject_monthly_percent)



        supportActionBar!!.title = "$code"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val listView = findViewById<ListView>(R.id.subject_detail_daily_list)

        val dataset = ArrayList<Attendance>()


        //getting all the data of the corresponding data from firebase database
        attendanceTable = FirebaseDatabase.getInstance().reference.child("ams").child("attendance")

        //loop for the specific month
        //get month of this sem
        val semMonthsAry = SEMESTERMONTH.split(",")
        for (element in semMonthsAry) {

            attendanceTable.child(element).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        p0.toException().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //day in db
                    //var dailyDate = ""
                    var mkptfromDB = ""
                    //var subjectCodefromDB = ""

                    //specific subject code
                    dataSnapshot.children.forEach {

                        //var dailyDate = it.key

                        //get subject
                        it.children.forEach {
                            val subjectCodefromDB = it.key
                            // Log.d("Tyler", "detail subject: dailydate is $dailyDate subject code is $subjectCodefromDB")
                            //check if it's the same subject code
                            if (subjectCodefromDB == code) {
                                it.children.forEach {
                                    mkptfromDB = it.key.toString()
                                    if (mkpt == mkptfromDB) {
                                        val attendance = it.getValue(Attendance::class.java)
                                        val day = attendance!!.date.subSequence(0, 2)
                                        val month = attendance.date.substring(2, 4)
                                        val year = attendance.date.substring(4)
                                        attendance.date = "$day-$month-$year"
                                        dataset.add(attendance)

                                        listView.adapter =
                                            SubjectDetailListAdapter(applicationContext, dataset)
                                        progressDialog.dismiss()
                                    }//end of if mkpt

                                }//end of it.children.for each

                            }//end of if
                        }//end of it.children for each

                    }//end of datasnapshot chilren for each
                }

            })
        }//end of for month

//---------------------------------------------------------------------------------------------------------second
        //calculate the total time period to attend at this semester
        val semMonthAry = SEMESTERMONTH.split(",")
        val startMonth = semMonthAry[0]

        val endMonth = semMonthAry[semMonthAry.lastIndex]

        var totalPeriodHourofSubjectinMonth = 0
        var totalPeroidHourofAttend = 0


        //start loop of 4 months here
        for (element in semMonthAry) {//whole month loop
            //Log.d("Tyler","start and end month : $startMonth, $endMonth")
            var numDaysInMonth = 30

            val dateFormat = SimpleDateFormat(DATEFORMATT)
            val c = Calendar.getInstance().time
            val year = Calendar.getInstance().get(Calendar.YEAR)
            //var today = dateFormat.format(c)

            //var rawSplit = today.split("-")
            //get the current month of loop and year
            val currentMonth = element
            val currentYear = year.toString()

            when (currentMonth.toInt()) {
                1 -> {
                    numDaysInMonth = 31
                }
                2 -> {

                    //cleck leap yar
                    numDaysInMonth = if (isLeapYear(currentYear.toInt())) {
                        29
                    } else {
                        28
                    }

                }
                3 -> {

                    numDaysInMonth = 31
                }
                4 -> {

                    numDaysInMonth = 30
                }
                5 -> {

                    numDaysInMonth = 31
                }
                6 -> {

                    numDaysInMonth = 30
                }
                7 -> {

                    numDaysInMonth = 31
                }
                8 -> {

                    numDaysInMonth = 31
                }
                9 -> {

                    numDaysInMonth = 30
                }
                10 -> {

                    numDaysInMonth = 31
                }
                11 -> {

                    numDaysInMonth = 30
                }
                12 -> {

                    numDaysInMonth = 31
                }
            }

            var startDateI = 1

            if (currentMonth == startMonth) {
                startDateI = 15
                //Log.d("Tyler","Starting from 15 May & num of day in month is $numDaysInMonth ")
            } else if (currentMonth == endMonth) {
                numDaysInMonth = 13
                // Log.d("Tyler","Starting from 1 but only 13 day")
            }


            // Log.d("Tyler", "Sem ary:: ${semMonthAry[b]}, number of days in month ${numDaysInMonth}, start date is : $startDateI")
            //get the total time period of this subject
            //get the subject data from firebase and get day

            //get current subject's day
            val dayOfSubRaw = subDay
            val dayOfSubAry = dayOfSubRaw?.split(",")


            //get the date of day of week of the current subject
            val eligibleSubDateToCheck = java.util.ArrayList<String>()
            val eligibleSubDateToCheckFinal = java.util.ArrayList<String>()

            for (i in startDateI until numDaysInMonth + 1) {//loop the whole month
                var eligible = false


                //check weather the current day is equal week of day of subject. And finally add date into array
                val dateToCheck = "$i-$currentMonth-$currentYear"
                Log.d("Tyler", "i is $i and date $dateToCheck")

                for (j in 0 until dayOfSubAry?.size!!) {//loop of subject teaching day
                    val subTeachingDayToCheck = dayOfSubAry.get(j)
                    //check if the current date of day of week is equal to subject day
                    val formatofDate = SimpleDateFormat(DATEFORMATT)
                    val dateForWeekDay = formatofDate.parse(dateToCheck)
                    val calendarObj = Calendar.getInstance()
                    calendarObj.time = dateForWeekDay

                    var checkWeekofDay = ""
                    when (calendarObj.get(Calendar.DAY_OF_WEEK)) {

                        Calendar.MONDAY -> {
                            checkWeekofDay = "Monday"
                        }
                        Calendar.TUESDAY -> {
                            checkWeekofDay = "Tuesday"
                        }
                        Calendar.WEDNESDAY -> {
                            checkWeekofDay = "Wednesday"
                        }
                        Calendar.THURSDAY -> {
                            checkWeekofDay = "Thursday"
                        }
                        Calendar.FRIDAY -> {
                            checkWeekofDay = "Friday"
                        }
                        Calendar.SATURDAY -> {
                            checkWeekofDay = "Saturday"
                        }
                        Calendar.SUNDAY -> {
                            checkWeekofDay = "Sunday"
                        }
                    }

                    if (checkWeekofDay == subTeachingDayToCheck) {

                        eligible = true

                        break
                        //Log.d("Tyler", "Eligibleeeee date is $dateToCheck and 16/19 ")
                    } else {
                        eligible = false
                        //Log.d("Tyler", "Check date is $dateToCheck and it's week of day is $checkWeekofDay and equal to holiday")
                    }

                }//end of subject teaching day for

                //add eligible to array
                if (eligible) {
                    eligibleSubDateToCheck.add(dateToCheck)
                } else {
                    Log.d("Tyler", "Detail Non eligible date is : $dateToCheck")
                }
            }//end of for

            //holiday
            val holidayAry = HOLIDAYS.split(",")

            //loop eligible date ary to re-filter holiday
            for (h in 0 until eligibleSubDateToCheck.size) {
                var e = false

                val firstEligibleDatetoCheck = eligibleSubDateToCheck[h]

                for (element in holidayAry) {
                    if (firstEligibleDatetoCheck == element) {

                        e = false
                        break
                    } else {
                        e = true
                    }

                }//end of holiday loop
                if (e) {
                    eligibleSubDateToCheckFinal.add(firstEligibleDatetoCheck)
                }

            }//end of first version of eligible date check
            //check holiday

            //get eligible date and check in attendance table in db whether it exist or not
            for (a in 0 until eligibleSubDateToCheckFinal.size) {
                //Log.d("Tyler", "Check date result::::: number of eligible date is: ${eligibleSubDateToCheck.size} and ${eligibleSubDateToCheck[a]} for subject ${items.get(position).name}" )
                var dateCheckAttendannce = eligibleSubDateToCheckFinal[a]


                //get the time period of subject of that day
                val timePeriod = subTime
                val dayofSubject = subDay

                val timeRaw = timePeriod!!.split(",")
                val dayRaw = dayofSubject!!.split(",")
                val formatofDate = SimpleDateFormat(DATEFORMATT)
                val c = Calendar.getInstance()

                val dateForWeekDay = formatofDate.parse(dateCheckAttendannce)
                c.time = dateForWeekDay

                var dayOfWeekofDate = ""
                when (c.get(Calendar.DAY_OF_WEEK)) {

                    Calendar.MONDAY -> {
                        dayOfWeekofDate = "Monday"
                    }
                    Calendar.TUESDAY -> {
                        dayOfWeekofDate = "Tuesday"
                    }
                    Calendar.WEDNESDAY -> {
                        dayOfWeekofDate = "Wednesday"
                    }
                    Calendar.THURSDAY -> {
                        dayOfWeekofDate = "Thursday"
                    }
                    Calendar.FRIDAY -> {
                        dayOfWeekofDate = "Friday"
                    }
                    Calendar.SATURDAY -> {
                        dayOfWeekofDate = "Saturday"
                    }
                    Calendar.SUNDAY -> {
                        dayOfWeekofDate = "Sunday"
                    }
                }

                var timeRangeofSubject = ""
                //check current loop date dayofweek
                for (g in dayRaw.indices) {
                    if (dayRaw[g] == dayOfWeekofDate) {
                        //get the time period of corresponding day of week
                        timeRangeofSubject = timeRaw[g]
                    }
                }//end of for
                val timeRangeofSubjectRaw = timeRangeofSubject.split("-")
                val startTime = timeRangeofSubjectRaw[0]
                val endTime = timeRangeofSubjectRaw[1]
                val periodHour = endTime.toInt() - startTime.toInt()

                totalPeriodHourofSubjectinMonth += periodHour

                //search the current date in db
                val attendanceTable =
                    FirebaseDatabase.getInstance().reference.child("ams").child("attendance")
                val monthofCheckDateRaw = dateCheckAttendannce.split("-")
                val monthofCheckDate = monthofCheckDateRaw[1]


                val dateR = dateCheckAttendannce.split("-")
                dateCheckAttendannce = "${dateR[0]}${dateR[1]}${dateR[2]}"
                if (dateR[0].toInt() < 10) {
                    dateCheckAttendannce = "0${dateR[0]}${dateR[1]}${dateR[2]}"
                }

                attendanceTable.child(monthofCheckDate).child(dateCheckAttendannce).child(code!!)
                    .child(mkpt)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(
                                this@SubjectOfParticularStudentDeanDetailActivity,
                                "Error occur" + p0.toException().toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            //Log.d("Tyler", "just checking for ${dataSnapshot.getValue(Attendance::class.java)!!.mkpt}")
                            if (dataSnapshot.exists()) {
                                //get period hour if attend
                                totalPeroidHourofAttend += periodHour

                                attendancePercentage =
                                    ((totalPeroidHourofAttend.toDouble() / totalPeriodHourofSubjectinMonth.toDouble()) * 100)

                                val decimalFormat = DecimalFormat("#.")
                                attendancePercentage = decimalFormat.format(attendancePercentage).toDouble()

                                Log.d(
                                    "Tyler",
                                    "Period Count in total: $totalPeriodHourofSubjectinMonth attend count $totalPeroidHourofAttend percentage: ${attendancePercentage} subject>> ${code}  the whole sem "
                                )

                                //holder?.tvSubjectPercent?.text = "$attendancePercentage% of 100% this month and still counting"


                                //update at UI title
                                tvmkpt?.text = "mkpt-$mkpt"
                                tvpercentSemester?.text = "$attendancePercentage% of 100% of this semester"
                            } else {
                                Log.d("Tyler", "No attendance for date $dateCheckAttendannce with subject code ${code}")
                            }


                        }//end of onDataChange
                    })
            }//end of for loop for eligible subject date
        }//whole month of this sem loop ending...


        //-------------------------------------------------------------------------------------------third
        //get percentage of each month in the semester
        //getting the percentage of this month of this subject
        //get the number of days in this month
        //get current day and month
        //start loop of 4 months here
        for (b in semMonthAry.indices) {//whole month loop
            // val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(DATEFORMATT)
            val c = Calendar.getInstance().time
            val today = dateFormat.format(c)
            val rawSplit = today.split("-")
            //var currentMonth = rawSplit[1]
            val currentYear = rawSplit[2]
            //Log.d("Tyler","Current day is $today and current month is $currentMonth and year $currentYear")

            val semMonthAryy = SEMESTERMONTH.split(",")
            val startMonth = semMonthAryy[0]

            val endMonth = semMonthAryy[semMonthAryy.lastIndex]

            //Log.d("Tyler","start and end month : $startMonth, $endMonth")
            var numDaysInMonth = 30

            when (semMonthAry[b].toInt()) {
                1 -> {
                    numDaysInMonth = 31
                }
                2 -> {

                    //cleck leap yar
                    if (isLeapYear(currentYear.toInt())) {
                        numDaysInMonth = 29
                    } else {
                        numDaysInMonth = 28
                    }

                }
                3 -> {

                    numDaysInMonth = 31
                }
                4 -> {

                    numDaysInMonth = 30
                }
                5 -> {

                    numDaysInMonth = 31
                }
                6 -> {

                    numDaysInMonth = 30
                }
                7 -> {

                    numDaysInMonth = 31
                }
                8 -> {

                    numDaysInMonth = 31
                }
                9 -> {

                    numDaysInMonth = 30
                }
                10 -> {

                    numDaysInMonth = 31
                }
                11 -> {

                    numDaysInMonth = 30
                }
                12 -> {

                    numDaysInMonth = 31
                }
            }

            var startDateI = 1

            if (semMonthAry[b] == startMonth) {
                startDateI = 15
                //Log.d("Tyler","Starting from 15 May & num of day in month is $numDaysInMonth ")
            } else if (semMonthAry[b].toString().equals(endMonth)) {
                numDaysInMonth = 13
                // Log.d("Tyler","Starting from 1 but only 13 day")
            }

            // Log.d("Tyler", "Current month's day in the month is $numDaysInMonth")

            //get current subject's day
            val dayOfSubRaw = subDay
            val dayOfSubAry = dayOfSubRaw!!.split(",")


            //get the date of day of week of the current subject
            val eligibleSubDateToCheck = java.util.ArrayList<String>()
            val eligibleSubDateToCheckFinal = java.util.ArrayList<String>()

            for (i in startDateI until numDaysInMonth + 1) {//loop the whole month
                var eligible = false

                //check weather the current day is equal week of day of subject. And finally add date into array
                val dateToCheck = "$i-${semMonthAry[b]}-$currentYear"
                Log.d("Tyler", "i is $i and date $dateToCheck")

                for (element in dayOfSubAry) {//loop of subject teaching day
                    val subTeachingDayToCheck = element
                    //check if the current date of day of week is equal to subject day
                    val formatofDate = SimpleDateFormat(DATEFORMATT)
                    val dateForWeekDay = formatofDate.parse(dateToCheck)
                    val calendarObj = Calendar.getInstance()
                    calendarObj.time = dateForWeekDay

                    var checkWeekofDay = ""
                    when (calendarObj.get(Calendar.DAY_OF_WEEK)) {

                        Calendar.MONDAY -> {
                            checkWeekofDay = "Monday"
                        }
                        Calendar.TUESDAY -> {
                            checkWeekofDay = "Tuesday"
                        }
                        Calendar.WEDNESDAY -> {
                            checkWeekofDay = "Wednesday"
                        }
                        Calendar.THURSDAY -> {
                            checkWeekofDay = "Thursday"
                        }
                        Calendar.FRIDAY -> {
                            checkWeekofDay = "Friday"
                        }
                        Calendar.SATURDAY -> {
                            checkWeekofDay = "Saturday"
                        }
                        Calendar.SUNDAY -> {
                            checkWeekofDay = "Sunday"
                        }
                    }

                    if (checkWeekofDay == subTeachingDayToCheck) {

                        eligible = true

                        break
                        //Log.d("Tyler", "Eligibleeeee date is $dateToCheck and 16/19 ")
                    } else {
                        eligible = false
                        //Log.d("Tyler", "Check date is $dateToCheck and it's week of day is $checkWeekofDay and equal to holiday")
                    }

                }//end of subject teaching day for

                //add eligible to array
                if (eligible) {
                    eligibleSubDateToCheck.add(dateToCheck)
                } else {
                    Log.d("Tyler", "Non eligible date is : $dateToCheck")
                }
            }//end of for

            //holiday
            val holidayAry = HOLIDAYS.split(",")

            //loop eligible date ary to re-filter holiday
            for (h in 0 until eligibleSubDateToCheck.size) {
                var e = false

                val firstEligibleDatetoCheck = eligibleSubDateToCheck[h]

                for (element in holidayAry) {
                    if (firstEligibleDatetoCheck == element) {

                        e = false
                        break
                    } else {
                        e = true
                    }

                }//end of holiday loop
                if (e) {
                    eligibleSubDateToCheckFinal.add(firstEligibleDatetoCheck)
                }

            }//end of first version of eligible date check
            //check holiday


            //get eligible date and check in attendance table in db whether it exist or not
            var totalPeriodHourofSubjectinMonth = 0
            var totalPeroidHourofAttend = 0


            for (a in 0 until eligibleSubDateToCheckFinal.size) {
                //Log.d("Tyler", "Check date result::::: number of eligible date is: ${eligibleSubDateToCheck.size} and ${eligibleSubDateToCheck[a]} for subject ${items.get(position).name}" )
                var dateCheckAttendannce = eligibleSubDateToCheckFinal[a]


                //get the time period of subject of that day
                val timePeriod = subTime
                val dayofSubject = subDay

                val timeRaw = timePeriod!!.split(",")
                val dayRaw = dayofSubject!!.split(",")
                val formatofDate = SimpleDateFormat(DATEFORMATT)
                val c = Calendar.getInstance()

                val dateForWeekDay = formatofDate.parse(dateCheckAttendannce)
                c.time = dateForWeekDay

                var dayOfWeekofDate = ""
                when (c.get(Calendar.DAY_OF_WEEK)) {

                    Calendar.MONDAY -> {
                        dayOfWeekofDate = "Monday"
                    }
                    Calendar.TUESDAY -> {
                        dayOfWeekofDate = "Tuesday"
                    }
                    Calendar.WEDNESDAY -> {
                        dayOfWeekofDate = "Wednesday"
                    }
                    Calendar.THURSDAY -> {
                        dayOfWeekofDate = "Thursday"
                    }
                    Calendar.FRIDAY -> {
                        dayOfWeekofDate = "Friday"
                    }
                    Calendar.SATURDAY -> {
                        dayOfWeekofDate = "Saturday"
                    }
                    Calendar.SUNDAY -> {
                        dayOfWeekofDate = "Sunday"
                    }
                }

                var timeRangeofSubject = ""
                //check current loop date dayofweek
                for (g in 0 until dayRaw.size) {
                    if (dayRaw[g].equals(dayOfWeekofDate)) {
                        //get the time period of corresponding day of week
                        timeRangeofSubject = timeRaw[g]
                    }
                }//end of for
                val timeRangeofSubjectRaw = timeRangeofSubject.split("-")
                val startTime = timeRangeofSubjectRaw[0]
                val endTime = timeRangeofSubjectRaw[1]
                val periodHour = endTime.toInt() - startTime.toInt()

                totalPeriodHourofSubjectinMonth += periodHour

                //search the current date in db
                val attendanceTable =
                    FirebaseDatabase.getInstance().reference.child("ams").child("attendance")
                val monthofCheckDateRaw = dateCheckAttendannce.split("-")
                val monthofCheckDate = monthofCheckDateRaw[1]


                val dateR = dateCheckAttendannce.split("-")
                dateCheckAttendannce = "${dateR[0]}${dateR[1]}${dateR[2]}"
                if (dateR[0].toInt() < 10) {
                    dateCheckAttendannce = "0${dateR[0]}${dateR[1]}${dateR[2]}"
                }

                attendanceTable.child(monthofCheckDate).child(dateCheckAttendannce)
                    .child(code!!).child(mkpt)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Toast.makeText(
                                this@SubjectOfParticularStudentDeanDetailActivity,
                                "Error occur" + p0.toException().toString(),
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            //Log.d("Tyler", "just checking for ${dataSnapshot.getValue(Attendance::class.java)!!.mkpt}")
                            if (dataSnapshot.exists()) {
                                //get period hour if attend
                                totalPeroidHourofAttend += periodHour

                                attendancePercentage =
                                    ((totalPeroidHourofAttend.toDouble() / totalPeriodHourofSubjectinMonth.toDouble()) * 100)

                                val decimalFormat = DecimalFormat("#.")
                                attendancePercentage = decimalFormat.format(attendancePercentage).toDouble()


                                if (semMonthAryy[b].equals("05")) {
                                    may = attendancePercentage.toString()
                                } else if (semMonthAryy[b].equals("06")) {
                                    june = attendancePercentage.toString()
                                } else if (semMonthAryy[b].equals("07")) {
                                    july = attendancePercentage.toString()
                                } else {
                                    august = attendancePercentage.toString()
                                }
                                tvmonthlypercent?.text =
                                    "$may% of 100% for May \n$june% of 100% for June\n$july% of 100% for July\n$august% of 100% for August"
                                Log.d(
                                    "Tyler",
                                    "Period Count in total: $totalPeriodHourofSubjectinMonth attend count $totalPeroidHourofAttend percentage: ${attendancePercentage} subject>> ${code} , month: ${semMonthAryy[b]} "
                                )

                            } else {
                                Log.d(
                                    "Tyler",
                                    "No attendance for date $dateCheckAttendannce with subject code ${code}"
                                )
                            }
                        }//end of onDataChange

                    })
            }//end of for loop for eligible subject date

        }//end of loop for 4 months

    }

    private fun isLeapYear(year: Int): Boolean {
        return if (year % 4 != 0) {
            false
        } else if (year % 400 == 0) {
            true
        } else year % 100 != 0
    }//end of leap year

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

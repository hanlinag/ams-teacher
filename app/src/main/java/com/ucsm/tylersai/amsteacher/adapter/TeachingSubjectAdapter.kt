package com.ucsm.tylersai.amsteacher.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ucsm.tylersai.amsteacher.QRGenerateActivity
import com.ucsm.tylersai.amsteacher.model.Subject
import kotlinx.android.synthetic.main.teaching_subjects_recycler_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class TeachingSubjectAdapter(val items: ArrayList<Subject>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    private val DATE_FORMAT = "dd-MM-yyyy"
    var todaysDay: String = ""
    var hour: Int? = null

    private var todayForAttendanceKey = ""

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = ViewHolder(
            LayoutInflater.from(context).inflate(
                com.ucsm.tylersai.amsteacher.R.layout.teaching_subjects_recycler_row,
                parent,
                false
            )
        )

        var dateFormat = SimpleDateFormat(DATE_FORMAT)
        var c = Calendar.getInstance().time
        var today = dateFormat.format(c)

        var formatofDate = SimpleDateFormat(DATE_FORMAT)
        var dateForWeekDay = formatofDate.parse(today)
        var calendarObj = Calendar.getInstance()

        //get current time
        hour = calendarObj.get(Calendar.HOUR_OF_DAY)
        Log.d("Tyler", "today current hour is $hour and current date is: $today")
        calendarObj.time = dateForWeekDay

        when (calendarObj.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> {
                todaysDay = "Sunday"
            }
            Calendar.MONDAY -> {
                todaysDay = "Monday"
            }
            Calendar.TUESDAY -> {
                todaysDay = "Tuesday"
            }
            Calendar.WEDNESDAY -> {
                todaysDay = "Wednesday"
            }
            Calendar.THURSDAY -> {
                todaysDay = "Thursday"
            }
            Calendar.FRIDAY -> {
                todaysDay = "Friday"
            }
            Calendar.SATURDAY -> {
                todaysDay = "Saturday"
            }
        }

        val cc = Calendar.getInstance()
        var year = cc.get(Calendar.YEAR)
        var month = cc.get(Calendar.MONTH) + 1
        var day = cc.get(Calendar.DAY_OF_MONTH)

        var realDay: String? = ""
        var realMonth: String? = ""

        if (day < 10) {
            realDay = "0${day}"
        } else {
            realDay = day.toString()
        }
        if (month < 10) {
            realMonth = "0${month}"
        } else {
            realMonth = month.toString()
        }

        todayForAttendanceKey = "$realDay$realMonth$year"

        view.itemView.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to collect attendance on subject: ${view.tvSubjectCode.text}")
                .setPositiveButton("Collect", DialogInterface.OnClickListener { dialogInterface, i ->

                    var correspondingDate = view.tvD
                    var correspondingTime = view.tvT
                    Log.d("Tyler", "today, tvd up ${view.tvD}")

                    if (dateAndTimeCorrect(correspondingDate, correspondingTime)) {

                        dialogInterface.dismiss()
                        var intent = Intent(context, QRGenerateActivity::class.java)
                        intent.putExtra(
                            "textToEncrypt",
                            "${view.tvSubjectCode.text},${view.tvSubjectName.text},$todaysDay,$hour,$todayForAttendanceKey"
                        )
                        intent.putExtra("subjectName", view.tvSubjectName.text)
                        startActivity(context, intent, null)

                    } else {

                        dialogInterface.dismiss()
                        AlertDialog.Builder(context)
                            .setTitle("Error")
                            .setMessage("You current day and time are not match with the schedule ")
                            .setPositiveButton(
                                "Okay",
                                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                            .show()
                    }//end of else

                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })
                .show()
        }

        return view
    }

    private fun dateAndTimeCorrect(corDate: String, corTime: String): Boolean {
        var eligible = true

        var dayAry = corDate.split(",")
        var timeAry = corTime.split(",")

        for (i in 0 until dayAry.size) {
            var d = dayAry[i].trim()
            Log.d("Tyler", "today is: $todaysDay and schedule is ${d}")
            if (d.equals(todaysDay)) {
                Log.d("Tyler", "today entering checking time")
                var timeInterval = timeAry[i].split("-")
                var timeStart = timeInterval[0].trim().toInt()
                var timeEnd = timeInterval[1].trim().toInt()

                Log.d("Tyler", "today date n time is: ${dayAry[i]}, ${timeAry[i]} current hour is : $hour")
                Log.d("Tyler", "today start and end time is: $timeStart, $timeEnd")
                //check if time is correct
                eligible = compareValues(hour, timeStart) >= 0 && compareValues(hour, timeEnd) < 0//end of else time check

                break

            } else {
                eligible = false
            }
        }
        return eligible
    }

    // Binds each animal in the ArrayList to a view,
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvSubjectCode?.text = items[position].subjectCode
        holder.tvSubjectName?.text = items[position].name

        //day and time
        var day = items[position].day
        var time = items[position].time

        var dayAry = day.split(",")
        var timeAry = time.split(",")
        holder.tvsubjectDay?.text = ""
        for (i in 0 until dayAry.size) {
            holder.tvsubjectDay?.text = "${holder.tvsubjectDay?.text}${dayAry[i]} : Time:${timeAry[i]}(hr)\n"
        }

        holder.tvD = items[position].day
        holder.tvT = items[position].time

        Log.d("Tyler", "today, tvd ${holder.tvD}")

        holder.tvsubjectRoom?.text = "Room - " + items[position].room
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvSubjectCode = view.subject_code_tv
    val tvSubjectName = view.subject_title_detail_tv
    val tvsubjectDay = view.subject_day_tv
    val tvsubjectRoom = view.subject_room_tv
    var tvD: String = ""
    var tvT: String = ""
}
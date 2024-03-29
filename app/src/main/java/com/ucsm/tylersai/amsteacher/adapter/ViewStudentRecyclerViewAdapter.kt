package com.ucsm.tylersai.amsteacher.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ucsm.tylersai.amsteacher.model.Student
import com.ucsm.tylersai.amsteacher.ui.activity.ViewDetailStudentDeanActivity
import kotlinx.android.synthetic.main.row_view_student_attendance.view.*
import java.text.SimpleDateFormat
import java.util.*

class ViewStudentRecyclerViewAdapter(val items: ArrayList<Student>, val context: Context) :
    RecyclerView.Adapter<mViewHolder>() {

    private val DATE_FORMAT = "dd-MM-yyyy"
    var todaysDay: String = ""
    var hour: Int? = null

    private var todayForAttendanceKey = ""

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mViewHolder {

        val view = mViewHolder(
            LayoutInflater.from(context).inflate(
                com.ucsm.tylersai.amsteacher.R.layout.row_view_student_attendance,
                parent,
                false
            )
        )

        val dateFormat = SimpleDateFormat(DATE_FORMAT)
        val c = Calendar.getInstance().time
        val today = dateFormat.format(c)

        val formatofDate = SimpleDateFormat(DATE_FORMAT)
        val dateForWeekDay = formatofDate.parse(today)
        val calendarObj = Calendar.getInstance()

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
        val year = cc.get(Calendar.YEAR)
        val month = cc.get(Calendar.MONTH) + 1
        val day = cc.get(Calendar.DAY_OF_MONTH)

        var realDay: String? = ""
        var realMonth: String? = ""

        realDay = if (day < 10) {
            "0${day}"
        } else {
            day.toString()
        }
        realMonth = if (month < 10) {
            "0${month}"
        } else {
            month.toString()
        }

        todayForAttendanceKey = "$realDay$realMonth$year"

        view.itemView.setOnClickListener {

            val intent = Intent(context, ViewDetailStudentDeanActivity::class.java)
            intent.putExtra("mkpt", view.tvmkpt.text)
            intent.putExtra("subject", view.tvSubject)
            startActivity(context, intent, null)
        }



        return view
    }

    // Binds each animal in the ArrayList to a view,
    override fun onBindViewHolder(holder: mViewHolder, position: Int) {
        holder.tvmkpt?.text = items[position].mkpt
        holder.tvname?.text = items[position].name
        holder.tvmajor?.text = items[position].major
        holder.tvSubject = items[position].subject


    }
}

class mViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvmkpt = view.tv_mkpt_view_student_dean
    val tvname = view.tv_name_view_student_dean
    var tvmajor = view.tv_major_view_student_dean
    var tvSubject = ""

}
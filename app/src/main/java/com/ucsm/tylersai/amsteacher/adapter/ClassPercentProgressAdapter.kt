package com.ucsm.tylersai.amsteacher.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ucsm.tylersai.amsteacher.model.ClassAttendanceProgress
import kotlinx.android.synthetic.main.row_major_dashboard_chart.view.*
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import java.text.SimpleDateFormat
import java.util.*

class ClassPercentProgressAdapter(
    val items: ArrayList<ClassAttendanceProgress>,
    val context: Context
) :
    RecyclerView.Adapter<MyViewHolder>() {

    private val DATE_FORMAT = "dd-MM-yyyy"
    var todaysDay: String = ""
    var hour: Int? = null

    private var todayForAttendanceKey = ""

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view = MyViewHolder(
            LayoutInflater.from(context).inflate(
                com.ucsm.tylersai.amsteacher.R.layout.row_major_dashboard_chart,
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

        }

        return view
    }

    // Binds each animal in the ArrayList to a view,
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvClassName?.text = items[position].className
        holder.tvMajorClassPercent?.text = items[position].overallPercentage

        //calculating pie chart
        //var attendancePercentage = attendancePercentage.toFloat()
        val pieData = ArrayList<SliceValue>()
        pieData.add(
            SliceValue(
                items[position].overallPercentage.toFloat(),
                Color.argb(100, 222, 74, 91)
            )
        )
        pieData.add(
            SliceValue(
                (100F - items[position].overallPercentage.toFloat()),
                Color.argb(100, 96, 153, 155)
            )
        )
        val pieChartData = PieChartData(pieData)
        pieChartData.setHasCenterCircle(true).centerText1FontSize = 10
        pieChartData.setHasCenterCircle(true).centerText1 = "${items[position].overallPercentage.toFloat()}%"

        holder.tvChart.pieChartData = pieChartData

    }
}

class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvClassName = view.tv_major_class
    val tvMajorClassPercent = view.tv_major_percent
    var tvChart = view.class_pie_chart_dashboard
}
package com.ucsm.tylersai.amsteacher.adapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.Attendance
import java.text.SimpleDateFormat
import java.util.*

class SubjectDetailListAdapter(context: Context, arrayList: ArrayList<Attendance>) : ListAdapter {
    var context: Context? = context
    var arrayList: ArrayList<Attendance>? = arrayList


    override fun isEmpty(): Boolean {

        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val attendance: Attendance = arrayList!!.get(position)

        val inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.subject_detail_listview_row, null, true)


        val date = rowView.findViewById<TextView>(R.id.attendance_date)
        val day = rowView.findViewById<TextView>(R.id.attendance_day)
        val time = rowView.findViewById<TextView>(R.id.attendance_time)

        //getthe current date of day of week
        val DateFormatt = "dd-MM-yyyy"
        val formatofDate = SimpleDateFormat(DateFormatt)
        val dateForWeekDay = formatofDate.parse(attendance.date)
        val calendarObj = Calendar.getInstance()
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
        }

        date.text = attendance.date
        day.text = weekofDay
        time.text = attendance.time

        rowView.setOnClickListener {

            // Toast.makeText(rowView.context, "Hello $text ", Toast.LENGTH_LONG).show()

            /*  val intent = Intent(rowView.context, NotiDetailActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("body", body)
            intent.putExtra("date", date)*/

            //ContextCompat.startActivity(rowView.context, intent, null)
        }

        return rowView
    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {

    }

    override fun getItemViewType(position: Int): Int {
        return position

    }

    override fun getItem(position: Int): Any {

        return position
    }

    override fun getViewTypeCount(): Int {

        return arrayList!!.size
    }

    override fun isEnabled(p0: Int): Boolean {

        return true
    }

    override fun getItemId(position: Int): Long {

        return position.toLong()
    }

    override fun hasStableIds(): Boolean {

        return false
    }

    override fun areAllItemsEnabled(): Boolean {

        return false
    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {

    }

    override fun getCount(): Int {

        return arrayList!!.size
    }
}



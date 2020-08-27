package com.ucsm.tylersai.amsteacher.adapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.Subject

class AddSubjectListViewAdapter(context: Context, arrayList: ArrayList<Subject>) : ListAdapter {
    var context: Context? = context
    var arrayList: ArrayList<Subject>? = arrayList


    override fun isEmpty(): Boolean {

        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var subject: Subject = arrayList!!.get(position)

        var inflater = LayoutInflater.from(context)
        var rowView = inflater.inflate(R.layout.row_add_subject_list_add, null, true)


        var tvsubjectcode = rowView.findViewById<TextView>(R.id.tv_subject_code_add_subject)
        var tvsubjectname = rowView.findViewById<TextView>(R.id.tv_subject_name_add_subject)
        var tvsubjectday = rowView.findViewById<TextView>(R.id.tv_subject_day_add_subject)
        var tvsubjectroom = rowView.findViewById<TextView>(R.id.tv_room_add_subject)
        var tvteacherid = rowView.findViewById<TextView>(R.id.tv_subject_teacher_id_add_subject)



        tvsubjectcode.text = subject.subjectCode
        tvsubjectname.text = subject.name
        tvsubjectroom.text = subject.room
        tvteacherid.text = subject.teacherId

        tvsubjectday.text = ""

        var subjectDaysAry = subject.day.split(",")
        var subjectTimeAry = subject.time.split(",")

        for (i in 0 until subjectDaysAry.size) {
            tvsubjectday.text = "${tvsubjectday.text}${subjectDaysAry[i]}: Time:${subjectTimeAry[i]}\n"
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
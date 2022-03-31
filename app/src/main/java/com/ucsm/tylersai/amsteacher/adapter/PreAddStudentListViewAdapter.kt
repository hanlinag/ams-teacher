package com.ucsm.tylersai.amsteacher.adapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.PreStudentList

class PreAddStudentListViewAdapter(context: Context, arrayList: ArrayList<PreStudentList>) : ListAdapter {
    var context: Context? = context
    var arrayList: ArrayList<PreStudentList>? = arrayList


    override fun isEmpty(): Boolean {

        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val student: PreStudentList = arrayList!![position]

        val inflater = LayoutInflater.from(context)
        val rowView =
            inflater.inflate(R.layout.row_pre_add_student_list_view_add_student, null, true)


        val nameTv = rowView.findViewById<TextView>(R.id.tv_name_add_student)
        val mkptTv = rowView.findViewById<TextView>(R.id.tv_mkpt_add_student)
        val majorTv = rowView.findViewById<TextView>(R.id.tv_major_add_student)

        nameTv.text = student.name
        mkptTv.text = student.mkpt
        majorTv.text = student.major



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
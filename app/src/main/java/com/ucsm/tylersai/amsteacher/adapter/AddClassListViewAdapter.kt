package com.ucsm.tylersai.amsteacher.adapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.SectionClass

class AddClassListViewAdapter(context: Context, arrayList: ArrayList<SectionClass>) : ListAdapter {
    var context: Context? = context
    var arrayList: ArrayList<SectionClass>? = arrayList


    override fun isEmpty(): Boolean {

        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val sectionClass: SectionClass = arrayList!!.get(position)

        val inflater = LayoutInflater.from(context)
        val rowView = inflater.inflate(R.layout.row_add_class_class_list_view_dean, null, true)


        val tvClassName = rowView.findViewById<TextView>(R.id.tv_section_name_add_class_dean)
        val tvTeachingSubject =
            rowView.findViewById<TextView>(R.id.tv_teaching_subject_add_class_dean)
        val tvYear = rowView.findViewById<TextView>(R.id.tv_year_add_class_dean)



        tvClassName.text = sectionClass.sectionname
        tvTeachingSubject.text = sectionClass.teachingsubject
        tvYear.text = sectionClass.year



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
package com.ucsm.tylersai.amsteacher.adapter

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.Teacher

class AddTeacherListViewAdapter(context: Context, arrayList: ArrayList<Teacher>) : ListAdapter {
    var context: Context? = context
    var arrayList: ArrayList<Teacher>? = arrayList


    override fun isEmpty(): Boolean {

        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var teacher: Teacher = arrayList!!.get(position)

        var inflater = LayoutInflater.from(context)
        var rowView = inflater.inflate(R.layout.row_teacher_list_view_add_teacher_dean, null, true)


        var tvid = rowView.findViewById<TextView>(R.id.tv_teacher_id_add_teacher_dean)
        var tvname = rowView.findViewById<TextView>(R.id.tv_teacher_name_add_teacher_dean)
        var tvphone = rowView.findViewById<TextView>(R.id.tv_teacher_phone_add_teacher_dean)
        var tvemail = rowView.findViewById<TextView>(R.id.tv_teacher_email_add_teacher_dean)
        var tvaddress = rowView.findViewById<TextView>(R.id.tv_teacher_address_add_teacher_dean)

        var imgProfile = rowView.findViewById<ImageView>(R.id.img_profile_add_teacher_dean)

        tvid.text = teacher.id
        tvname.text = teacher.name
        tvphone.text = teacher.phone
        tvemail.text = teacher.email
        tvaddress.text = teacher.address

        Glide.with(context!!).load(teacher.profileurl).into(imgProfile)



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
package com.ucsm.tylersai.amsteacher.adapter

import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.ucsm.tylersai.amsteacher.MedicalLeaveActionDeanActivity
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.MedicalLeave

class AddMedicalLeaveListViewAdapter (context: Context, arrayList: ArrayList<MedicalLeave>) : ListAdapter {
    var context: Context? = context
    var arrayList: ArrayList<MedicalLeave>? = arrayList


    override fun isEmpty(): Boolean {

        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var medicalLeave : MedicalLeave = arrayList!!.get(position)

        var inflater = LayoutInflater.from(context)
        var rowView = inflater.inflate(R.layout.row_asking_student_lists_medical_leave_dean,null,true)



        var tvmkpt = rowView.findViewById<TextView>(R.id.tv_mkpt_medical_leave_dean)
        var tvaskingDate = rowView.findViewById<TextView>(R.id.tv_asking_date_medical_leave_dean)
        var tvprogress = rowView.findViewById<TextView>(R.id.tv_progress_medical_leave_dean)

        var imgMedicalLeaveLetter = rowView.findViewById<ImageView>(R.id.img_medical_leave_letter_dean)



        tvmkpt.text = medicalLeave.mkpt
        tvaskingDate.text = medicalLeave.askingDate
        tvprogress.text = medicalLeave.progress

        Glide.with(context!!).load(medicalLeave.imgurl).into(imgMedicalLeaveLetter)


        rowView.setOnClickListener {
            var intent = Intent(rowView.context, MedicalLeaveActionDeanActivity::class.java)
            intent.putExtra("mkpt", medicalLeave.mkpt)
            intent.putExtra("askingdate", medicalLeave.askingDate)
            intent.putExtra("progress", medicalLeave.progress)
            intent.putExtra("imgurl", medicalLeave.imgurl)
            intent.putExtra("key", medicalLeave.key)

            startActivity(rowView.context, intent, null)
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
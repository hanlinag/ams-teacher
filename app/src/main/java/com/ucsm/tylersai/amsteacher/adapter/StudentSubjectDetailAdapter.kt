package com.ucsm.tylersai.amsteacher.adapter

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.Subject
import com.ucsm.tylersai.amsteacher.ui.activity.SubjectOfParticularStudentDeanDetailActivity

class StudentSubjectDetailAdapter(context: Context, arrayList: ArrayList<String>, m: String) :
    ListAdapter {
    var context: Context? = context
    var arrayList: ArrayList<String>? = arrayList
    var mkpt: String = m


    override fun isEmpty(): Boolean {

        return false
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val subject: String = arrayList!!.get(position)

        val inflater = LayoutInflater.from(context)
        val rowView =
            inflater.inflate(R.layout.row_student_subject_detail_dean_subject_list_view, null, true)


        val subjectCode = rowView.findViewById<TextView>(R.id.subject_code_in_list_view_dean_detail)


        subjectCode.text = subject



        rowView.setOnClickListener {
            val pDialog = ProgressDialog(context)
            pDialog.setMessage("Loading...")
            pDialog.show()

            //student and subject
            val subAry = subject.split(":")
            val subjectTable =
                FirebaseDatabase.getInstance().reference.child("ams").child("subject")
            subjectTable.child(subAry[0].trim())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(context, "Error occurred ${p0.message}", Toast.LENGTH_LONG)
                            .show()
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        val sub = dataSnapshot.getValue(Subject::class.java)

                        val intent = Intent(
                            context,
                            SubjectOfParticularStudentDeanDetailActivity::class.java
                        )
                        intent.putExtra("subcode", sub!!.subjectCode)
                        intent.putExtra("subname", sub.name)
                        intent.putExtra("mkpt", mkpt)
                        intent.putExtra("subdays", sub.day)
                        intent.putExtra("subtimes", sub.time)
                        startActivity(context!!, intent, null)
                        pDialog.dismiss()
                    }
            })
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
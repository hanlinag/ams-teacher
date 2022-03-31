package com.ucsm.tylersai.amsteacher.ui.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.adapter.StudentSubjectDetailAdapter

class ViewDetailStudentDeanActivity : AppCompatActivity() {
    lateinit var listView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_detail_student_dean)

        val mkpt = intent.getStringExtra("mkpt")
        val subject = intent.getStringExtra("subject")
        supportActionBar!!.title = "mkpt-$mkpt"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)

        listView = findViewById(R.id.subject_of_particular_student_deann_detail)

        //get corresponding data of student
        val eligiblesubject = ArrayList<String>()
        val subjectAry = subject?.split(",")
        if (subjectAry != null) {
            for (i in subjectAry.indices) {
                if (subjectAry[i].length > 5) {
                    eligiblesubject.add(subjectAry[i])
                }
            }
        }

        listView.adapter = mkpt?.let {
            StudentSubjectDetailAdapter(
                this@ViewDetailStudentDeanActivity, eligiblesubject,
                it
            )
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

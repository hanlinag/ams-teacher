package com.ucsm.tylersai.amsteacher

import android.os.Bundle
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.ucsm.tylersai.amsteacher.adapter.StudentSubjectDetailAdapter

class ViewDetailStudentDeanActivity : AppCompatActivity() {
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_detail_student_dean)

        var mkpt = intent.getStringExtra("mkpt")
        var subject = intent.getStringExtra("subject")
        supportActionBar!!.title = "mkpt-$mkpt"

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)

        listView = findViewById(R.id.subject_of_particular_student_deann_detail)

        //get corresponding data of student
        var eligiblesubject = ArrayList<String>()
        var subjectAry = subject.split(",")
        for (i in 0 until subjectAry.size) {
            if (subjectAry[i].length > 5) {
                eligiblesubject.add(subjectAry[i])
            }
        }

        listView.adapter = StudentSubjectDetailAdapter(this@ViewDetailStudentDeanActivity, eligiblesubject, mkpt)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

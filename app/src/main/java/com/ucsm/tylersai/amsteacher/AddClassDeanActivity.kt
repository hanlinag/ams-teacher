package com.ucsm.tylersai.amsteacher

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucsm.tylersai.amsteacher.model.SectionClass

class AddClassDeanActivity : AppCompatActivity() {

    lateinit var edtSectionName: EditText
    lateinit var edtTeachingSubject: EditText
    lateinit var edtYear: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_class_dean)

        supportActionBar!!.title = "Add Section Class"
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        edtSectionName = findViewById(R.id.edt_section_name_add_section_class_dean)
        edtTeachingSubject = findViewById(R.id.edt_teaching_subject_add_section_class_dean)
        edtYear = findViewById(R.id.edt_year_add_section_class_dean)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_class_dean, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
            }

            R.id.menu_add_class_dean_add -> {

                var sectionname = edtSectionName.text.toString()
                var teachingsub = edtTeachingSubject.text.toString()
                var year = edtTeachingSubject.text.toString()

                if (sectionname.isEmpty() || teachingsub.isEmpty() || year.isEmpty()) {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Form not complete!")
                        .setPositiveButton(
                            "Ok",
                            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                        .show()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to add this section class to database?")
                        .setPositiveButton("Add", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()

                            var progressDialog = ProgressDialog(this@AddClassDeanActivity)
                            progressDialog.setMessage("Loading...")
                            progressDialog.show()

                            var sectionClassObj = SectionClass(teachingsub, year, sectionname)
                            var sectionClassTable =
                                FirebaseDatabase.getInstance().reference.child("ams").child("sectionclass")
                            sectionClassTable.child(sectionname)
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                        Toast.makeText(
                                            this@AddClassDeanActivity,
                                            "Error occurred ${p0.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }

                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            progressDialog.dismiss()
                                            AlertDialog.Builder(this@AddClassDeanActivity)
                                                .setTitle("Error")
                                                .setMessage("This section already exist!")
                                                .setPositiveButton(
                                                    "Ok",
                                                    DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                                                .show()
                                        } else {
                                            sectionClassTable.child(sectionname).setValue(sectionClassObj)
                                            Toast.makeText(
                                                this@AddClassDeanActivity,
                                                "Added successfully!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            progressDialog.dismiss()
                                            finish()
                                        }
                                    }

                                })
                        })
                        .setNegativeButton(
                            "Cancel",
                            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                        .show()

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

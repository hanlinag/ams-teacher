package com.ucsm.tylersai.amsteacher

import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Base64
import android.util.Log
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
import com.ucsm.tylersai.amsteacher.model.Teacher
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AddTeacherDeanActivity : AppCompatActivity() {
    lateinit var edtId: EditText
    lateinit var edtName: EditText
    lateinit var edtPhone: EditText
    lateinit var edtEmail: EditText
    lateinit var edtAddress: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_teacher_dean)

        supportActionBar!!.title = "Add Teacher"
        supportActionBar!!.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        edtId = findViewById(R.id.edt_id_add_teacher)
        edtName = findViewById(R.id.edt_name_add_teacher)
        edtPhone = findViewById(R.id.edt_phone_add_teacher)
        edtEmail = findViewById(R.id.edt_email_add_teacher)
        edtAddress = findViewById(R.id.edt_address_add_teacher)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_teacher_dean, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_add_teacher_dean_add -> {

                var id = edtId.text.toString()
                var name = edtName.text.toString()
                var phone = edtPhone.text.toString()
                var email = edtEmail.text.toString()
                var address = edtAddress.text.toString()

                if (id.isEmpty() || name.isEmpty() || phone.isEmpty() || email.isEmpty() || address.isEmpty()) {
                    AlertDialog.Builder(this@AddTeacherDeanActivity)
                        .setTitle("Error")
                        .setMessage("Form not complete! ")
                        .setPositiveButton(
                            "Ok",
                            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                        .show()
                } else if (id.length < 4 || !id.contains("10")) {
                    AlertDialog.Builder(this@AddTeacherDeanActivity)
                        .setTitle("Error")
                        .setMessage("Wrong ID format. It should be 10xx. Try again!")
                        .setPositiveButton(
                            "Ok",
                            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                        .show()
                } else {
                    AlertDialog.Builder(this@AddTeacherDeanActivity)
                        .setTitle("Confirmationn")
                        .setMessage("Are you sure you want to add this user into the database?")
                        .setPositiveButton("Add", DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()

                            var progressDialog = ProgressDialog(this@AddTeacherDeanActivity)
                            progressDialog.setMessage("Loading...")
                            progressDialog.show()

                            //do the real job
                            //decrypt the password
                            var password = "1234".encrypt("amsucsmapp123456")
                            var profileurl =
                                "https://firebasestorage.googleapis.com/v0/b/attendance-system-245615.appspot.com/o/dprofile.png?alt=media&token=5af77504-a9d6-4294-968e-e7d457247d0d"
                            var teacherObj = Teacher(
                                id, name, email, phone, password, address, profileurl, "no"
                            )

                            var teacherTable = FirebaseDatabase.getInstance().reference.child("ams").child("teacher")
                            teacherTable.child(id).addValueEventListener(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Toast.makeText(
                                        this@AddTeacherDeanActivity,
                                        "Error occurred ${p0.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }


                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    Log.d("Tyler", "key is ::: ${dataSnapshot.key}")

                                    if (dataSnapshot.exists()) {
                                        AlertDialog.Builder(this@AddTeacherDeanActivity)
                                            .setTitle("Error")
                                            .setMessage("User already exist!")
                                            .setPositiveButton(
                                                "Ok",
                                                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                                            .show()
                                    } else {
                                        teacherTable.child(id).setValue(teacherObj)
                                        Toast.makeText(
                                            this@AddTeacherDeanActivity,
                                            "User added successfull to database!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        finish()
                                        progressDialog.dismiss()
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

    fun String.encrypt(password: String): String {
        val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = password.toCharArray()
        for (i in 0 until charArray.size) {
            iv[i] = charArray[i].toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

        val encryptedValue = cipher.doFinal(this.toByteArray())
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }
}

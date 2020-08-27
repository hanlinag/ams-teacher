package com.ucsm.tylersai.amsteacher

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucsm.tylersai.amsteacher.model.Teacher
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class LoginActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(findViewById(R.id.login_customized_toolbar))

        supportActionBar!!.title = ""

        FirebaseApp.initializeApp(this)
        sharedPreferences = getSharedPreferences(getString(R.string.teacherPref), Context.MODE_PRIVATE)
        prefEditor = sharedPreferences.edit()

        var teacherTable = FirebaseDatabase.getInstance().reference

        val edtMkpt = findViewById<EditText>(R.id.login_edt_mkpt)
        val edtPw = findViewById<EditText>(R.id.login_edt_password)

        val btLogin = findViewById<Button>(R.id.login_bt_login)

        val tvForgetPw = findViewById<TextView>(R.id.login_forget_pw_tv)

        var teacher: Teacher?

        btLogin.setOnClickListener {

            // var password = edtPw.text.toString()
            if (edtMkpt.text.toString().equals("") || edtPw.text.toString().equals("")) {
                Toast.makeText(applicationContext, "Enter user id and password", Toast.LENGTH_LONG).show()
            } else if (edtMkpt.text.length < 4) {
                Toast.makeText(applicationContext, "Enter the correct user id", Toast.LENGTH_LONG).show()
            } else {

                var userid = edtMkpt.text.toString()

                Log.d("Tyler", "child................${userid}")

                var progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Please wait...")
                progressDialog.max = 100
                progressDialog.show()

                teacherTable.child("ams").child("teacher").child(userid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                            Toast.makeText(
                                this@LoginActivity,
                                "Error Occur" + p0.toException().toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            if (dataSnapshot.exists()) {

                                teacher = dataSnapshot.getValue(Teacher::class.java)
                                teacher?.id = userid

                                var decryptedPassword = teacher!!.password.decrypt("amsucsmapp123456")
                                Log.d("Tyler", "name is: ${teacher!!.name} and password is: $decryptedPassword")
                                if (edtPw.text.toString().equals(decryptedPassword)) {

                                    //saving current user account for other activity

                                    // var prefEditor = sharedPreferences.edit()
                                    //prefEditor.clear()
                                    prefEditor.putString(getString(R.string.prefid), teacher?.id)
                                    prefEditor.putString(getString(R.string.prefname), teacher?.name)
                                    prefEditor.putString(getString(R.string.prefpassword), teacher?.password)
                                    prefEditor.putString(getString(R.string.prefphone), teacher?.phone)
                                    prefEditor.putString(getString(R.string.prefemail), teacher?.email)
                                    prefEditor.putString(getString(R.string.prefaddress), teacher?.address)
                                    prefEditor.putString(getString(R.string.prefurl), teacher?.profileurl)
                                    prefEditor.putString(getString(R.string.prefisdean), teacher?.isdean)

                                    if (teacher?.isdean == "yes") {
                                        //go to dean panel
                                        val intent = Intent(applicationContext, Main2Activity::class.java)
                                        progressDialog.dismiss()
                                        startActivity(intent)
                                        finishAffinity()
                                    } else {
                                        //go to teacher panel
                                        val intent = Intent(applicationContext, MainActivity::class.java)
                                        progressDialog.dismiss()
                                        startActivity(intent)
                                        finishAffinity()
                                    }

                                } else {

                                    progressDialog.dismiss()
                                    AlertDialog.Builder(this@LoginActivity)
                                        .setTitle("Error")
                                        .setMessage("Incorrect password!")
                                        .setPositiveButton(
                                            "Got it",
                                            DialogInterface.OnClickListener { dialogInterface, i ->

                                                dialogInterface.dismiss()

                                            })
                                        .show()
                                }//end of else in checking mkpt and pw

                            } else {

                                progressDialog.dismiss()
                                AlertDialog.Builder(this@LoginActivity)
                                    .setTitle("Error")
                                    .setMessage("User not exit!")
                                    .setPositiveButton("Okay", DialogInterface.OnClickListener { dialogInterface, i ->

                                        dialogInterface.dismiss()

                                    })
                                    .show()
                            }//end of else in checking user exit
                            prefEditor.clear()
                            prefEditor.commit()
                        }
                    })

            }//end of method

            tvForgetPw.setOnClickListener {
                val intent = Intent(this, ForgetPasswordActivity::class.java)
                startActivity(intent)

            }
        }
    }

    fun String.decrypt(password: String): String {
        val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = password.toCharArray()
        for (i in 0 until charArray.size) {
            iv[i] = charArray[i].toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

        val decryptedByteValue = cipher.doFinal(Base64.decode(this, Base64.DEFAULT))
        return String(decryptedByteValue)
    }
}

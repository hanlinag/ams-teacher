package com.ucsm.tylersai.amsteacher.ui.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.Teacher
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class ChangePasswordActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var preferencesEditor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        supportActionBar!!.title = "Change Password"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        sharedPreferences =
            getSharedPreferences(getString(R.string.teacherPref), Context.MODE_PRIVATE)
        preferencesEditor = sharedPreferences.edit()

        val edtPassword = findViewById<EditText>(R.id.changepw_edt_pw)
        val edtConfirm = findViewById<EditText>(R.id.changepw_edt_confirm)

        val btReset = findViewById<Button>(R.id.change_pw_bt_create)

        btReset.setOnClickListener {
            val password = edtPassword.text.toString()
            val confirmPassword = edtConfirm.text.toString()

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Password field is empty")
                    .setPositiveButton(
                        "Ok"
                    ) { dialogInterface, i -> dialogInterface.dismiss() }
                    .show()
            } else {
                if (password != confirmPassword) {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Password does not match")
                        .setPositiveButton(
                            "Ok"
                        ) { dialogInterface, i -> dialogInterface.dismiss() }
                        .show()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Confirm")
                        .setMessage("Are you sure to change your password?")
                        .setNegativeButton("Cancel") { dialogInterface, i ->
                            dialogInterface.dismiss()
                        }
                        .setPositiveButton("Change") { dialogInterface, i ->

                            dialogInterface.dismiss()
                            val progressDialog = ProgressDialog(this)
                            progressDialog.setMessage("Loading...")
                            progressDialog.show()

                            //encrypt password
                            val encryptedPassword = confirmPassword.encrypt("amsucsmapp123456")
                            //teacher table
                            val teacherTable = FirebaseDatabase.getInstance().reference.child("ams")
                                .child("teacher")

                            val teacher =
                                sharedPreferences.getString(getString(R.string.prefid), null)
                                    ?.let { it1 ->
                                        Teacher(
                                            it1,
                                            sharedPreferences.getString(
                                                getString(R.string.prefname),
                                                null
                                            )!!,
                                            sharedPreferences.getString(
                                                getString(R.string.prefemail),
                                                null
                                            )!!,
                                            sharedPreferences.getString(
                                                getString(R.string.prefphone),
                                                null
                                            )!!,
                                            encryptedPassword,
                                            sharedPreferences.getString(
                                                getString(R.string.prefaddress),
                                                null
                                            )!!,
                                            sharedPreferences.getString(
                                                getString(R.string.prefurl),
                                                null
                                            )!!,
                                            sharedPreferences.getString(
                                                getString(R.string.prefisdean),
                                                null
                                            )!!
                                        )
                                    }
                            preferencesEditor.putString(
                                getString(R.string.prefpassword),
                                confirmPassword
                            )
                            preferencesEditor.commit()

                            sharedPreferences.getString(getString(R.string.prefid), null)
                                ?.let { it1 ->
                                    teacherTable.child(it1)
                                        .setValue(teacher)
                                }

                            progressDialog.dismiss()
                            edtConfirm.text.clear()
                            edtPassword.text.clear()
                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_LONG)
                                .show()


                        }.show()
                }
            }


        }//end of button action


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun String.encrypt(password: String): String {
        val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = password.toCharArray()
        for (i in charArray.indices) {
            iv[i] = charArray[i].code.toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

        val encryptedValue = cipher.doFinal(this.toByteArray())
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }
}

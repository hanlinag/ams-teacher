package com.ucsm.tylersai.amsteacher.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ucsm.tylersai.amsteacher.R

class ForgetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        setSupportActionBar(findViewById(R.id.forget_customized_toolbar))

        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val btReset = findViewById<Button>(R.id.forget_bt_reset)
        btReset.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Are you sure to reset your password?")
                .setNegativeButton("Cancel") { _, i ->
                }
                .setPositiveButton("Reset") { _, i ->

                    val intent = Intent(this, PinActivity::class.java)
                    startActivity(intent)
                }
                .show()
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

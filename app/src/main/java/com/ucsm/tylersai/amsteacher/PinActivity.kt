package com.ucsm.tylersai.amsteacher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.goodiebag.pinview.Pinview
import com.google.android.material.snackbar.Snackbar

class PinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        val pinView = findViewById<Pinview>(R.id.pin_view)

        pinView.setPinViewEventListener { pinview, fromUser ->

            if (pinview.value.toString().equals("1111")) {
                val intent = Intent(this, NewPasswordActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Snackbar.make(pinView, "Pin Incorrect", Snackbar.LENGTH_LONG).show()
            }
        }


    }


}

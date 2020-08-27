package com.ucsm.tylersai.amsteacher

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    val SPLASH_LENGTH: Long = 1000
    lateinit var myDelayHandler: Handler

    lateinit var sharedPreferences: SharedPreferences

    internal val mRunnable: Runnable = Runnable {

        if (!isFinishing) {

            sharedPreferences = getSharedPreferences(getString(R.string.teacherPref), Context.MODE_PRIVATE)
            var id = sharedPreferences.getString(getString(R.string.prefid), null)

            if (id != null) {
                if (sharedPreferences.getString(getString(R.string.prefisdean), null) == "yes") {
                    //go to dean panel
                    val intent = Intent(this@SplashActivity, Main2Activity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    //go to teacher panel
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            } else {

                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        myDelayHandler = Handler()
        myDelayHandler.postDelayed(mRunnable, SPLASH_LENGTH)

    }

    override fun onDestroy() {
        if (myDelayHandler != null) {

            myDelayHandler.removeCallbacks(mRunnable)
        }
        super.onDestroy()
    }
}

package com.ucsm.tylersai.amsteacher.ui.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ucsm.tylersai.amsteacher.R

class SplashActivity : AppCompatActivity() {

    val SPLASH_LENGTH: Long = 1000
    lateinit var myDelayHandler: Handler

    lateinit var sharedPreferences: SharedPreferences

    private val mRunnable: Runnable = Runnable {

        if (!isFinishing) {

            sharedPreferences =
                getSharedPreferences(getString(R.string.teacherPref), Context.MODE_PRIVATE)
            val id = sharedPreferences.getString(getString(R.string.prefid), null)

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

        myDelayHandler = Handler(Looper.getMainLooper())
        myDelayHandler.postDelayed(mRunnable, SPLASH_LENGTH)

    }

    override fun onDestroy() {
        myDelayHandler.removeCallbacks(mRunnable)
        super.onDestroy()
    }
}

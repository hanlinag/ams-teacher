package com.ucsm.tylersai.amsteacher.ui.activity

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.model.Notifications
import java.text.SimpleDateFormat
import java.util.*

class BroadcastActivity : AppCompatActivity() {
    lateinit var edtTo: EditText
    lateinit var edtTitle: EditText
    lateinit var edtMessage: EditText

    lateinit var btSend: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_broadcast)

        supportActionBar!!.title = "Broadcast"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        edtTo = findViewById(R.id.edt_to_user_broadcast_dean)
        edtTitle = findViewById(R.id.edt_title_user_broadcast_dean)
        edtMessage = findViewById(R.id.edt_message_user_broadcast_dean)

        btSend = findViewById(R.id.bt_broadcast_dean)

        btSend.setOnClickListener {
            var to = edtTo.text.toString()
            var title = edtTitle.text.toString()
            var message = edtMessage.text.toString()



            if (to.isEmpty() || title.isEmpty() || message.isEmpty()) {
                AlertDialog.Builder(this@BroadcastActivity)
                    .setTitle("Error")
                    .setMessage("Form not complete!")
                    .setPositiveButton(
                        "OK",
                        DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                    .show()
            } else if (to.length < 8 || !to.contains("mkpt")) {
                AlertDialog.Builder(this@BroadcastActivity)
                    .setTitle("Error")
                    .setMessage("Have to be in mkpt-0000 format")
                    .setPositiveButton(
                        "OK",
                        DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                    .show()
            } else {

                to = to.substring(5)
                val DATEFORMAT = "dd-MM-YYYY HH:mm"

                // today date
                var dateFormat = SimpleDateFormat(DATEFORMAT)
                var c = Calendar.getInstance().time
                var today = dateFormat.format(c)
                var hour = c.hours
                var min = c.minutes

                //sending notification
                var noti = Notifications(title, message, "$today$hour$min", "mkpt$to$today", to)
                var notiTable = FirebaseDatabase.getInstance().reference.child("ams").child("notifications")
                notiTable.child("mkpt$to$today").setValue(noti)

                Toast.makeText(this@BroadcastActivity, "Message sent!", Toast.LENGTH_LONG).show()
                edtMessage.text.clear()
                edtTitle.text.clear()
                edtTo.text.clear()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

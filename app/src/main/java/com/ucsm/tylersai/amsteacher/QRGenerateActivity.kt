package com.ucsm.tylersai.amsteacher


import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color.BLACK
import android.graphics.Color.WHITE
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.ucsm.tylersai.amsteacher.model.Attendance
import com.ucsm.tylersai.amsteacher.model.Student
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList
import kotlin.collections.MutableMap
import kotlin.collections.forEach
import kotlin.collections.set


class QRGenerateActivity : AppCompatActivity() {

    val QRcodeWidth = 500
    private val IMAGE_DIRECTORY = "/AMSTeacher"
    var bitmap: Bitmap? = null
    private val etqr: EditText? = null
    private var iv: ImageView? = null

    lateinit var tvSubName: TextView

    lateinit var sharedPreferences: SharedPreferences

    var todayDateFromQR = ""
    var todaySubCodeFromQR = ""
    var todaySubNameFromQR = ""
    var monthFromQR = ""
    var absentCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrgenerate)

        supportActionBar!!.title = "Collect RollCall"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        tvSubName = findViewById(R.id.title_qr_gen)

        var text = intent.getStringExtra("textToEncrypt")
        tvSubName.text = intent.getStringExtra("subjectName")

        sharedPreferences = getSharedPreferences(getString(R.string.teacherPref), Context.MODE_PRIVATE)
        var lat = sharedPreferences.getString(getString(R.string.prefLat), null)
        var lon = sharedPreferences.getString(getString(R.string.prefLon), null)

        // var latfake = "22.1225661"
        // var lonfake ="96.1735698"
        text = "$text,$lat,$lon"
        Log.d("Tyler", "QR Text is : $text")

        var rawSplit = text.split(",")
        todayDateFromQR = rawSplit[4]
        todaySubCodeFromQR = rawSplit[0]
        todaySubNameFromQR = rawSplit[1]
        monthFromQR = todayDateFromQR.substring(2, 4)
        Log.d("Tyler", "substring of month is: $monthFromQR original is $todayDateFromQR")

        //encryption text
        var encrypted = ""
        // val sourceStr = "This is any source string"
        try {
            encrypted = text.encrypt("amsucsmapp123456")
            Log.d("Tyler", "today encrypted text is: $encrypted")
            Log.d("Tyler", "today decrypted text is: ${encrypted.decrypt("amsucsmapp123456")}")

        } catch (e: Exception) {
            e.printStackTrace()
        }

        iv = findViewById(R.id.qr_place_img)

        bitmap = TextToImageEncode(encrypted, BarcodeFormat.QR_CODE, QRcodeWidth, QRcodeWidth)
        iv!!.setImageBitmap(bitmap)
        // String path = saveImage(bitmap);  //give read write permission
        Toast.makeText(this, "QR Code Generated", Toast.LENGTH_SHORT).show()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            android.R.id.home -> {
                AlertDialog.Builder(this@QRGenerateActivity)
                    .setTitle("Confirmation")
                    .setMessage("Are you sure you want to exit from this activity")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                        finish()
                    })
                    .setNegativeButton(
                        "No",
                        DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                    .show()
            }
            R.id.menu_finish_qr_gen -> {
                //show absent list
                //get student who register in this subject
                var dialog = ProgressDialog(this@QRGenerateActivity)
                dialog.setMessage("Loading...")
                dialog.show()

                absentCount = 0

                var studentTable = FirebaseDatabase.getInstance().reference.child("ams").child("student")
                var studAry = ArrayList<Student>()
                studentTable.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(
                            this@QRGenerateActivity,
                            "Error occur" + p0.toException().toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        //var result =  dataSnapshot.getValue(Student::class.java)
                        var result: Student? = null
                        dataSnapshot.children.forEach {
                            result = it.getValue(Student::class.java)
                            Log.d(
                                "Tyler",
                                "student name is ${result!!.name}with sub ${result!!.subject}and subject name is : $todaySubCodeFromQR: $todaySubNameFromQR"
                            )
                            if (result!!.subject.contains("$todaySubCodeFromQR: $todaySubNameFromQR")) {
                                studAry.add(result!!)
                                Log.d("Tyler", "Student who take this course ${tvSubName.text} is:  ${result!!.name}")
                            } else {
                                Log.d("Tyler", "Student : not contain programming language")
                            }
                        }//end of for each

                        //getting the submitted attendance of today date
                        var attendanceTable =
                            FirebaseDatabase.getInstance().reference.child("ams").child("pendingattendance")
                        var attendAry = ArrayList<Attendance>()
                        attendanceTable.child(monthFromQR).child(todayDateFromQR).child(todaySubCodeFromQR)
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    Toast.makeText(
                                        this@QRGenerateActivity,
                                        "Error occur" + p0.toException().toString(),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    //var attendance:Attendance? = null
                                    //Log.d("Tyler","keyyyyyyyyy ${dataSnapshot.key }")
                                    var subjectCode = dataSnapshot.key
                                    dataSnapshot.children.forEach {

                                        var attendance = it.getValue(Attendance::class.java)
                                        attendance?.subjectcode = subjectCode!!
                                        attendAry.add(attendance!!)
                                        Log.d("Tyler", "Subject code from pending table: ${attendance.subjectcode}")
                                    }//end of datasnapshot


                                    //get the list of absent
                                    var absentAry = ArrayList<Student>()
                                    var isAbsent = true
                                    for (i in 0 until studAry.size) {
                                        //check if existing student mkpt is in attendance table
                                        var studForCheck = studAry[i]
                                        Log.d(
                                            "Tyler",
                                            "Student: this is for check student name: ${studForCheck.name} and mkpt ${studForCheck.mkpt}"
                                        )
                                        //looping attendance table
                                        for (a in 0 until attendAry.size) {
                                            //check and get absent student
                                            Log.d(
                                                "Tyler",
                                                "student mkpt in the attendance table is: ${attendAry[a].mkpt}"
                                            )
                                            if (studForCheck.mkpt == attendAry[a].mkpt) {
                                                Log.d("Tyler", "student ${studForCheck.name}is coming to school")
                                                isAbsent = false
                                                break

                                            } else {

                                                isAbsent = true
                                                Log.d(
                                                    "Tyler",
                                                    "student with ${studForCheck.mkpt} and name ${studForCheck.name} is absent"
                                                )

                                            }
                                        }//end of inner for
                                        //check

                                        if (isAbsent) {
                                            absentAry.add(studForCheck)
                                            absentCount += 1
                                        } else {
                                            Log.d("Tyler", "student ${studForCheck.name} isn't absent ")
                                        }
                                    }//end of outer for

                                    var absentStudents: String? = ""
                                    for (e in 0 until absentAry.size) {
                                        absentStudents = absentStudents + "-" + absentAry[e].name + "\n"
                                    }

                                    if (absentStudents.equals("")) {
                                        absentStudents = "No Absent!"
                                    }

                                    Log.d("Tyler", "student total array size is ${studAry.size}")
                                    absentStudents =
                                        absentStudents + "\nTotal student: ${studAry.size}\nAbsent student: ${absentCount}\nPresent Student: ${(studAry.size) - absentCount}\n"

                                    AlertDialog.Builder(this@QRGenerateActivity)
                                        .setTitle("Information")
                                        .setMessage("Absent students: \n$absentStudents")
                                        .setPositiveButton(
                                            "Submit",
                                            DialogInterface.OnClickListener { dialogInterface, i ->
                                                dialogInterface.dismiss()
                                                //submit all the current data in the attendance pending table to the real attendance table
                                                var realAttendanceTable =
                                                    FirebaseDatabase.getInstance().reference.child("ams")
                                                        .child("attendance")

                                                //loop the attendance array from the pending table getting from the above
                                                for (q in 0 until attendAry.size) {

                                                    realAttendanceTable.child(monthFromQR).child(todayDateFromQR)
                                                        .child(todaySubCodeFromQR).child(attendAry[q].mkpt)
                                                        .setValue(attendAry[q])
                                                }

                                                Toast.makeText(
                                                    this@QRGenerateActivity,
                                                    "Attendance data submitted.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                finish()
                                            })
                                        .setNegativeButton(
                                            "Cancel",
                                            DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
                                        .show()
                                    dialog.dismiss()
                                }//end of datasnapshot

                            })// end of getting attendance
                    }//end of data snapshot

                }) //end of getting all student data and filter those who are registered for this sub


            }//end of menu finish click
        }

        return super.onOptionsItemSelected(item)
    }

    @Throws(IOException::class)
    fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        var wallpaperDirectory: File = File(
            "${Environment.getExternalStorageDirectory()} $IMAGE_DIRECTORY"
        )
        // have the object build the directory structure, if needed.

        if (!wallpaperDirectory.exists()) {
            Log.d("dirrrrrr", "" + wallpaperDirectory.mkdirs())
            wallpaperDirectory.mkdirs()
            wallpaperDirectory.createNewFile()
        }

        try {
            val f: File = File(
                wallpaperDirectory,
                "${Calendar.getInstance().timeInMillis}  ${etqr!!.text} .jpg"
            )

            f.createNewFile()   //give read write permission
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                this,
                arrayOf(f.path),
                arrayOf("image/jpeg"), null
            )
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.absolutePath)

            return f.absolutePath

        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""

    }

    @Throws(WriterException::class)
    fun TextToImageEncode(contents: String?, format: BarcodeFormat, img_width: Int, img_height: Int): Bitmap? {
        if (contents == null) {
            return null
        }
        var hints: MutableMap<EncodeHintType, Any>? = null
        val encoding = guessAppropriateEncoding(contents)
        if (encoding != null) {
            hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.CHARACTER_SET] = encoding
        }
        val writer = MultiFormatWriter()
        val result: BitMatrix
        try {
            result = writer.encode(contents, format, img_width, img_height, hints)
        } catch (iae: IllegalArgumentException) {
            // Unsupported format
            return null
        }

        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (result.get(x, y)) BLACK else WHITE
            }
        }

        val bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    private fun guessAppropriateEncoding(contents: CharSequence): String? {
        // Very crude at the moment
        for (i in 0 until contents.length) {
            if (contents[i].toInt() > 0xFF) {
                return "UTF-8"
            }
        }
        return null
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_gen_qr, menu)
        return super.onCreateOptionsMenu(menu)
    }
}

package com.ucsm.tylersai.amsteacher.ui.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.adapter.TeachingSubjectAdapter
import com.ucsm.tylersai.amsteacher.model.Subject
import com.ucsm.tylersai.amsteacher.ui.activity.Main2Activity
import com.ucsm.tylersai.amsteacher.ui.activity.MainActivity
import java.text.SimpleDateFormat
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CollectAttendanceFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var tvTodayDate: TextView

    val DATEFORMAT = "dd MMMM yyyy"

    lateinit var sharedPreferences: SharedPreferences

    lateinit var subjectTable: DatabaseReference
    var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_collect_attendance, container, false)
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view_dashboard2)

        tvTodayDate = view.findViewById(R.id.today_date_tv2)

        subjectTable = FirebaseDatabase.getInstance().reference.child("ams").child("subject")

        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.teacherPref),
            Context.MODE_PRIVATE
        )

        val teacherId = sharedPreferences.getString(getString(R.string.prefid), null)

        lateinit var activity: Activity
        if (sharedPreferences.getString(getString(R.string.prefisdean), null) == "yes") {
            activity = getActivity() as Main2Activity
            activity.supportActionBar!!.title = "Collect Attendance"
        } else {
            activity = getActivity() as MainActivity
            activity.supportActionBar!!.title = "Collect Attendance"
        }

        recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view!!.context)
            // adapter = TeachingSubjectAdapter(dataset, view!!.context)

        }
        val dataset = ArrayList<Subject>()
        dataset.clear()

        subjectTable.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Error occured" + p0.toException().toString(), Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    val subjectchild = it.getValue(Subject::class.java)

                    Log.d(
                        "Tyler",
                        "teacher id is: $teacherId subject name is: ${subjectchild!!.name} ${subjectchild.teacherId} ${subjectchild.day} ${subjectchild.time}"
                    )

                    if (teacherId?.let { it1 -> subjectchild.teacherId.contains(it1) } == true) {

                        dataset.add(subjectchild)
                    }
                    recyclerView!!.adapter = TeachingSubjectAdapter(dataset, view!!.context)
                }

                progressDialog.dismiss()
            }

        })

        val dateFormat = SimpleDateFormat(DATEFORMAT)
        val c = Calendar.getInstance().time
        val today = dateFormat.format(c)
        tvTodayDate.text = today

        // Inflate the layout for this fragment
        return view
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

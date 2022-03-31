package com.ucsm.tylersai.amsteacher.ui.fragments

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.adapter.AddMedicalLeaveListViewAdapter
import com.ucsm.tylersai.amsteacher.model.MedicalLeave
import com.ucsm.tylersai.amsteacher.ui.activity.Main2Activity
import com.ucsm.tylersai.amsteacher.ui.activity.SeeAllStudentMedicalLeaveActivity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MedicalLeaveFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var btSeeAllData: Button
    lateinit var listViewMedicalLeaveStudent: ListView


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
        val view = inflater.inflate(R.layout.fragment_medical_leave_dean, container, false)

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        val activity = activity as Main2Activity
        activity.supportActionBar!!.title = "Medical Leave"

        btSeeAllData = view.findViewById(R.id.bt_see_all_data_medical_leave_dean)
        listViewMedicalLeaveStudent = view.findViewById(R.id.list_view_medical_leave_dean)

        val medicalLeaveArrayList = ArrayList<MedicalLeave>()

        //getting data from firebase
        val medicalLeaveTable =
            FirebaseDatabase.getInstance().reference.child("ams").child("medicalleave")
        medicalLeaveTable.orderByChild("progress").equalTo("pending")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(context, "Error occurred ${p0.message}", Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    medicalLeaveArrayList.clear()
                    if (dataSnapshot.exists()) {
                        //add to array list
                        dataSnapshot.children.forEach {
                            val medicalLeaveObj = it.getValue(MedicalLeave::class.java)
                            medicalLeaveArrayList.add(medicalLeaveObj!!)

                            //adapter
                            listViewMedicalLeaveStudent.adapter =
                                AddMedicalLeaveListViewAdapter(context!!, medicalLeaveArrayList)
                            progressDialog.dismiss()
                        }

                    } else {
                        progressDialog.dismiss()
                        Toast.makeText(context, "No data!", Toast.LENGTH_LONG).show()
                    }
                }
            })

        btSeeAllData.setOnClickListener {
            val intent = Intent(context, SeeAllStudentMedicalLeaveActivity::class.java)
            startActivity(intent)
        }



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
            MedicalLeaveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

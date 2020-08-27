package com.ucsm.tylersai.amsteacher

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
import com.ucsm.tylersai.amsteacher.adapter.AddTeacherListViewAdapter
import com.ucsm.tylersai.amsteacher.model.Teacher

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

lateinit var btAddTeacher: Button
lateinit var listViewTeacherList: ListView

lateinit var teacherListAry: ArrayList<Teacher>

class AddTeacherFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null


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
        var view = inflater.inflate(R.layout.fragment_add_teacher_dean, container, false)
        var progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        var activity = activity as Main2Activity
        activity.supportActionBar!!.title = "Add Teachers"

        btAddTeacher = view.findViewById(R.id.bt_add_add_teacher)
        listViewTeacherList = view.findViewById(R.id.list_view_teacher_list_add_teacher)

        teacherListAry = ArrayList<Teacher>()
        teacherListAry.clear()

        //get teacher data from firebase
        var teacherTable = FirebaseDatabase.getInstance().reference.child("ams").child("teacher")
        teacherTable.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Error occurred ${p0.message}", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                teacherListAry.clear()
                dataSnapshot.children.forEach {
                    var teacherObj = it.getValue(Teacher::class.java)
                    teacherListAry.add(teacherObj!!)

                    //set list view adapter and update ui
                    listViewTeacherList.adapter = AddTeacherListViewAdapter(context!!, teacherListAry)
                    progressDialog.dismiss()
                }
            }

        })


        btAddTeacher.setOnClickListener {
            var intent = Intent(context, AddTeacherDeanActivity::class.java)
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
            AddTeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}

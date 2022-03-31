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
import com.ucsm.tylersai.amsteacher.adapter.AddSubjectListViewAdapter
import com.ucsm.tylersai.amsteacher.model.Subject
import com.ucsm.tylersai.amsteacher.ui.activity.AddSubjectDeanActivity
import com.ucsm.tylersai.amsteacher.ui.activity.Main2Activity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddSubjectFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var btAddSubject: Button
    lateinit var listViewSubjectList: ListView

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
        val view = inflater.inflate(R.layout.fragment_add_subject_dean, container, false)

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        val activity = activity as Main2Activity
        activity.supportActionBar!!.title = "Add Subjects"

        btAddSubject = view.findViewById(R.id.bt_add_add_subject)
        listViewSubjectList = view.findViewById(R.id.list_view_subject_list_add_subject)

        val subjectAry = ArrayList<Subject>()
        subjectAry.clear()

        //getting data from database
        val subjectTable = FirebaseDatabase.getInstance().reference.child("ams").child("subject")
        subjectTable.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Error occurred ${p0.message}", Toast.LENGTH_LONG).show()

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                subjectAry.clear()
                dataSnapshot.children.forEach {
                    val subjectObj = it.getValue(Subject::class.java)
                    subjectAry.add(subjectObj!!)

                    listViewSubjectList.adapter = AddSubjectListViewAdapter(context!!, subjectAry)


                    progressDialog.dismiss()
                }
            }

        })

        btAddSubject.setOnClickListener {

            val intent = Intent(context, AddSubjectDeanActivity::class.java)
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
            AddSubjectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

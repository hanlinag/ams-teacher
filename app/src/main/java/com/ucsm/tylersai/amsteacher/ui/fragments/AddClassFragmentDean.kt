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
import com.ucsm.tylersai.amsteacher.adapter.AddClassListViewAdapter
import com.ucsm.tylersai.amsteacher.model.SectionClass
import com.ucsm.tylersai.amsteacher.ui.activity.AddClassDeanActivity
import com.ucsm.tylersai.amsteacher.ui.activity.Main2Activity


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddClassFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var btAddClass: Button
    lateinit var listViewClass: ListView

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
        val view = inflater.inflate(R.layout.fragment_add_class_dean, container, false)
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        val activity = activity as Main2Activity
        activity.supportActionBar!!.title = "Add Section Class"

        btAddClass = view.findViewById(R.id.bt_add_add_section_class_dean)

        listViewClass = view.findViewById(R.id.list_view_class_list_add_class)


        //get data from db
        val sectionclassTable =
            FirebaseDatabase.getInstance().reference.child("ams").child("sectionclass")
        sectionclassTable.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Error occurred ${p0.message}", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val sectionClassAry = ArrayList<SectionClass>()
                sectionClassAry.clear()
                dataSnapshot.children.forEach {
                    val sectionClass = it.getValue(SectionClass::class.java)
                    sectionClassAry.add(sectionClass!!)
                    listViewClass.adapter = AddClassListViewAdapter(context!!, sectionClassAry)
                    progressDialog.dismiss()
                }
            }

        })

        btAddClass.setOnClickListener {
            val intent = Intent(context, AddClassDeanActivity::class.java)
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
            AddClassFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

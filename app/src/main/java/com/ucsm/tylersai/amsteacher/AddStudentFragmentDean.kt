package com.ucsm.tylersai.amsteacher

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucsm.tylersai.amsteacher.adapter.PreAddStudentListViewAdapter
import com.ucsm.tylersai.amsteacher.model.PreStudentList

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddStudentFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var spinnerMajorClass: Spinner
    lateinit var btAddStudent: Button
    lateinit var listViewAddStudent: ListView

    lateinit var preStudentListAry: ArrayList<PreStudentList>

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
        var view = inflater.inflate(R.layout.fragment_add_student_dean, container, false)

        var progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        var activity = activity as Main2Activity
        activity.supportActionBar!!.title = "Add Students"

        spinnerMajorClass = view.findViewById(R.id.spinner_major_class_student)
        btAddStudent = view.findViewById(R.id.bt_add_student_student)
        listViewAddStudent = view.findViewById(R.id.pre_student_list_view_add_student)

        preStudentListAry = ArrayList<PreStudentList>()
        preStudentListAry.clear()

        //get section major class and add to drop down menu
        var sectionMajorClassAry = ArrayList<String?>()
        var sectionMajorClassTable = FirebaseDatabase.getInstance().reference.child("ams").child("sectionclass")
        sectionMajorClassTable.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Error occured ${p0.toException().message}", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                preStudentListAry.clear()
                dataSnapshot.children.forEach {
                    //var sectionClass =it.getValue(SectionClass::class.java)
                    sectionMajorClassAry.add(it.key)
                    // Log.d("Tyler","sectionMajorClassAry ${it.key}")

                    var spinnerAdapter = ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_spinner_dropdown_item,
                        sectionMajorClassAry
                    )
                    spinnerMajorClass.adapter = spinnerAdapter
                    var postition = spinnerAdapter.getPosition(sectionMajorClassAry[0])
                    spinnerMajorClass.setSelection(postition)


                    //getting all the student in the specific major
                    var prestudentTable = FirebaseDatabase.getInstance().reference.child("ams").child("prestudentlist")
                    prestudentTable.orderByChild("major").equalTo(sectionMajorClassAry[0])
                        .addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Toast.makeText(context, p0.message, Toast.LENGTH_LONG).show()
                            }


                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                preStudentListAry.clear()
                                dataSnapshot.children.forEach { it ->
                                    var prestudent = it.getValue(PreStudentList::class.java)
                                    //Log.d("Tyler", "prestudent is :::: ${prestudent!!.name}, key:: ${it.key}")

                                    //update to UI
                                    preStudentListAry.add(prestudent!!)
                                    listViewAddStudent.adapter =
                                        PreAddStudentListViewAdapter(context!!, preStudentListAry)


                                    //spinner action
                                    spinnerMajorClass.onItemSelectedListener =
                                        object : AdapterView.OnItemSelectedListener {
                                            override fun onNothingSelected(p0: AdapterView<*>?) {

                                            }

                                            override fun onItemSelected(
                                                p0: AdapterView<*>?,
                                                p1: View?,
                                                p: Int,
                                                p3: Long
                                            ) {
                                                progressDialog.show()
                                                //change ui according to the major class
                                                //get data from firebase
                                                var prestudentTable =
                                                    FirebaseDatabase.getInstance().reference.child("ams")
                                                        .child("prestudentlist")
                                                preStudentListAry.clear()
                                                prestudentTable.orderByChild("major")
                                                    .equalTo(spinnerMajorClass.selectedItem.toString())
                                                    .addValueEventListener(object : ValueEventListener {
                                                        override fun onCancelled(p0: DatabaseError) {
                                                            Toast.makeText(
                                                                context,
                                                                "Error occur" + p0.message,
                                                                Toast.LENGTH_LONG
                                                            )
                                                                .show()
                                                        }

                                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                            dataSnapshot.children.forEach {
                                                                var prestudent = it.getValue(PreStudentList::class.java)
                                                                //Log.d("Tyler", "prestudent is :::: ${prestudent!!.name}, key:: ${it.key}")

                                                                //update to UI
                                                                preStudentListAry.add(prestudent!!)
                                                                listViewAddStudent.adapter =
                                                                    PreAddStudentListViewAdapter(
                                                                        context!!,
                                                                        preStudentListAry
                                                                    )

                                                                progressDialog.dismiss()
                                                            }

                                                        }


                                                    })
                                            }//end of item selected

                                        }//end of listener

                                    progressDialog.dismiss()
                                }

                            }

                        })


                }

            }


        })




        btAddStudent.setOnClickListener {
            var intent = Intent(context, AddStudentDeanActivity::class.java)
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
            AddStudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

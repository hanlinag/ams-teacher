package com.ucsm.tylersai.amsteacher.ui.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.adapter.ViewStudentRecyclerViewAdapter
import com.ucsm.tylersai.amsteacher.model.Student
import com.ucsm.tylersai.amsteacher.ui.activity.Main2Activity
import java.text.SimpleDateFormat
import java.util.*


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ViewStudentFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var tvTodayDate: TextView

    val DATEFORMAT = "dd MMMM yyyy"

    lateinit var sharedPreferences: SharedPreferences
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
        val view = inflater.inflate(R.layout.fragment_view_student_dean, container, false)

        val activity = activity as Main2Activity
        activity.supportActionBar!!.title = "Dean Dashboard"

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view_student_dean)

        tvTodayDate = view.findViewById(R.id.today_date_tv_view_student_dean)

        sharedPreferences =
            activity.getSharedPreferences(getString(R.string.teacherPref), Context.MODE_PRIVATE)


        //show today date
        val dateFormat = SimpleDateFormat(DATEFORMAT)
        val c = Calendar.getInstance().time
        val today = dateFormat.format(c)
        tvTodayDate.text = today

        val dataset = ArrayList<Student>()

        //get student from db
        val studentTable = FirebaseDatabase.getInstance().reference.child("ams").child("student")
        studentTable.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "Error occurred ${p0.message}", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataset.clear()

                dataSnapshot.children.forEach {
                    val student = it.getValue(Student::class.java)
                    dataset.add(student!!)


                    recyclerView?.adapter = ViewStudentRecyclerViewAdapter(dataset, view!!.context)

                    progressDialog.dismiss()

                }
            }

        })

        recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view!!.context)


        }



        return view
    }


    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    /*override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }
*/
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
            ViewStudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

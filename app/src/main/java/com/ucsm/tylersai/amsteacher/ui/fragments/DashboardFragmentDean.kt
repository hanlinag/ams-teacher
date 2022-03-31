package com.ucsm.tylersai.amsteacher.ui.fragments

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.adapter.ClassPercentProgressAdapter
import com.ucsm.tylersai.amsteacher.model.ClassAttendanceProgress
import com.ucsm.tylersai.amsteacher.ui.activity.Main2Activity
import lecho.lib.hellocharts.model.PieChartData
import lecho.lib.hellocharts.model.SliceValue
import lecho.lib.hellocharts.view.PieChartView
import java.text.SimpleDateFormat
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DashboardFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var tvTodayDate: TextView

    val DATEFORMAT = "dd MMMM yyyy"
    lateinit var chartOverall: PieChartView

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
        val view = inflater.inflate(R.layout.fragment_dashboard_dean, container, false)

        val activity = activity as Main2Activity
        activity.supportActionBar!!.title = "Dean Dashboard"

        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading...")
        progressDialog.show()

        chartOverall = view.findViewById(R.id.subject_pie_chart_dean)

        recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view_dashboard_dean)

        tvTodayDate = view.findViewById(R.id.today_date_tv_dashboard_dean)

        sharedPreferences = activity.getSharedPreferences(getString(R.string.teacherPref), Context.MODE_PRIVATE)


        //calculating pie chart
        //var attendancePercentage = attendancePercentage.toFloat()
        val pieData = ArrayList<SliceValue>()
        pieData.add(SliceValue(80f, Color.argb(100, 222, 74, 91)))
        pieData.add(SliceValue((100F - 80f), Color.argb(100, 96, 153, 155)))
        val pieChartData = PieChartData(pieData)
        pieChartData.setHasCenterCircle(true).centerText1FontSize = 10
        pieChartData.setHasCenterCircle(true).centerText1 = "80%"

        chartOverall.pieChartData = pieChartData

        val dataset = ArrayList<ClassAttendanceProgress>()
        dataset.clear()
        dataset.add(ClassAttendanceProgress("Knowledge Engineering", "80"))
        dataset.add(ClassAttendanceProgress("Software Engineering", "70"))
        dataset.add(ClassAttendanceProgress("High Performance Computing", "80"))
        dataset.add(ClassAttendanceProgress("Business Information System", "85"))

        recyclerView?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(view!!.context)
            adapter = ClassPercentProgressAdapter(dataset, view.context)

        }


        //show today date
        val dateFormat = SimpleDateFormat(DATEFORMAT)
        val c = Calendar.getInstance().time
        val today = dateFormat.format(c)
        tvTodayDate.text = today

        progressDialog.dismiss()
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

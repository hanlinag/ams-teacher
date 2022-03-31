package com.ucsm.tylersai.amsteacher.ui.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ucsm.tylersai.amsteacher.R
import com.ucsm.tylersai.amsteacher.ui.activity.Main2Activity
import com.ucsm.tylersai.amsteacher.ui.activity.MainActivity

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CheckInFragment : Fragment(), OnMapReadyCallback {

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    lateinit var mapView: MapView
    private var gmap: GoogleMap? = null
    private var mapViewBundle: Bundle? = null

    private val MAP_VIEW_BUNDLE_KEY = "ADD YOU OWN API KEY HERE"

    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val LOCATION_REQUEST_CODE = 101

    lateinit var sharedPreferences: SharedPreferences
    lateinit var preferenceEditor: SharedPreferences.Editor

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

        val view = inflater.inflate(R.layout.fragment_check_in, container, false)

        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.teacherPref),
            Context.MODE_PRIVATE
        )

        var activity: Activity? = null
        if (sharedPreferences.getString(getString(R.string.prefisdean), null) == "yes") {
            activity = getActivity() as Main2Activity
            activity.supportActionBar!!.title = "Check In"
        } else {
            activity = getActivity() as MainActivity
            activity.supportActionBar!!.title = "Check In"
        }


        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView = view.findViewById(R.id.mapView)
        //get my current location
        try {
            fusedLocationProviderClient =
                if (sharedPreferences.getString(getString(R.string.prefisdean), null)
                        .equals("yes")
                ) {
                    //dean
                    LocationServices.getFusedLocationProviderClient((activity as Main2Activity))
                } else {
                    //other teacher
                    LocationServices.getFusedLocationProviderClient((activity as MainActivity))
                }


            fatchLastLocation()
            mapView.onCreate(mapViewBundle)
            mapView.getMapAsync(this)
        } catch (ex: Exception) {
            Toast.makeText(context, "Error occurred ${ex.message}", Toast.LENGTH_LONG).show()
        }


        return view
    }

    private fun fatchLastLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity().parent,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )

            return
        }

        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener {
            if (it != null) {

                currentLocation = it

                preferenceEditor = sharedPreferences.edit()
                preferenceEditor.putString(getString(R.string.prefLat), "${it.latitude}")
                preferenceEditor.putString(getString(R.string.prefLon), "${it.longitude}")
                preferenceEditor.commit()
                Log.d("Tyler", "current location is : ${it.longitude} ${it.latitude}")
                Toast.makeText(context, "current location is : ${it.longitude}, ${it.latitude}", Toast.LENGTH_LONG)
                    .show()
            }//end of if
        }
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
            CheckInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView.onSaveInstanceState(mapViewBundle)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fatchLastLocation()
                }
            }

        }

    }

    override fun onResume() {

        mapView.onResume()
        super.onResume()
    }

    override fun onStart() {

        mapView.onStart()
        super.onStart()
    }

    override fun onStop() {

        mapView.onStop()
        super.onStop()
    }

    override fun onPause() {

        mapView.onPause()
        super.onPause()

    }

    override fun onDestroy() {

        mapView.onDestroy()
        super.onDestroy()

    }

    override fun onLowMemory() {

        mapView.onLowMemory()
        super.onLowMemory()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        try {
            gmap = googleMap
            gmap!!.setMinZoomPreference(17f)

            val finalLocation = currentLocation

            Log.d(
                "Tyler",
                "final location is : ${finalLocation?.latitude} and ${finalLocation?.longitude}"
            )

            val latLng = LatLng(finalLocation!!.latitude, finalLocation.longitude)
            val markerOptions = MarkerOptions().position(latLng).title("I am here")

            //gmap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            // gmap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
            gmap!!.addMarker(markerOptions)
            gmap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
            gmap!!.mapType = GoogleMap.MAP_TYPE_NORMAL

            val uiSetting = gmap!!.uiSettings
            uiSetting.isRotateGesturesEnabled = true
            uiSetting.isScrollGesturesEnabled = true
            uiSetting.isTiltGesturesEnabled = true
            uiSetting.isZoomGesturesEnabled = true
        } catch (e: Exception) {
            Toast.makeText(context, "Error occur$e", Toast.LENGTH_LONG).show()
        }
    }
}

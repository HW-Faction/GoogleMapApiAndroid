package map.api.assignment.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_otp.*
import kotlinx.android.synthetic.main.fragment_phone_no.*
import map.api.assignment.GeoPosition
import map.api.assignment.MapActivity
import map.api.assignment.R

class OtpFragment : Fragment() {

    private var phoneNo: String? = null
    private val TAG = "OtpFragment.kt"

    private lateinit var mFusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phoneNo = it.get("phone_number").toString()
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_otp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        otp_desc_tv.text = "Otp for $phoneNo is 785922"

        login_btn.setOnClickListener {
            if (otp_et.text.isEmpty()) {
                otp_et.error = "Please enter otp"
            } else if (otp_et.text.length < 6) {
                otp_et.error = "Otp must be 6 digits"
            } else if (Integer.parseInt(otp_et.text.toString()) != 785922) {
                otp_et.error = "Incorrect otp"
            } else {
                getLastKnownLocation()
            }
        }
    }

    fun getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation: called.")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val location: Location = task.result
                Log.d(TAG, "onComplete: latitude: " + location.latitude)
                Log.d(TAG, "onComplete: longitude: " + location.longitude)

                val intent = Intent(requireContext(), MapActivity::class.java)
                intent.putExtra("lat", location.latitude)
                intent.putExtra("long", location.longitude)
                startActivity(intent)
            }
        }
    }
}
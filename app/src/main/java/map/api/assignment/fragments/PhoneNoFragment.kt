package map.api.assignment.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_phone_no.*
import map.api.assignment.R

class PhoneNoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_no, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        get_otp_btn.setOnClickListener {
            when {
                phone_number_et.text.isEmpty() -> {
                    phone_number_et.error = "Phone no can not be empty"
                }
                phone_number_et.text.length < 10 -> {
                    phone_number_et.error = "Phone no must be atleast 10 digits"
                }
                else -> {
                    findNavController().navigate(
                        PhoneNoFragmentDirections.actionFromPhoneNoFragToOtpFrag(
                            phone_number_et.text.toString()
                        )
                    )
                }
            }
        }
    }
}
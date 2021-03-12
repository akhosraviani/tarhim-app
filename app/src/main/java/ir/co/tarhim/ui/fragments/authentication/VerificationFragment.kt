package ir.co.tarhim.ui.fragments.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.navigation.NavDirections
import ir.co.tarhim.R
import ir.co.tarhim.utils.Timer
import kotlinx.android.synthetic.main.fragment_verification.*


class VerificationFragment : Fragment() {

    private val args: VerificationFragmentArgs by navArgs()
    private lateinit var timer: Timer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timer = Timer(verificationTimerTv)
        val userPhoneNumber = args.userPhoneNumber

        verificationStartDescriptionTv.text = resources.getString(
            R.string.verification_start_description,
            userPhoneNumber
        )
        
    }

}
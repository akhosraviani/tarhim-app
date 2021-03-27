package ir.co.tarhim.ui.fragments.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ir.co.tarhim.R
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_verification.*


class VerificationFragment : Fragment() {

    private lateinit var phoneNumber: String
    private var checkRecovery: Boolean = false
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        if (arguments != null) {
            phoneNumber = arguments?.getString("phoneNumber").toString()
            checkRecovery = arguments?.getBoolean("CheckRecovery")!!

        }

        verificationEnterTv.setOnClickListener {
            viewModel.requestConfirmOtp(
                ConfirmOtpRequest(
                    loginPhoneNumberEditText.text.toString(),
                    phoneNumber
                )
            )
        }

        viewModel.ldConfirmOtp.observe(viewLifecycleOwner, Observer { x ->
            when (x.code) {
                200 -> {
                    val args = bundleOf("phoneNumber" to phoneNumber)
                    findNavController().navigate(
                        R.id.action_verificationFragment_to_signInFragment,
                        args
                    )
                }
                400 -> {
                    Toast.makeText(context, "کد شما صحیح نیست", Toast.LENGTH_SHORT).show()
                }
            }

        })


    }

}
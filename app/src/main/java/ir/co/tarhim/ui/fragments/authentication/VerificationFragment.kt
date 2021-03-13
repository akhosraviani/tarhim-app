package ir.co.tarhim.ui.fragments.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.navigation.NavDirections
import androidx.lifecycle.Observer
import ir.co.tarhim.R
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.ui.AbstractFragment
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.Timer
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_verification.*


class VerificationFragment : AbstractFragment(){

    private val args: VerificationFragmentArgs by navArgs()
    private lateinit var timer: Timer
    private lateinit var viewModel: HomeViewModel

    override val layoutId: Int
        get() = R.layout.fragment_verification

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        timer = Timer(verificationTimerTv)
        val userPhoneNumber = args.userPhoneNumber

        verificationStartDescriptionTv.text = resources.getString(
            R.string.verification_start_description,
            userPhoneNumber
        )

        //for removing error layout
        verificationPhoneNumberEditText.doOnTextChanged { _, _, _, _ ->
            verificationPasswordLayout.setBackgroundResource(R.drawable.shape_login_edit_text_phone_number)
        }

        verificationEnterTv.setOnClickListener {
            viewModel.requestConfirmOtp(ConfirmOtpRequest(verificationPhoneNumberEditText.text.toString().toInt() , userPhoneNumber))
        }

        viewModel.ldConfirmOtp.observe(viewLifecycleOwner ,Observer{ x ->
            if(x.code==200){
                navigate(VerificationFragmentDirections.verificationToSignIn(loginEnterPhoneOrMailEt.text.toString() , false))
            }else{
                verificationPasswordLayout.setBackgroundResource(R.drawable.shape_purple_card)
            }

        } )
    }

}
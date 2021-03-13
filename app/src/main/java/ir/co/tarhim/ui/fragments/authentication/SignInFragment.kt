package ir.co.tarhim.ui.fragments.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import ir.co.tarhim.R
import ir.co.tarhim.model.password.PasswordRequest
import ir.co.tarhim.ui.AbstractFragment
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_verification.*


class SignInFragment :  AbstractFragment() {

    private val args: SignInFragmentArgs by navArgs()
    private lateinit var viewModel: HomeViewModel

    override val layoutId: Int
        get() = R.layout.fragment_sign_in


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val userPhoneNumber = args.userPhoneNumber
        val userState = args.userRegistered

        signInEnterTv.setOnClickListener {
            if(userState){
                viewModel.requestConfirmPassword(PasswordRequest(userPhoneNumber , signInPhoneNumberEditText.text.toString()))
            }else{
                viewModel.requestSetPassword(PasswordRequest(userPhoneNumber , signInPhoneNumberEditText.text.toString()))
            }
        }

        viewModel.ldsetPassword.observe(viewLifecycleOwner , Observer{ x ->
            if(x.code==200){
                //go to home
            }else{
                //set error for entered pass
                signInPasswordLayout.setBackgroundResource(R.drawable.shape_purple_card)

            }

        } )
        viewModel.ldConfirmPassword.observe(viewLifecycleOwner , Observer{ x ->
            if(x.code==200){
                //go to home
            }else{
                //set error for entered pass
                signInPhoneNumberEditText.setBackgroundResource(R.drawable.shape_purple_card)
            }

        } )


        signInCv.setBackgroundResource(R.drawable.shape_login_card_view_border)

        signInBack.setOnClickListener {
//            val fm: FragmentManager = requireActivity().supportFragmentManager
//            fm.popBackStack()
        }
        signInPhoneNumberEditText.doOnTextChanged { _, _, _, _ ->
                if(signInPhoneNumberEditText.text.toString().isEmpty()){
                    signInPhoneNumberEditText.setBackgroundResource(R.drawable.shape_login_edit_text_phone_number)
                }else{
                    signInPhoneNumberEditText.setBackgroundResource(R.drawable.shape_purple_card)
                }
                }

    }

}
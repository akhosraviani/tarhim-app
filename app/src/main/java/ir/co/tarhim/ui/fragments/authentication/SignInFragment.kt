package ir.co.tarhim.ui.fragments.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.R
import ir.co.tarhim.model.confirmpass.ConfirmPasswordRequest
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.ui.activities.AuthenticationActivity
import ir.co.tarhim.ui.activities.HomeActivity
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment() {

    companion object {
        private const val TAG = "SignInFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var phoneNumber: String
    private var Register: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        if (arguments != null) {
            phoneNumber = arguments?.getString("phoneNumber").toString()
            Register = arguments?.getBoolean("Register")!!
        }
        signInCv.setBackgroundResource(R.drawable.shape_login_card_view_border)

        signInRecoveryTv.setOnClickListener {
            val args = bundleOf("phoneNumber" to phoneNumber, "CheckRecovery" to true)
            viewModel.requestOtp(CheckPhoneNumber(phoneNumber))

            findNavController().navigate(R.id.action_signInFragment_to_verificationFragment, args)
        }

        signInBack.setOnClickListener {
            val fm: FragmentManager = requireActivity().supportFragmentManager
            fm.popBackStack()
        }

        loginPhoneNumberEditText.doOnTextChanged { _, _, _, _ ->
            if (loginPhoneNumberEditText.text.toString().isEmpty()) {
                loginPhoneNumberEditText.setBackgroundResource(R.drawable.shape_login_edit_text_phone_number)
            } else {
                loginPhoneNumberEditText.setBackgroundResource(R.drawable.shape_purple_card)
            }
        }

        Log.e(TAG, "onViewCreated: phoneNumber" + phoneNumber)
        signInEnterTv.setOnClickListener {
            if (Register) {
            Toast.makeText(activity, "register", Toast.LENGTH_SHORT).show()
                viewModel.requestConfirmPassword(
                    ConfirmPasswordRequest(
                        phoneNumber,
                        loginPhoneNumberEditText.text.toString()
                    )
                )
            }
            else{
                Toast.makeText(activity, "nonRegister", Toast.LENGTH_SHORT).show()

                viewModel.requestSetPassword(
                    ConfirmPasswordRequest(
                        phoneNumber,
                        loginPhoneNumberEditText.text.toString()
                    )
                )
            }
        }

        viewModel.ldSetPassword.observe(viewLifecycleOwner, Observer {
            if(it.code==200){
//                Hawk.put("UserNumber", phoneNumber)
                startActivity(Intent(requireContext(), HomeActivity::class.java))
            }
        })
        viewModel.ldConfirmPassword.observe(viewLifecycleOwner, Observer {

            Log.e(TAG, "onViewCreated: code " + it.code)
            if (it.code == 200) {

//                Hawk.put("UserNumber", phoneNumber)
             startActivity(Intent(requireContext(), HomeActivity::class.java))
            } else {
                Toast.makeText(activity, "رمز عبور نامعتبر میباشد", Toast.LENGTH_SHORT).show()
            }
        })


    }

}
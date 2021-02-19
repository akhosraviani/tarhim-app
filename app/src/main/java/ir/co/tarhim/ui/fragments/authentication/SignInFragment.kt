package ir.co.tarhim.ui.fragments.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import ir.co.tarhim.R
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signInCv.setBackgroundResource(R.drawable.shape_login_card_view_border)

        signInBack.setOnClickListener {
//            val fm: FragmentManager = requireActivity().supportFragmentManager
//            fm.popBackStack()
        }

        loginPhoneNumberEditText.doOnTextChanged { _, _, _, _ ->
                if(loginPhoneNumberEditText.text.toString().isEmpty()){
                    loginPhoneNumberEditText.setBackgroundResource(R.drawable.shape_login_edit_text_phone_number)
                }else{
                    loginPhoneNumberEditText.setBackgroundResource(R.drawable.shape_purple_card)
                }
                }



    }


}
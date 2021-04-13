//package ir.co.tarhim.ui.fragments.authentication
//
//
//import android.content.Intent
//import android.os.Bundle
//import android.text.SpannableString
//import android.text.Spanned
//import android.text.TextPaint
//import android.text.method.LinkMovementMethod
//import android.text.style.ClickableSpan
//import android.view.View
//import androidx.core.os.bundleOf
//import androidx.core.text.toSpannable
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.fragment.findNavController
//import com.orhanobut.hawk.Hawk
//import ir.co.tarhim.R
//import ir.co.tarhim.model.login.mobile.CheckPhoneNumber
//import ir.co.tarhim.ui.AbstractFragment
//import ir.co.tarhim.ui.activities.HomeActivity
//import ir.co.tarhim.ui.viewModels.HomeViewModel
//import kotlinx.android.synthetic.main.fragment_login.*
//
//
//class LoginFragment : AbstractFragment() {
//
//
//
//    private lateinit var viewModel: HomeViewModel
//
//    override val layoutId: Int
//        get() = R.layout.fragment_login
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        if (Hawk.get("UserNumber", null) != null) {
//            startActivity(Intent(requireContext(), HomeActivity::class.java))
//        } else {
//            viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//
//            btnSubmitEnter.setOnClickListener {
//                viewModel.requestCheckRegister(CheckPhoneNumber(loginEnterEt.text.toString()))
//            }
//
//            viewModel.ldSignUp.observe(viewLifecycleOwner, Observer { x ->
//                if (x.registered) {
//                    val args = bundleOf(
//                        "phoneNumber" to loginEnterEt.text.toString(),
//                        "Register" to true
//                    )
//                    findNavController().navigate(R.id.loginToSignIn, args)
//                } else {
//                    viewModel.requestOtp(CheckPhoneNumber(loginEnterEt.text.toString()))
//                }
//            })
//
//            viewModel.ldOtp.observe(viewLifecycleOwner, Observer { x ->
//                if (x.IsSuccessful) {
//                    val args = bundleOf("phoneNumber" to loginEnterEt.text.toString())
//                    findNavController().navigate(R.id.loginToVerification, args)
//
//                }
//            })
//
//        loginCv.setBackgroundResource(R.drawable.shape_login_card_view_border)
//
//
//        }
//
//
//    }
//
//
//}
package ir.co.tarhim.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpDataModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.model.otp.OtpDataModel
import ir.co.tarhim.ui.repository.LoginRepository

class HomeViewModel : ViewModel() {


    private var loginRepository: LoginRepository = LoginRepository()
    var ldSignUp: MutableLiveData<CheckRegisterModel>
    var ldOtp: MutableLiveData<OtpDataModel>
    var ldConfirmOtp: MutableLiveData<ConfirmOtpDataModel>

    init {
        ldSignUp = loginRepository.mldSignUp
        ldOtp = loginRepository.mldOtp
        ldConfirmOtp = loginRepository.mldConfirmOtp
    }

    fun requestSignUp(checkRegisterRequest: CheckPhoneNumber): MutableLiveData<CheckRegisterModel> {
        ldSignUp = loginRepository.requestSignUp(checkRegisterRequest)
        Log.i("testTag" ,"hi model view" + ldSignUp)
        return ldSignUp

    }

    fun requestOtp(otpRequest: CheckPhoneNumber): MutableLiveData<OtpDataModel> {
        ldOtp = loginRepository.requestOtp(otpRequest)
        Log.i("testTag" ,"hi model view" + ldOtp)
        return ldOtp

    }

    fun requestConfirmOtp(confirmOtpRequest: ConfirmOtpRequest): MutableLiveData<ConfirmOtpDataModel>{
        ldConfirmOtp = loginRepository.confirmOtp(confirmOtpRequest)
        Log.i("testTag" ,"hi model view" + ldConfirmOtp)
        return ldConfirmOtp
    }
}

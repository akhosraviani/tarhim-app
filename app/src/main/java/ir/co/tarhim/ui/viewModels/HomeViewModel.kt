package ir.co.tarhim.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpDataModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.model.otp.OtpDataModel
import ir.co.tarhim.model.password.PasswordDataModel
import ir.co.tarhim.model.password.PasswordRequest
import ir.co.tarhim.ui.repository.LoginRepository

class HomeViewModel : ViewModel() {


    private var loginRepository: LoginRepository = LoginRepository()
    var ldSignUp: MutableLiveData<CheckRegisterModel>
    var ldOtp: MutableLiveData<OtpDataModel>
    var ldConfirmOtp: MutableLiveData<ConfirmOtpDataModel>
    var ldsetPassword: MutableLiveData<PasswordDataModel>
    var ldConfirmPassword: MutableLiveData<PasswordDataModel>

    init {
        ldSignUp = loginRepository.mldSignUp
        ldOtp = loginRepository.mldOtp
        ldConfirmOtp = loginRepository.mldConfirmOtp
        ldsetPassword = loginRepository.mldSetPassword
        ldConfirmPassword = loginRepository.mldConfirmPassword
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

    fun requestSetPassword(setPasswordRequest: PasswordRequest): MutableLiveData<PasswordDataModel>{
        ldsetPassword = loginRepository.setPassword(setPasswordRequest)
        Log.i("testTag" ,"hi model view" + ldsetPassword)
        return ldsetPassword
    }

    fun requestConfirmPassword(setConfirmPasswordRequest: PasswordRequest): MutableLiveData<PasswordDataModel>{
        ldConfirmPassword = loginRepository.confirmPassword(setConfirmPasswordRequest)
        Log.i("testTag" ,"hi model view" + ldConfirmPassword)
        return ldConfirmPassword
    }
}

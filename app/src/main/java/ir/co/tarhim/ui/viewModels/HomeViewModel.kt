package ir.co.tarhim.ui.viewModels

import android.mtp.MtpObjectInfo
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.model.confirmotp.ConfirmOtpDataModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.confirmpass.ConfirmPasswordDataModel
import ir.co.tarhim.model.confirmpass.ConfirmPasswordRequest
import ir.co.tarhim.model.confirmpass.SetPasswordDataModel
import ir.co.tarhim.model.deceased.DeceasedDataModel
import ir.co.tarhim.model.deceased.LatestDeceasedDataModel
import ir.co.tarhim.model.deceased.SearchDeceasedDataModel
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.otp.OtpDataModel
import ir.co.tarhim.ui.repository.LoginRepository

class HomeViewModel : ViewModel() {


    private var loginRepository: LoginRepository = LoginRepository()
    var ldSignUp: MutableLiveData<CheckRegisterModel>
    var ldOtp: MutableLiveData<OtpDataModel>
    var ldConfirmOtp: MutableLiveData<ConfirmOtpDataModel>
    var ldConfirmPassword: MutableLiveData<ConfirmPasswordDataModel>
    var ldSetPassword: MutableLiveData<ConfirmPasswordDataModel>
    var ldSearch: MutableLiveData<List<DeceasedDataModel>>
    var ldLatestSearch: MutableLiveData<LatestDeceasedDataModel>
    var ldDeceasedProfile: MutableLiveData<DeceasedDataModel>

    init {
        ldSignUp = loginRepository.mldSignUp
        ldOtp = loginRepository.mldOtp
        ldConfirmOtp = loginRepository.mldConfirmOtp
        ldConfirmPassword = loginRepository.mldConfirmPassword
        ldSetPassword = loginRepository.mldConfirmSetPassword
        ldSearch=loginRepository.mldSearchList
        ldLatestSearch=loginRepository.mldLatestSearch
        ldDeceasedProfile=loginRepository.mldDeceaedProfile
    }

    fun requestCheckRegister(checkRegisterRequest: CheckPhoneNumber) {
         loginRepository.requestCheckRegister(checkRegisterRequest)
        Log.i("testTag", "hi model view" + ldSignUp)


    }

    fun requestOtp(otpRequest: CheckPhoneNumber) {
        loginRepository.requestOtp(otpRequest)
        Log.i("testTag", "hi model view" + ldOtp)
    }
    fun requestSearch(keyword: String) {
        loginRepository.requestSearch(keyword)
    }
    fun requestlatestSearch(mobile: String) {
        loginRepository.requestLatestSearch(mobile)
    }
    fun requestDeceasedProfile(id:Int) {
        loginRepository.requestDeceasedProfile(id,Hawk.get("UserNumber"))

    }

    fun requestConfirmPassword(confirmPassword: ConfirmPasswordRequest) {
        loginRepository.confirmPassword(confirmPassword)
    }

    fun requestSetPassword(setPassword: ConfirmPasswordRequest) {
        loginRepository.setPassword(setPassword)
    }

    fun requestConfirmOtp(confirmOtpRequest: ConfirmOtpRequest) {
       loginRepository.confirmOtp(confirmOtpRequest)
        Log.i("testTag", "hi model view" + ldConfirmOtp)

    }
}

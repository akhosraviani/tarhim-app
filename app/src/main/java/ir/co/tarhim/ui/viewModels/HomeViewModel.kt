package ir.co.tarhim.ui.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.mobile.CheckRegisterRequest
import ir.co.tarhim.ui.repository.LoginRepository

class HomeViewModel : ViewModel() {


    private var loginRepository: LoginRepository = LoginRepository()
    var ldSignUp: MutableLiveData<CheckRegisterModel>

    init {
        ldSignUp = loginRepository.mldSignUp
    }

    fun requestSignUp(checkRegisterRequest: CheckRegisterRequest): MutableLiveData<CheckRegisterModel> {
        ldSignUp = loginRepository.requestSignUp(checkRegisterRequest)
        Log.i("testTag" ,"hi model view" + ldSignUp)
        return ldSignUp

    }
}

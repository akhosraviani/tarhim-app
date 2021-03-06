package ir.co.tarhim.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ir.co.tarhim.ui.repository.LoginRepositoy
import okhttp3.ResponseBody
import kotlin.math.log

class HomeViewModel:ViewModel() {


    private lateinit var loginRepositoy: LoginRepositoy
    public lateinit var ldSignUp: LiveData<ResponseBody>
    init {
        loginRepositoy= LoginRepositoy()
        ldSignUp=loginRepositoy.mldSignUp
    }

    fun  requestSignUp(number:String){
        loginRepositoy.requestSignUp(number)
    }
}
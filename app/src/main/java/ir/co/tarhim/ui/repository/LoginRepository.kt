package ir.co.tarhim.ui.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.mobile.CheckRegisterRequest
import ir.co.tarhim.network.RequestClient
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {


    companion object {
        private const val TAG = "LoginRepository"
    }

    val mldSignUp = MutableLiveData<CheckRegisterModel>()
    fun requestSignUp(checkRegisterRequest: CheckRegisterRequest): MutableLiveData<CheckRegisterModel> {

        RequestClient.makeRequest().requestSignUp(checkRegisterRequest)
            .enqueue(object : Callback<CheckRegisterModel> {
                override fun onFailure(call: retrofit2.Call<CheckRegisterModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<CheckRegisterModel>,
                    response: Response<CheckRegisterModel>
                ) {
                  mldSignUp.value=  CheckRegisterModel(response.body()!!.registered)
                       Log.i("testTag","im here="+response.body())
                }
            })
        return mldSignUp
    }

}
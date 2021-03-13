package ir.co.tarhim.ui.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ir.co.tarhim.model.confirmotp.ConfirmOtpDataModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.model.otp.OtpDataModel
import ir.co.tarhim.network.RequestClient
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {


    companion object {
        private const val TAG = "LoginRepository"
    }

    val mldSignUp = MutableLiveData<CheckRegisterModel>()
    val mldOtp = MutableLiveData<OtpDataModel>()
    val mldConfirmOtp = MutableLiveData<ConfirmOtpDataModel>()

    fun requestCheckRegister(checkRegisterRequest: CheckPhoneNumber): MutableLiveData<CheckRegisterModel> {
        RequestClient.makeRequest().requestCheckRegister(checkRegisterRequest)
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

    fun requestOtp(checkOtpRequest: CheckPhoneNumber): MutableLiveData<OtpDataModel>{
        RequestClient.makeRequest().requestOtp(checkOtpRequest)
            .enqueue(object : Callback<OtpDataModel> {
                override fun onFailure(call: retrofit2.Call<OtpDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }
                override fun onResponse(
                    call: retrofit2.Call<OtpDataModel>,
                    response: Response<OtpDataModel>
                ) {
                    mldOtp.value=  OtpDataModel(response.body()!!.IsSuccessful)
                    Log.i("testTag","im here="+response.body())
                }
            })
        return mldOtp
    }

    fun confirmOtp(confirmOtpRequest: ConfirmOtpRequest): MutableLiveData<ConfirmOtpDataModel>{
        RequestClient.makeRequest().confirmOtp(confirmOtpRequest)
            .enqueue(object : Callback<ConfirmOtpDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmOtpDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }
                override fun onResponse(
                    call: retrofit2.Call<ConfirmOtpDataModel>,
                    response: Response<ConfirmOtpDataModel>
                ) {
                    mldConfirmOtp.value=  ConfirmOtpDataModel(response.body()!!.message , response.body()!!.code )
                    Log.i("testTag","im here="+response.body())
                }
            })
        return mldConfirmOtp
    }

}
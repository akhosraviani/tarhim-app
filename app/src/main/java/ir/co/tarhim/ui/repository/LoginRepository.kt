package ir.co.tarhim.ui.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ir.co.tarhim.model.confirmotp.ConfirmOtpDataModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.confirmpass.ConfirmPasswordDataModel
import ir.co.tarhim.model.confirmpass.ConfirmPasswordRequest
import ir.co.tarhim.model.deceased.DeceasedDataModel
import ir.co.tarhim.model.deceased.LatestDeceasedDataModel
import ir.co.tarhim.model.deceased.SearchDeceasedDataModel
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.otp.OtpDataModel
import ir.co.tarhim.network.RequestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {


    companion object {
        private const val TAG = "LoginRepository"
    }

    val mldSignUp = MutableLiveData<CheckRegisterModel>()
    val mldOtp = MutableLiveData<OtpDataModel>()
    val mldConfirmOtp = MutableLiveData<ConfirmOtpDataModel>()
    val mldConfirmPassword = MutableLiveData<ConfirmPasswordDataModel>()
    val mldConfirmSetPassword = MutableLiveData<ConfirmPasswordDataModel>()
    val mldLatestSearch = MutableLiveData<LatestDeceasedDataModel>()
    val mldSearchList = MutableLiveData<List<DeceasedDataModel>>()
    val mldDeceaedProfile = MutableLiveData<DeceasedDataModel>()

    fun requestCheckRegister(checkRegisterRequest: CheckPhoneNumber) {
        RequestClient.makeRequest().requestCheckRegister(checkRegisterRequest)
            .enqueue(object : Callback<CheckRegisterModel> {
                override fun onFailure(call: retrofit2.Call<CheckRegisterModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<CheckRegisterModel>,
                    response: Response<CheckRegisterModel>
                ) {
                    mldSignUp.value = CheckRegisterModel(response.body()!!.registered)
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }
    fun requestLatestSearch(mobile: String) {
        RequestClient.makeRequest().requestLatestSearch(mobile)
            .enqueue(object : Callback<LatestDeceasedDataModel> {
                override fun onFailure(call: retrofit2.Call<LatestDeceasedDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<LatestDeceasedDataModel>,
                    response: Response<LatestDeceasedDataModel>
                ) {
                    mldLatestSearch.value =response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }
    fun requestSearch(keyword:String) {
        RequestClient.makeRequest().requestSearch(SearchDeceasedDataModel(keyword))
            .enqueue(object : Callback<List<DeceasedDataModel>> {
                override fun onFailure(call: retrofit2.Call<List<DeceasedDataModel>>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<List<DeceasedDataModel>>,
                    response: Response<List<DeceasedDataModel>>
                ) {
                    mldSearchList.value =response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }
    fun requestDeceasedProfile(id:Int,mobile:String) {
        RequestClient.makeRequest().requestDeceaedProfile(id,mobile)
            .enqueue(object : Callback<DeceasedDataModel> {
                override fun onFailure(call: retrofit2.Call<DeceasedDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<DeceasedDataModel>,
                    response: Response<DeceasedDataModel>
                ) {
                    mldDeceaedProfile.value =response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun requestOtp(checkOtpRequest: CheckPhoneNumber) {
        RequestClient.makeRequest().requestOtp(checkOtpRequest)
            .enqueue(object : Callback<OtpDataModel> {
                override fun onFailure(call: retrofit2.Call<OtpDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<OtpDataModel>,
                    response: Response<OtpDataModel>
                ) {
                    mldOtp.value = OtpDataModel(response.body()!!.IsSuccessful)
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun confirmOtp(confirmOtpRequest: ConfirmOtpRequest) {
        RequestClient.makeRequest().confirmOtp(confirmOtpRequest)
            .enqueue(object : Callback<ConfirmOtpDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmOtpDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmOtpDataModel>,
                    response: Response<ConfirmOtpDataModel>
                ) {
                    mldConfirmOtp.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })
    }

    fun confirmPassword(confirmPassword: ConfirmPasswordRequest) {
        RequestClient.makeRequest().confirmPassword(confirmPassword)
            .enqueue(object : Callback<ConfirmPasswordDataModel> {
                override fun onResponse(
                    call: Call<ConfirmPasswordDataModel>,
                    response: Response<ConfirmPasswordDataModel>
                ) {
                    mldConfirmPassword.value =
                        ConfirmPasswordDataModel(response.code(), response.message())
                }

                override fun onFailure(call: Call<ConfirmPasswordDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }
            })
    }

    fun setPassword(confirmPassword: ConfirmPasswordRequest) {
        RequestClient.makeRequest().setPassword(confirmPassword)
            .enqueue(object : Callback<ConfirmPasswordDataModel> {

                override fun onFailure(call: Call<ConfirmPasswordDataModel>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(
                    call: Call<ConfirmPasswordDataModel>,
                    response: Response<ConfirmPasswordDataModel>
                ) {
                    mldConfirmSetPassword.value =
                        ConfirmPasswordDataModel(response.code(), response.message())
                }
            })
    }

}



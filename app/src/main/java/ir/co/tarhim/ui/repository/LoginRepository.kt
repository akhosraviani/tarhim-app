package ir.co.tarhim.ui.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ir.co.tarhim.model.confirmotp.ConfirmOtpDataModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.confirmpass.ConfirmDataModel
import ir.co.tarhim.model.confirmpass.ConfirmPasswordRequest
import ir.co.tarhim.model.deceased.*
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.otp.OtpDataModel
import ir.co.tarhim.network.RequestClient
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {


    companion object {
        private const val TAG = "LoginRepository"
    }

    val mldSignUp = MutableLiveData<CheckRegisterModel>()
    val mldOtp = MutableLiveData<OtpDataModel>()
    val mldConfirmOtp = MutableLiveData<ConfirmDataModel>()
    val mldCreateDeceased = MutableLiveData<ConfirmDataModel>()
    val mldConfirmPassword = MutableLiveData<ConfirmDataModel>()
    val mldConfirmSetPassword = MutableLiveData<ConfirmDataModel>()
    val mldLatestSearch = MutableLiveData<List<DeceasedDataModel>>()
    val mldSearchList = MutableLiveData<List<DeceasedDataModel>>()
    val mldDeceaedProfile = MutableLiveData<DeceasedProfileDataModel>()
    val mldMyDeceaed = MutableLiveData<List<MyDeceasedDataModel>>()
    val mldUploadImage = MutableLiveData<UploadFileDataModel>()
    val mldGetGallery = MutableLiveData<GalleryDataModel>()
    val mldGetComment = MutableLiveData<List<CommentDataModel>>()
    val mldSendComment = MutableLiveData<ConfirmDataModel>()

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
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestSendComment(body: SendCommentRequest, mobile: String) {
        RequestClient.makeRequest().requestSendComment(body, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldSendComment.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun requestGetComment(id: Int, mobile: String) {
        RequestClient.makeRequest().requestGetComment(id, mobile)
            .enqueue(object : Callback<List<CommentDataModel>> {
                override fun onFailure(call: retrofit2.Call<List<CommentDataModel>>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<List<CommentDataModel>>,
                    response: Response<List<CommentDataModel>>
                ) {
                    mldGetComment.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun requestGetGallery(id: Int) {
        RequestClient.makeRequest().requestGetGallery(id)
            .enqueue(object : Callback<GalleryDataModel> {
                override fun onFailure(call: retrofit2.Call<GalleryDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure getGallery: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<GalleryDataModel>,
                    response: Response<GalleryDataModel>
                ) {
                    mldGetGallery.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }


    fun requestUploadImage(file: MultipartBody.Part) {
        RequestClient.makeRequest().requestUploadFile(file)
            .enqueue(object : Callback<UploadFileDataModel> {
                override fun onFailure(call: retrofit2.Call<UploadFileDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<UploadFileDataModel>,
                    response: Response<UploadFileDataModel>
                ) {
                    mldUploadImage.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun requestMyDeceaed(mobile: String) {
        RequestClient.makeRequest().requestMyDeceased(mobile)
            .enqueue(object : Callback<List<MyDeceasedDataModel>> {
                override fun onFailure(
                    call: retrofit2.Call<List<MyDeceasedDataModel>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<List<MyDeceasedDataModel>>,
                    response: Response<List<MyDeceasedDataModel>>
                ) {
                    mldMyDeceaed.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun requestCreateDeceaed(dataRequest: CreateDeceasedRequest, mobile: String) {
        RequestClient.makeRequest().requestCreateDeceased(dataRequest, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(
                    call: retrofit2.Call<ConfirmDataModel>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldCreateDeceased.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun requestEditDeceaed(dataRequest: CreateDeceasedRequest, id: Int, mobile: String) {
        RequestClient.makeRequest().requestEditDeceased(dataRequest, id, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(
                    call: retrofit2.Call<ConfirmDataModel>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldCreateDeceased.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun requestLatestSearch(mobile: String) {
        RequestClient.makeRequest().requestLatestSearch(mobile)
            .enqueue(object : Callback<List<DeceasedDataModel>> {
                override fun onFailure(
                    call: retrofit2.Call<List<DeceasedDataModel>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<List<DeceasedDataModel>>,
                    response: Response<List<DeceasedDataModel>>
                ) {
                    mldLatestSearch.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun requestSearch(keyword: String) {
        RequestClient.makeRequest().requestSearch(SearchDeceasedRequest(keyword))
            .enqueue(object : Callback<List<DeceasedDataModel>> {
                override fun onFailure(
                    call: retrofit2.Call<List<DeceasedDataModel>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<List<DeceasedDataModel>>,
                    response: Response<List<DeceasedDataModel>>
                ) {
                    mldSearchList.postValue(response.body())
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun requestDeceasedProfile(id: Int, mobile: String) {
        RequestClient.makeRequest().requestDeceaedProfile(id, mobile)
            .enqueue(object : Callback<DeceasedProfileDataModel> {
                override fun onFailure(
                    call: retrofit2.Call<DeceasedProfileDataModel>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<DeceasedProfileDataModel>,
                    response: Response<DeceasedProfileDataModel>
                ) {
                    mldDeceaedProfile.value = response.body()
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
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    Log.i(TAG, "im here=" + ConfirmDataModel(response.message(),response.code()).toString())
                    response.body().let {
                        mldConfirmOtp.value = ConfirmDataModel(response.message(), response.code())
                        Log.i("testTag", "im here=" + it?.code)
                    }
                }
            })
    }

    fun confirmPassword(confirmPassword: ConfirmPasswordRequest) {
        RequestClient.makeRequest().confirmPassword(confirmPassword)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onResponse(
                    call: Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldConfirmPassword.value =
                        ConfirmDataModel(response.message(), response.code())
                }

                override fun onFailure(call: Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                }
            })
    }

    fun setPassword(confirmPassword: ConfirmPasswordRequest) {
        RequestClient.makeRequest().setPassword(confirmPassword)
            .enqueue(object : Callback<ConfirmDataModel> {

                override fun onFailure(call: Call<ConfirmDataModel>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(
                    call: Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldConfirmSetPassword.value =
                        ConfirmDataModel(response.message(), response.code())
                }
            })
    }

}



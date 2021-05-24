package ir.co.mazar.ui.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.type.LatLng
import ir.co.mazar.model.ConfirmDataModel
import ir.co.mazar.model.RemindeRequestModel
import ir.co.mazar.model.deceased.*
import ir.co.mazar.model.deceased.setting.SettingDataModel
import ir.co.mazar.model.login.confirmotp.ConfirmOtpRequest
import ir.co.mazar.model.login.confirmpass.ConfirmPasswordRequest
import ir.co.mazar.model.login.mobile.CheckPhoneNumberRequest
import ir.co.mazar.model.login.mobile.CheckRegisterModel
import ir.co.mazar.model.login.otp.OtpDataModel
import ir.co.mazar.model.map.MapMatching
import ir.co.mazar.model.news.NewsDataModel
import ir.co.mazar.model.user.RegisterUser
import ir.co.mazar.model.user.UserInfoDataModel
import ir.co.mazar.network.MapRequest
import ir.co.mazar.network.RequestClient
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
    val mldError = MutableLiveData<Throwable>()
    val mldNotification = MutableLiveData<List<NotifDataModel>>()
    val mldInBox = MutableLiveData<List<MyInboxDataModel>>()
    val mldConfirmOtp = MutableLiveData<ConfirmDataModel>()
    val mldCreateDeceased = MutableLiveData<userRedirect>()
    val mldEditDeceased = MutableLiveData<ConfirmDataModel>()
    val mldConfirmPassword = MutableLiveData<ConfirmDataModel>()
    val mldConfirmSetPassword = MutableLiveData<ConfirmDataModel>()
    val mldLatestSearch = MutableLiveData<List<DeceasedDataModel>>()
    val mldSearchList = MutableLiveData<List<DeceasedDataModel>>()
    val mldDeceaedProfile = MutableLiveData<DeceasedProfileDataModel>()
    val mldDeceaedFromSearch = MutableLiveData<DeceasedProfileDataModel>()
    val mldMyDeceaed = MutableLiveData<List<MyDeceasedDataModel>>()
    val mldGetSiritual = MutableLiveData<List<PrayDeceasedDataModel>>()
    val mldUploadImage = MutableLiveData<UploadFileDataModel>()
    val mldGetGallery = MutableLiveData<List<GalleryDataModel>>()
    val mldGetComment = MutableLiveData<List<CommentDataModel>>()
    val mldSendComment = MutableLiveData<ConfirmDataModel>()
    val mldRegisterUser = MutableLiveData<ConfirmDataModel>()
    val mldUserInfo = MutableLiveData<UserInfoDataModel>()
    val mldNews = MutableLiveData<List<NewsDataModel>>()
    val mldFollow = MutableLiveData<ConfirmDataModel>()
    val mldUnFollow = MutableLiveData<ConfirmDataModel>()
    val mldInvite = MutableLiveData<ConfirmDataModel>()
    val mldSiritual = MutableLiveData<ConfirmDataModel>()
    val mldPostGallery = MutableLiveData<ConfirmDataModel>()
    val mldPray = MutableLiveData<List<RequirementDataModel>>()
    val mldCharity = MutableLiveData<List<CharityDataModel>>()
    val mldSendPray = MutableLiveData<ConfirmDataModel>()
    val mldDeleteComment = MutableLiveData<ConfirmDataModel>()
    val mldFollowers = MutableLiveData<List<FollowersDataModel>>()
    val mldFollowing = MutableLiveData<List<MyDeceasedDataModel>>()
    val mldDeleteLatest = MutableLiveData<ConfirmDataModel>()
    val mldReport = MutableLiveData<ConfirmDataModel>()
    val mldUnFollowDeceased = MutableLiveData<ConfirmDataModel>()
    val mldAcceptRequest = MutableLiveData<ConfirmDataModel>()
    val mldRejectRequest = MutableLiveData<ConfirmDataModel>()
    val mldReminder = MutableLiveData<ConfirmDataModel>()
    val mldSetting = MutableLiveData<SettingDataModel>()
    val mldDeceasedFollowers = MutableLiveData<List<FollowersDataModel>>()
    val mldMap = MutableLiveData<MapMatching>()

    fun requestSendPray(checkRegisterRequest: CheckPhoneNumberRequest) {
        RequestClient.makeRequest().requestCheckRegister(checkRegisterRequest)
            .enqueue(object : Callback<CheckRegisterModel> {
                override fun onFailure(call: retrofit2.Call<CheckRegisterModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
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

    fun requestReminder(body: RemindeRequestModel) {
        RequestClient.makeRequest().requestReminder(body)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldReminder.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestNotification(mobile: String) {
        RequestClient.makeRequest().requestNotification(mobile)
            .enqueue(object : Callback<List<NotifDataModel>> {
                override fun onFailure(
                    call: retrofit2.Call<List<NotifDataModel>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<List<NotifDataModel>>,
                    response: Response<List<NotifDataModel>>
                ) {
                    mldNotification.value = response.body()
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestFollowing(mobile: String) {
        RequestClient.makeRequest().requestFollowingList(mobile)
            .enqueue(object : Callback<List<MyDeceasedDataModel>> {
                override fun onFailure(
                    call: retrofit2.Call<List<MyDeceasedDataModel>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<List<MyDeceasedDataModel>>,
                    response: Response<List<MyDeceasedDataModel>>
                ) {
                    mldFollowing.value = response.body()!!
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestDeleteLatestItem(deceasedId: Int, mobile: String) {
        RequestClient.makeRequest().requestDeleteLatestItem(deceasedId, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldDeleteLatest.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestDeceasedFollowersList(deceasedId: Int) {
        RequestClient.makeRequest().requesDeceasedFollowersList(deceasedId)
            .enqueue(object : Callback<List<FollowersDataModel>> {
                override fun onFailure(
                    call: retrofit2.Call<List<FollowersDataModel>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<List<FollowersDataModel>>,
                    response: Response<List<FollowersDataModel>>
                ) {
                    mldDeceasedFollowers.value = response.body()
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestDeleteComment(body: DeleteCommentRequestModel, mobile: String) {
        RequestClient.makeRequest().requestDeleteComment(body, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldDeleteComment.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestAcceptRequest(mobile: String, notificationId: Int) {
        RequestClient.makeRequest().requestAcceptRequest(mobile, notificationId)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldAcceptRequest.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestRejectRequest(mobile: String, notificationId: Int) {
        RequestClient.makeRequest().requestRejectRequest(mobile, notificationId)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldRejectRequest.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestSetting(mobile: String, token: String) {
        RequestClient.makeRequest().requestSetting(mobile, token)
            .enqueue(object : Callback<SettingDataModel> {
                override fun onFailure(call: retrofit2.Call<SettingDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<SettingDataModel>,
                    response: Response<SettingDataModel>
                ) {
                    mldSetting.value = response.body()
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }


    fun requestSiritual(mobile: String, body: PrayDeceasedRequest) {
        RequestClient.makeRequest().requestSiritual(mobile, body)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldSiritual.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestGetSiritualRes(id: Int, mobile: String) {
        RequestClient.makeRequest().requestGetSiritualDeceased(id, mobile)
            .enqueue(object : Callback<List<PrayDeceasedDataModel>> {
                override fun onFailure(
                    call: retrofit2.Call<List<PrayDeceasedDataModel>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<List<PrayDeceasedDataModel>>,
                    response: Response<List<PrayDeceasedDataModel>>
                ) {
                    mldGetSiritual.value = response.body()
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestReport(mobile: String, body: ReportRequest) {
        RequestClient.makeRequest().requestReport(mobile, body)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldReport.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestSendPray(mobile: String, body: PrayDataRequest) {
        RequestClient.makeRequest().requestSendPray(mobile, body)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldSendPray.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestGetPray() {
        RequestClient.makeRequest().requestPray()
            .enqueue(object : Callback<List<RequirementDataModel>> {
                override fun onFailure(
                    call: retrofit2.Call<List<RequirementDataModel>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<List<RequirementDataModel>>,
                    response: Response<List<RequirementDataModel>>
                ) {
                    mldPray.value = response.body()

                }
            })

    }

    fun requestGetCharity() {
        RequestClient.makeRequest().requestGetCharity()
            .enqueue(object : Callback<List<CharityDataModel>> {
                override fun onFailure(call: retrofit2.Call<List<CharityDataModel>>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<List<CharityDataModel>>,
                    response: Response<List<CharityDataModel>>
                ) {
                    mldCharity.value = response.body()
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestFollowersList(id: Int) {
        RequestClient.makeRequest().requestFollowesList(id)
            .enqueue(object : Callback<List<FollowersDataModel>> {
                override fun onFailure(
                    call: retrofit2.Call<List<FollowersDataModel>>,
                    t: Throwable
                ) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<List<FollowersDataModel>>,
                    response: Response<List<FollowersDataModel>>
                ) {
                    mldFollowers.value = response.body()
                    Log.i(TAG, "im hereeeeee=" + response.body())
                }
            })

    }

    fun requestMyInbox(mobile: String) {
        RequestClient.makeRequest().requestMyInbox(mobile)
            .enqueue(object : Callback<List<MyInboxDataModel>> {
                override fun onFailure(call: retrofit2.Call<List<MyInboxDataModel>>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<List<MyInboxDataModel>>,
                    response: Response<List<MyInboxDataModel>>
                ) {
                    mldInBox.value = response.body()
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestPostGallery(deceasedId: Int, path: String) {
        RequestClient.makeRequest().requestPostGallery(deceasedId, path)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldPostGallery.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestInvite(deceasedId: Int, mobile: String) {
        RequestClient.makeRequest().requestInvite(deceasedId, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldInvite.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestFollowDeceased(id: Int, mobile: String) {
        RequestClient.makeRequest().requestFollowDeceased(id, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t

                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {

                    Log.e(TAG, "onResponse: " + response.body())
                    if (response.isSuccessful && response.body() != null) {
                        mldFollow.value =
                            ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    }
                }
            })

    }

    fun requestUnFollowDeceased(id: Int, mobile: String) {
        RequestClient.makeRequest().requestUnFollowDeceased(id, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t

                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldUnFollow.value =
                        ConfirmDataModel(response.body()!!.message, response.body()!!.code)
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }

    fun requestSendComment(body: SendCommentRequest, mobile: String) {
        RequestClient.makeRequest().requestSendComment(body, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t

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
                    mldError.value = t

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
            .enqueue(object : Callback<List<GalleryDataModel>> {
                override fun onFailure(call: retrofit2.Call<List<GalleryDataModel>>, t: Throwable) {
                    Log.e(TAG, "onFailure getGallery: " + t.message)
                    mldError.value = t

                }

                override fun onResponse(
                    call: retrofit2.Call<List<GalleryDataModel>>,
                    response: Response<List<GalleryDataModel>>
                ) {
                    mldGetGallery.value = response.body()
                    Log.i(TAG, "im here=" + response.body())
                }
            })

    }


    fun requestUploadImage(file: MultipartBody.Part) {
        RequestClient.makeRequest().requestUploadFile(file)
            .enqueue(object : Callback<UploadFileDataModel> {
                override fun onFailure(call: retrofit2.Call<UploadFileDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t

                }

                override fun onResponse(
                    call: retrofit2.Call<UploadFileDataModel>,
                    response: Response<UploadFileDataModel>
                ) {
                    mldUploadImage.value =
                        UploadFileDataModel(response.body()!!.id, response.body()!!.path)
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
                    mldError.value = t

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
            .enqueue(object : Callback<userRedirect> {
                override fun onFailure(
                    call: retrofit2.Call<userRedirect>,
                    t: Throwable
                ) {
                    mldError.value = t
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<userRedirect>,
                    response: Response<userRedirect>
                ) {
                    mldCreateDeceased.value = userRedirect(response.body()!!.id)
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
                    Log.i("testTag3","edited error repo")
                    mldError.value = t
                    Log.e(TAG, "onFailure: " + t.message)
                }
                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    Log.i("testTag3","edited response repo")
                    mldEditDeceased.value = ConfirmDataModel(response.body()!!.message,response.body()!!.code)
                    Log.i("testTag3", "edited response=" + response.body())
                }
            })

    }

    fun requestRegisterUser(dataRequest: RegisterUser, mobile: String) {
        RequestClient.makeRequest().requestRegisterUser(dataRequest, mobile)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(
                    call: retrofit2.Call<ConfirmDataModel>,
                    t: Throwable
                ) {
                    mldError.value = t
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    mldRegisterUser.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun requestUserInfo(mobile: String) {
        RequestClient.makeRequest().requestUserInfo(mobile)
            .enqueue(object : Callback<UserInfoDataModel> {
                override fun onFailure(
                    call: retrofit2.Call<UserInfoDataModel>,
                    t: Throwable
                ) {
                    mldError.value = t
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<UserInfoDataModel>,
                    response: Response<UserInfoDataModel>
                ) {
                    mldUserInfo.value = response.body()
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
                    mldError.value = t
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
                    mldError.value = t
                    Log.e(TAG, "onFailure: " + t.message)
                    Log.i("testTag0", "im here erro=" + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<List<DeceasedDataModel>>,
                    response: Response<List<DeceasedDataModel>>
                ) {
                    mldSearchList.postValue(response.body())

                    Log.i("testTag0", "im here=" + response.body())
                }
            })

    }

    fun requestDeceasedPersonalProfile(id: Int, mobile: String) {
        RequestClient.makeRequest().requestDeceaedPersonal(id, mobile)
            .enqueue(object : Callback<DeceasedProfileDataModel> {
                override fun onFailure(
                    call: retrofit2.Call<DeceasedProfileDataModel>,
                    t: Throwable
                ) {
                    mldError.value = t
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

    fun requestDeceasedFromSearch(id: Int, mobile: String) {
        RequestClient.makeRequest().requestDeceaedFromSearch(id, mobile)
            .enqueue(object : Callback<DeceasedProfileDataModel> {
                override fun onFailure(
                    call: retrofit2.Call<DeceasedProfileDataModel>,
                    t: Throwable
                ) {
                    mldError.value = t
                    Log.e(TAG, "onFailure: " + t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<DeceasedProfileDataModel>,
                    response: Response<DeceasedProfileDataModel>
                ) {
                    mldDeceaedFromSearch.value = response.body()
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }


    fun requestOtp(checkOtpRequest: CheckPhoneNumberRequest) {
        RequestClient.makeRequest().requestOtp(checkOtpRequest)
            .enqueue(object : Callback<OtpDataModel> {
                override fun onFailure(call: retrofit2.Call<OtpDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                }

                override fun onResponse(
                    call: retrofit2.Call<OtpDataModel>,
                    response: Response<OtpDataModel>
                ) {
                    mldOtp.value =
                        OtpDataModel(response.body()!!.IsSuccessful, response.body()!!.Message)
                    Log.i("testTag", "im here=" + response.body())
                }
            })

    }

    fun confirmOtp(confirmOtpRequest: ConfirmOtpRequest) {
        RequestClient.makeRequest().requestConfirmOtp(confirmOtpRequest)
            .enqueue(object : Callback<ConfirmDataModel> {
                override fun onFailure(call: retrofit2.Call<ConfirmDataModel>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t

                }

                override fun onResponse(
                    call: retrofit2.Call<ConfirmDataModel>,
                    response: Response<ConfirmDataModel>
                ) {
                    Log.i(
                        TAG,
                        "im here=" + ConfirmDataModel(
                            response.message(),
                            response.code()
                        ).toString()
                    )
                    response.body().let {
                        mldConfirmOtp.value = ConfirmDataModel(response.message(), response.code())
                        Log.i("testTag", "im here=" + it?.code)
                    }
                }
            })
    }

    fun confirmPassword(confirmPassword: ConfirmPasswordRequest) {
        RequestClient.makeRequest().requestConfirmPassword(confirmPassword)
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
                    mldError.value = t
                }
            })
    }

    fun requestNews(page: String, size: String) {
        RequestClient.makeRequest().requestNews(page, size)
            .enqueue(object : Callback<List<NewsDataModel>> {

                override fun onFailure(call: Call<List<NewsDataModel>>, t: Throwable) {
                    Log.e(TAG, "onFailure news: " + t.message)
                    mldError.value = t

                }

                override fun onResponse(
                    call: Call<List<NewsDataModel>>,
                    response: Response<List<NewsDataModel>>
                ) {
                    mldNews.value = response.body()
                }
            })
    }

    fun setPassword(confirmPassword: ConfirmPasswordRequest) {
        RequestClient.makeRequest().setPassword(confirmPassword)
            .enqueue(object : Callback<ConfirmDataModel> {

                override fun onFailure(call: Call<ConfirmDataModel>, t: Throwable) {
                    mldError.value = t
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


    fun requestMapMatching(path : List<LatLng>) {
        MapRequest.makeRequest().requestMap(path)
            .enqueue(object : Callback<MapMatching> {
                override fun onFailure(call: retrofit2.Call<MapMatching>, t: Throwable) {
                    Log.e(TAG, "onFailure: " + t.message)
                    mldError.value = t
                    Log.i("testTag44", "im here in map error")
                    Log.i("testTag44", "im here in map error= "+ t.message)
                }

                override fun onResponse(
                    call: retrofit2.Call<MapMatching>,
                    response: Response<MapMatching>
                ) {
                    mldMap.value =
                        MapMatching(response.body()!!.snappedPoints)
                    Log.i("testTag44", "im here in map")
                    Log.i("testTag44", "im here in map= "+response.body()!!.snappedPoints.toString())
                }
            })

    }

}



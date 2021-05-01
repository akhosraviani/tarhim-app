package ir.co.tarhim.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.model.ConfirmDataModel
import ir.co.tarhim.model.deceased.*
import ir.co.tarhim.model.deceased.setting.SettingDataModel
import ir.co.tarhim.model.login.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.login.confirmpass.ConfirmPasswordRequest
import ir.co.tarhim.model.login.mobile.CheckPhoneNumberRequest
import ir.co.tarhim.model.login.mobile.CheckRegisterModel
import ir.co.tarhim.model.login.otp.OtpDataModel
import ir.co.tarhim.model.user.RegisterUser
import ir.co.tarhim.model.user.UserInfoDataModel
import ir.co.tarhim.ui.repository.LoginRepository
import ir.co.tarhim.utils.TarhimConfig.Companion.USER_NUMBER
import okhttp3.MultipartBody

class HomeViewModel : ViewModel() {


    private var loginRepository: LoginRepository = LoginRepository()
    var ldSignUp: LiveData<CheckRegisterModel>
    var ldOtp: LiveData<OtpDataModel>
    var ldConfirmOtp: LiveData<ConfirmDataModel>
    var ldConfirmPass: LiveData<ConfirmDataModel>
    var ldSetPass: LiveData<ConfirmDataModel>
    var ldSearch: LiveData<List<DeceasedDataModel>>
    var ldLatestSearch: LiveData<List<DeceasedDataModel>>
    var ldInboxMessage: LiveData<List<MyInboxDataModel>>
    var ldDeceasedProfile: LiveData<DeceasedProfileDataModel>
    var ldDeceasedFromSearch: LiveData<DeceasedProfileDataModel>
    var ldMyDeceased: LiveData<List<MyDeceasedDataModel>>
    var ldcreateDeceased: LiveData<userRedirect>
    var ldEditDeceased: LiveData<ConfirmDataModel>
    var ldImageUpload: LiveData<UploadFileDataModel>
    var ldGetGallery: LiveData<List<GalleryDataModel>>
    var ldGetCommnet: LiveData<List<CommentDataModel>>
    var ldSendCommnet: LiveData<ConfirmDataModel>
    var ldRegisterUser: LiveData<ConfirmDataModel>
    var ldFollow: LiveData<ConfirmDataModel>
    var ldUnFollow: LiveData<ConfirmDataModel>
    var ldSiritual: LiveData<ConfirmDataModel>
    var ldGetSiritual: LiveData<List<PrayDeceasedDataModel>>
    var ldUserInfo: LiveData<UserInfoDataModel>
    var ldError: LiveData<Throwable>
    var ldPray: LiveData<List<RequirementDataModel>>
    var ldInvite: LiveData<ConfirmDataModel>
    var ldPostGallery: LiveData<ConfirmDataModel>
    var ldCharity: LiveData<List<CharityDataModel>>
    var ldSendPray: LiveData<ConfirmDataModel>
    var ldDeleteLatest: LiveData<ConfirmDataModel>
    var ldReport: LiveData<ConfirmDataModel>
    var ldDeleteComment: LiveData<ConfirmDataModel>
    var ldAcceptRequest: LiveData<ConfirmDataModel>
    var ldRejectRequest: LiveData<ConfirmDataModel>
    var ldFollowersList: LiveData<List<FollowersDataModel>>
    var ldFollowing: LiveData<List<MyDeceasedDataModel>>
    var ldDeceasedFollowers: LiveData<List<FollowersDataModel>>
    var ldSetting: LiveData<SettingDataModel>

    init {
        ldSignUp = loginRepository.mldSignUp
        ldOtp = loginRepository.mldOtp
        ldConfirmOtp = loginRepository.mldConfirmOtp
        ldConfirmPass = loginRepository.mldConfirmPassword
        ldSiritual=loginRepository.mldSiritual
        ldSetPass = loginRepository.mldConfirmSetPassword
        ldSearch = loginRepository.mldSearchList
        ldInboxMessage = loginRepository.mldInBox
        ldGetSiritual = loginRepository.mldGetSiritual
        ldLatestSearch = loginRepository.mldLatestSearch
        ldDeceasedProfile = loginRepository.mldDeceaedProfile
        ldDeceasedFromSearch = loginRepository.mldDeceaedFromSearch
        ldMyDeceased = loginRepository.mldMyDeceaed
        ldPostGallery = loginRepository.mldPostGallery
        ldcreateDeceased = loginRepository.mldCreateDeceased
        ldImageUpload = loginRepository.mldUploadImage
        ldGetGallery = loginRepository.mldGetGallery
        ldReport=loginRepository.mldReport
        ldInvite = loginRepository.mldInvite
        ldGetCommnet = loginRepository.mldGetComment
        ldEditDeceased = loginRepository.mldEditDeceased
        ldSendCommnet = loginRepository.mldSendComment
        ldRegisterUser = loginRepository.mldRegisterUser
        ldUserInfo = loginRepository.mldUserInfo
        ldError = loginRepository.mldError
        ldFollow = loginRepository.mldFollow
        ldUnFollow = loginRepository.mldUnFollow
        ldPray = loginRepository.mldPray
        ldCharity = loginRepository.mldCharity
        ldSendPray = loginRepository.mldSendPray
        ldFollowersList = loginRepository.mldFollowers
        ldFollowing = loginRepository.mldFollowing
        ldDeleteLatest = loginRepository.mldDeleteLatest
        ldDeceasedFollowers = loginRepository.mldDeceasedFollowers
        ldDeleteComment=loginRepository.mldDeleteComment
        ldAcceptRequest=loginRepository.mldAcceptRequest
        ldRejectRequest=loginRepository.mldRejectRequest
        ldSetting=loginRepository.mldSetting
    }

    fun requestPostGallery(deceasedId: Int, path: String) {
        loginRepository.requestPostGallery(deceasedId, path)
    }

    fun requestDeceasedFollowers(deceasedId: Int) {
        loginRepository.requestDeceasedFollowersList(deceasedId)
    }
    fun requestDeleteComment(body:DeleteCommentRequestModel) {
        loginRepository.requestDeleteComment(body,Hawk.get(USER_NUMBER))
    }
    fun requestAcceptRequest(notificationId:Int) {
        loginRepository.requestAcceptRequest(Hawk.get(USER_NUMBER),notificationId )
    }
    fun requestRejectRequest(notificationId:Int) {
        loginRepository.requestRejectRequest(Hawk.get(USER_NUMBER),notificationId )
    }fun requestSetting() {
        loginRepository.requestSetting()
    }

    fun requestDeleteLatest(deceasedId: Int) {
        loginRepository.requestDeleteLatestItem(deceasedId, Hawk.get(USER_NUMBER))
    }

    fun requestSiritual(body: PrayDeceasedRequest) {
        loginRepository.requestSiritual(Hawk.get(USER_NUMBER), body)
    }
    fun getSiritualRes(id:Int) {
        loginRepository.requestGetSiritualRes(id,Hawk.get(USER_NUMBER))
    }
    fun grequestReport(body:ReportRequest) {
        loginRepository.requestReport(Hawk.get(USER_NUMBER),body)
    }

    fun requestFollowing() {
        loginRepository.requestFollowing(Hawk.get(USER_NUMBER))
    }

    fun requestGetPray() {
        loginRepository.requestGetPray()
    }

    fun requestGetCharity() {
        loginRepository.requestGetCharity()
    }

    fun requestFollowes(deceasedId: Int) {
        loginRepository.requestFollowersList(deceasedId)
    }

    fun requestInboxMessage() {
        loginRepository.requestMyInbox(Hawk.get(USER_NUMBER))
    }

    fun requestInvite(id: Int, mobile: String) {
        loginRepository.requestInvite(id, mobile)
    }

    fun requestCreateDeceased(dataRequest: CreateDeceasedRequest) {
        loginRepository.requestCreateDeceaed(dataRequest, Hawk.get(USER_NUMBER))
    }

    fun requestFollowDeceased(id: Int) {
        loginRepository.requestFollowDeceased(id, Hawk.get(USER_NUMBER))
    }

    fun requestUnFollowDeceased(id: Int) {
        loginRepository.requestUnFollowDeceased(id, Hawk.get(USER_NUMBER))
    }

    fun requestRegisterUser(dataRequest: RegisterUser) {
        loginRepository.requestRegisterUser(dataRequest, Hawk.get(USER_NUMBER))
    }

    fun requestUserInfo() {
        loginRepository.requestUserInfo(Hawk.get(USER_NUMBER))
    }

    fun requestEditDeceased(dataRequest: CreateDeceasedRequest, id: Int) {
        loginRepository.requestEditDeceaed(dataRequest, id, Hawk.get(USER_NUMBER))
    }

    fun requestSendComment(body: SendCommentRequest) {
        loginRepository.requestSendComment(body, Hawk.get(USER_NUMBER))
    }

    fun requestGetComment(id: Int) {
        loginRepository.requestGetComment(id, Hawk.get(USER_NUMBER))
        Log.i("testTag", "hi model view" + ldSignUp)
    }

    fun requestUploadImage(file: MultipartBody.Part) {
        loginRepository.requestUploadImage(file)
    }

    fun requestGetGallery(id: Int) {
        loginRepository.requestGetGallery(id)
    }

    fun requestCheckRegister(checkRegisterRequest: CheckPhoneNumberRequest) {
        loginRepository.requestSendPray(checkRegisterRequest)
        Log.i("testTag", "hi model view" + ldSignUp)
    }

    fun requestSendPray(body: PrayDataRequest) {
        loginRepository.requestSendPray(Hawk.get(USER_NUMBER), body)
        Log.i("testTag", "hi model view" + ldSignUp)
    }

    fun requestMydeceased() {
        loginRepository.requestMyDeceaed(Hawk.get(USER_NUMBER))
    }


    fun requestOtp(otpRequest: CheckPhoneNumberRequest) {
        loginRepository.requestOtp(otpRequest)
        Log.i("testTag", "hi model view" + ldOtp)
    }

    fun requestSearch(keyword: String) {
        loginRepository.requestSearch(keyword)
    }

    fun requestlatestSearch() {
        loginRepository.requestLatestSearch(Hawk.get(USER_NUMBER))
    }

    fun requestDeceasedPersonal(id: Int) {
        loginRepository.requestDeceasedPersonalProfile(id, Hawk.get(USER_NUMBER))
    }

    fun requestDeceasedFromSearch(id: Int) {
        loginRepository.requestDeceasedFromSearch(id, Hawk.get(USER_NUMBER))
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

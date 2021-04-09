package ir.co.tarhim.ui.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import ir.co.tarhim.model.confirmotp.ConfirmOtpDataModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.confirmpass.ConfirmDataModel
import ir.co.tarhim.model.confirmpass.ConfirmPasswordRequest
import ir.co.tarhim.model.deceased.*
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.otp.OtpDataModel
import ir.co.tarhim.model.user.RegisterUser
import ir.co.tarhim.model.user.UserInfoDataModel
import ir.co.tarhim.ui.repository.LoginRepository
import okhttp3.MultipartBody
import kotlin.math.log

class HomeViewModel : ViewModel() {


    private var loginRepository: LoginRepository = LoginRepository()
    var ldSignUp: LiveData<CheckRegisterModel>
    var ldOtp: LiveData<OtpDataModel>
    var ldConfirmOtp: LiveData<ConfirmDataModel>
    var ldConfirm: LiveData<ConfirmDataModel>
    var ldSet: LiveData<ConfirmDataModel>
    var ldSearch: LiveData<List<DeceasedDataModel>>
    var ldLatestSearch: LiveData<List<DeceasedDataModel>>
    var ldDeceasedProfile: LiveData<DeceasedProfileDataModel>
    var ldDeceasedFromSearch: LiveData<DeceasedProfileDataModel>
    var ldMyDeceased: LiveData<List<MyDeceasedDataModel>>
    var ldcreateDeceased: LiveData<userRedirect>
    var ldEditDeceased: LiveData<ConfirmDataModel>
    var ldImageUpload: LiveData<UploadFileDataModel>
    var ldGetGallery: LiveData<GalleryDataModel>
    var ldGetCommnet: LiveData<List<CommentDataModel>>
    var ldSendCommnet: LiveData<ConfirmDataModel>
    var ldRegisterUser: LiveData<ConfirmDataModel>
    var ldFollow: LiveData<ConfirmDataModel>
    var ldUnFollow: LiveData<ConfirmDataModel>
    var ldUserInfo: LiveData<UserInfoDataModel>
    var ldError: LiveData<Throwable>

    init {
        ldSignUp = loginRepository.mldSignUp
        ldOtp = loginRepository.mldOtp
        ldConfirmOtp = loginRepository.mldConfirmOtp
        ldConfirm = loginRepository.mldConfirmPassword
        ldSet = loginRepository.mldConfirmSetPassword
        ldSearch = loginRepository.mldSearchList
        ldLatestSearch = loginRepository.mldLatestSearch
        ldDeceasedProfile = loginRepository.mldDeceaedProfile
        ldDeceasedFromSearch = loginRepository.mldDeceaedFromSearch
        ldMyDeceased = loginRepository.mldMyDeceaed
        ldcreateDeceased = loginRepository.mldCreateDeceased
        ldImageUpload = loginRepository.mldUploadImage
        ldGetGallery = loginRepository.mldGetGallery
        ldGetCommnet = loginRepository.mldGetComment
        ldEditDeceased = loginRepository.mldEditDeceased
        ldSendCommnet = loginRepository.mldSendComment
        ldRegisterUser = loginRepository.mldRegisterUser
        ldUserInfo = loginRepository.mldUserInfo
        ldError= loginRepository.mldError
        ldFollow=loginRepository.mldFollow
        ldUnFollow=loginRepository.mldUnFollow

    }

    fun requestCreateDeceased(dataRequest: CreateDeceasedRequest) {
        loginRepository.requestCreateDeceaed(dataRequest, Hawk.get("UserNumber"))
    }
    fun requestFollowDeceased(id:Int) {
        loginRepository.requestFollowDeceased(id, Hawk.get("UserNumber"))
    }
    fun requestUnFollowDeceased(id:Int) {
        loginRepository.requestUnFollowDeceased(id, Hawk.get("UserNumber"))
    }

    fun requestRegisterUser(dataRequest: RegisterUser) {
        loginRepository.requestRegisterUser(dataRequest, Hawk.get("UserNumber"))
    }

    fun requestUserInfo() {
        loginRepository.requestUserInfo(Hawk.get("UserNumber"))
    }

    fun requestEditDeceased(dataRequest: CreateDeceasedRequest, id: Int) {
        loginRepository.requestEditDeceaed(dataRequest, id, Hawk.get("UserNumber"))
    }

    fun requestSendComment(body: SendCommentRequest) {
        loginRepository.requestSendComment(body, Hawk.get("UserNumber"))
    }

    fun requestGetComment(id: Int) {
        loginRepository.requestGetComment(id, Hawk.get("UserNumber"))
        Log.i("testTag", "hi model view" + ldSignUp)
    }

    fun requestUploadImage(file: MultipartBody.Part) {
        loginRepository.requestUploadImage(file)
    }

    fun requestGetGallery(id: Int) {
        loginRepository.requestGetGallery(id)
    }

    fun requestCheckRegister(checkRegisterRequest: CheckPhoneNumber) {
        loginRepository.requestCheckRegister(checkRegisterRequest)
        Log.i("testTag", "hi model view" + ldSignUp)
    }

    fun requestMydeceased() {
        loginRepository.requestMyDeceaed(Hawk.get("UserNumber"))
    }


    fun requestOtp(otpRequest: CheckPhoneNumber) {
        loginRepository.requestOtp(otpRequest)
        Log.i("testTag", "hi model view" + ldOtp)
    }

    fun requestSearch(keyword: String) {
        loginRepository.requestSearch(keyword)
    }

    fun requestlatestSearch() {
        loginRepository.requestLatestSearch(Hawk.get("UserNumber"))
    }

    fun requestDeceasedPersonal(id: Int) {
        loginRepository.requestDeceasedPersonalProfile(id, Hawk.get("UserNumber"))
    }

    fun requestDeceasedFromSearch(id: Int) {
        loginRepository.requestDeceasedFromSearch(id, Hawk.get("UserNumber"))
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

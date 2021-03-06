package ir.co.mazar.ui.viewModels

import android.content.ContentResolver
import android.content.Context
import android.os.RemoteException
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orhanobut.hawk.Hawk
import ir.co.mazar.model.ConfirmDataModel
import ir.co.mazar.model.RemindeRequestModel
import ir.co.mazar.model.deceased.*
import ir.co.mazar.model.deceased.setting.SettingDataModel
import ir.co.mazar.model.login.confirmotp.ConfirmOtpRequest
import ir.co.mazar.model.login.confirmpass.ConfirmPasswordRequest
import ir.co.mazar.model.login.mobile.CheckPhoneNumberRequest
import ir.co.mazar.model.login.mobile.CheckRegisterModel
import ir.co.mazar.model.login.otp.OtpDataModel
import ir.co.mazar.model.user.RegisterUser
import ir.co.mazar.model.user.UserInfoDataModel
import ir.co.mazar.ui.activities.invite_friend.ContactModel
import ir.co.mazar.ui.activities.invite_friend.InviteActivity
import ir.co.mazar.ui.repository.LoginRepository
import ir.co.mazar.utils.TarhimConfig.Companion.USER_NUMBER
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
    var livedataListContact: LiveData<List<ContactModel>>
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
    var ldReminder: LiveData<ConfirmDataModel>
    var ldRejectRequest: LiveData<ConfirmDataModel>
    var ldShareLink: LiveData<ConfirmDataModel>
    var ldFollowersList: LiveData<List<FollowersDataModel>>
    var ldFollowing: LiveData<List<MyDeceasedDataModel>>
    var ldDeceasedFollowers: LiveData<List<FollowersDataModel>>
    var ldSetting: LiveData<SettingDataModel>
    var ldNotificationMessage: LiveData<List<NotifDataModel>>
    var ldAddContact: LiveData<List<ListOfContactsModel>>
    var cheeert: LiveData<List<ContactModel>>

    var muList: MutableLiveData<List<ContactModel>>


    init {
        muList = MutableLiveData()
        ldSignUp = loginRepository.mldSignUp
        ldOtp = loginRepository.mldOtp
        ldConfirmOtp = loginRepository.mldConfirmOtp
        ldConfirmPass = loginRepository.mldConfirmPassword
        ldSiritual = loginRepository.mldSiritual
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
        ldReport = loginRepository.mldReport
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
        ldDeleteComment = loginRepository.mldDeleteComment
        ldAcceptRequest = loginRepository.mldAcceptRequest
        ldRejectRequest = loginRepository.mldRejectRequest
        ldSetting = loginRepository.mldSetting
        ldReminder = loginRepository.mldReminder
        ldNotificationMessage = loginRepository.mldNotification
        ldAddContact = loginRepository.mldAddContact
        ldShareLink = loginRepository.mldShareLink
        livedataListContact = loginRepository.mldContact

        cheeert = muList

    }

    fun requestgetContact(context: Context) {
        loginRepository.getContactsData(context)
    }

    fun requestPostGallery(deceasedId: Int, path: String) {
        loginRepository.requestPostGallery(deceasedId, path)
    }

    fun requestShareLink(id: Int, mobile: String) {
        loginRepository.ShareLink(id, mobile)
    }


    fun requestDeceasedFollowers(deceasedId: Int) {
        loginRepository.requestDeceasedFollowersList(deceasedId)
    }

    fun requestDeleteComment(body: DeleteCommentRequestModel) {
        loginRepository.requestDeleteComment(body, Hawk.get(USER_NUMBER))
    }

    fun requestAcceptRequest(notificationId: Int) {
        loginRepository.requestAcceptRequest(Hawk.get(USER_NUMBER), notificationId)
    }

    fun requestRejectRequest(notificationId: Int) {
        loginRepository.requestRejectRequest(Hawk.get(USER_NUMBER), notificationId)
    }

    fun requestSetting(token: String) {
        loginRepository.requestSetting(Hawk.get(USER_NUMBER), token)
    }

    fun requestDeleteLatest(deceasedId: Int) {
        loginRepository.requestDeleteLatestItem(deceasedId, Hawk.get(USER_NUMBER))
    }

    fun requestSiritual(body: PrayDeceasedRequest) {
        loginRepository.requestSiritual(Hawk.get(USER_NUMBER), body)
    }

    fun getSiritualRes(id: Int) {
        loginRepository.requestGetSiritualRes(id, Hawk.get(USER_NUMBER))
    }

    fun grequestReport(body: ReportRequest) {
        loginRepository.requestReport(Hawk.get(USER_NUMBER), body)
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

    fun requestAddContact(id: Int) {
//        loginRepository.requestMyContactsList(id)
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
        Log.i("testTag3", "edited view model")
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

    fun requestReminder(body: RemindeRequestModel) {
        loginRepository.requestReminder(body)
    }

    fun requestNotification() {
        loginRepository.requestNotification(Hawk.get(USER_NUMBER))
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

    fun getContactList(context: Context) {
//        var mContactList: ArrayList<ContactModel> = ArrayList<ContactModel>()
//        val cResolver: ContentResolver = context.contentResolver
//        val mCProviderClient =
//            cResolver.acquireContentProviderClient(ContactsContract.Contacts.CONTENT_URI)
//        val phoneCursor = mCProviderClient!!.query(
//            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//            null,
//            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//            null,
//            null
//        )
//        try {
//            val mCursor = mCProviderClient!!.query(
//                ContactsContract.Contacts.CONTENT_URI,
//                null,
//                null,
//                null,
//                null
//            )
//            if (mCursor != null && mCursor.count > 0) {
//                mContactList = ArrayList()
//                mCursor.moveToFirst()
//                while (!mCursor.isLast) {
//                    val contactId =
//                        mCursor.getString(mCursor.getColumnIndex(ContactsContract.Contacts._ID));
//                    val displayName =
//                        mCursor.getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
//                    val phoneNumber =
//                        phoneCursor!!.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                    mContactList.add(ContactModel(phoneNumber, displayName))
//
//
//                    mCursor.moveToNext()
//                    phoneCursor.moveToNext()
//                }
//                if (mCursor.isLast) {
//                    val contactId =
//                        mCursor.getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
//                    val displayName =
//                        mCursor.getString(mCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
//
//                    val phoneCursor = mCProviderClient.query(
//                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                        null,
//                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
//                        arrayOf<String>(contactId),
//                        null
//                    )
//                    val phoneNumber =
//                        phoneCursor!!.getString(
//                            phoneCursor
//                                .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
//                        )
//                    mContactList.add(ContactModel(phoneNumber, displayName))
//
//
//                }
//            }
//            mCursor!!.close()
//        } catch (e: RemoteException) {
//            e.printStackTrace()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//
//        }
        var mContactList: ArrayList<ContactModel> = ArrayList<ContactModel>()

        val cr = context.contentResolver
        val cursor = cr.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            PROJECTION,
            null,
            null,
            null
        )
        if (cursor != null) {
            try {
                val nameIndex: Int = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
                val numberIndex: Int =
                    cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                var name: String
                var number: String
                while (cursor.moveToNext()) {
                    name = cursor.getString(nameIndex)
                    number = cursor.getString(numberIndex)

                    if(number.startsWith("+98")){
                        var replacePhone=number.replace("+98","0")
                        number=replacePhone
                    }
                    mContactList.add(ContactModel(number, name))

                }
            } finally {
                cursor.close()
            }
        }


        muList.value = mContactList
    }


    private val PROJECTION = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER
    )

}



package ir.co.tarhim.network

import ir.co.tarhim.model.ConfirmDataModel
import ir.co.tarhim.model.deceased.*
import ir.co.tarhim.model.deceased.like.LikeCommentDataModel
import ir.co.tarhim.model.deceased.like.LikeCommentRequest
import ir.co.tarhim.model.login.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.login.confirmpass.ConfirmPasswordRequest
import ir.co.tarhim.model.login.mobile.CheckPhoneNumberRequest
import ir.co.tarhim.model.login.mobile.CheckRegisterModel
import ir.co.tarhim.model.login.otp.OtpDataModel
import ir.co.tarhim.model.news.NewsDataModel
import ir.co.tarhim.model.user.FollowersDataModel
import ir.co.tarhim.model.user.RegisterUser
import ir.co.tarhim.model.user.UserInfoDataModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RequestApi {


    @POST("api/v1/account/mobile")
    fun requestCheckRegister(@Body checkRegisterRequest: CheckPhoneNumberRequest): Call<CheckRegisterModel>

    @POST("api/v1/account/otp")
    fun requestOtp(@Body checkPhoneNumber: CheckPhoneNumberRequest): Call<OtpDataModel>

    @POST("api/v1/account/confirmOtp")
    fun requestConfirmOtp(@Body confirmOtpRequest: ConfirmOtpRequest): Call<ConfirmDataModel>

    @POST("api/v1/account/confirmPassword")
    fun requestConfirmPassword(@Body confirmPassword: ConfirmPasswordRequest): Call<ConfirmDataModel>

    @POST("api/v1/account/setPassword")
    fun setPassword(@Body setPassword: ConfirmPasswordRequest): Call<ConfirmDataModel>


    @GET("api/v1/deceased/latestsearch")
    fun requestLatestSearch(@Query("mobile") mobile: String): Call<List<DeceasedDataModel>>

    @GET("api/v1/deceased/getfromsearch")
    fun requestDeceaedFromSearch(
        @Query("id") id: Int,
        @Query("mobile") mobile: String
    ): Call<DeceasedProfileDataModel>

    @GET("api/v1/deceased/personal")
    fun requestDeceaedPersonal(
        @Query("id") id: Int,
        @Query("mobile") mobile: String
    ): Call<DeceasedProfileDataModel>

    @POST("api/v1/deceased/search")
    fun requestSearch(@Body keyword: SearchDeceasedRequest): Call<List<DeceasedDataModel>>


    @GET("api/v1/deceased/mydeceased")
    fun requestMyDeceased(@Query("mobile") mobile: String): Call<List<MyDeceasedDataModel>>

    @POST("api/v1/deceased/create")
    fun requestCreateDeceased(
        @Body createdeceased: CreateDeceasedRequest, @Query("mobile") mobile: String
    ): Call<userRedirect>

    @PUT("api/v1/deceased/edit")
    fun requestEditDeceased(
        @Body createdeceased: CreateDeceasedRequest,
        @Query("id") id: Int,
        @Query("mobile") mobile: String
    ): Call<ConfirmDataModel>


    @Multipart
    @POST("api/v1/utility/image")
    fun requestUploadFile(@Part file: MultipartBody.Part): Call<UploadFileDataModel>

    @GET("api/v1/deceased/mygallery")
    fun requestGetGallery(@Query("id") id: Int): Call<List<GalleryDataModel>>


    @GET("api/v1/comments")
    fun requestGetComment(
        @Query("id") id: Int,
        @Query("mobile") mobile: String
    ): Call<List<CommentDataModel>>

    @POST("api/v1/comments")
    fun requestSendComment(
        @Body body: SendCommentRequest,
        @Query("mobile") mobile: String
    ): Call<ConfirmDataModel>

    @PUT("api/v1/account/edit")
    fun requestRegisterUser(
        @Body body: RegisterUser,
        @Query("mobile") mobile: String
    ): Call<ConfirmDataModel>

    @GET("api/v1/account/me")
    fun requestUserInfo(@Query("mobile") mobile: String): Call<UserInfoDataModel>

    @GET("api/v1/account/news")
    fun requestNews(
        @Query("page") page: String,
        @Query("pagesize") pageSize: String
    ): Call<List<NewsDataModel>>


    @POST("api/v1/deceased/follow")
    fun requestFollowDeceased(
        @Query("id") deceasedId: Int,
        @Query("mobile") mobile: String
    ): Call<ConfirmDataModel>

    @POST("api/v1/deceased/unfollow")
    fun requestUnFollowDeceased(
        @Query("id") deceasedId: Int,
        @Query("mobile") mobile: String
    ): Call<ConfirmDataModel>


    @POST("api/v1/inbox/invite")
    fun requestInvite(
        @Query("id") userId: Int,
        @Query("mobile") contactMobile: String
    ): Call<ConfirmDataModel>

    @POST("api/v1/admin/specificdeceased/gallery")
    fun requestPostGallery(
        @Query("id") userId: Int,
        @Query("path") path: String
    ): Call<ConfirmDataModel>


    @GET("api/v1/inbox/mybox")
    fun requestMyInbox(
        @Query("mobile") mobile: String,

        ): Call<List<MyInboxDataModel>>

    @GET("api/v1/pray")
    fun requestPray(): Call<List<RequirementDataModel>>

    @POST("api/v1/pray")
    fun requestSendPray(
        @Query("mobile") mobile: String,
        @Body prayBody: PrayDataRequest
    ): Call<ConfirmDataModel>

    @GET("api/v1/deceased/charity")
    fun requestGetCharity(): Call<List<CharityDataModel>>

    @GET("api/v1/admin/followerlist")
    fun requestFollowesList(@Query("id") deceaseId: Int): Call<List<FollowersDataModel>>

    @GET("api/v1/deceased/following\n")
    fun requestFollowingList(@Query("mobile") mobile: String): Call<List<MyDeceasedDataModel>>

    @DELETE("api/v1/deceased/deleteonelatestsearch")
    fun requestDeleteLatestItem(
        @Query("id") deceasedId: Int,
        @Query("mobile") mobile: String
    ): Call<ConfirmDataModel>


    @GET("api/v1/deceased/prayDeceased")
    fun requestGetSiritualDeceased(
        @Query("id") id: Int,
        @Query("mobile") mobile: String
    ): Call<List<PrayDeceasedDataModel>>

    @POST("api/v1/deceased/prayDeceased")
    fun requestSiritual(
        @Query("mobile") mobile: String,
        @Body pratRequest: PrayDeceasedRequest
    ): Call<ConfirmDataModel>

    @POST("api/v1/reports")
    fun requestReport(
        @Query("mobile") mobile: String,
        @Body reportRequest: ReportRequest
    ): Call<ConfirmDataModel>


    @POST("api/v1/likes/favourite")
    fun requestLikeComment(
        @Body body: LikeCommentRequest,
        @Query("mobile") mobile: String,
    ): Call<LikeCommentDataModel>

}


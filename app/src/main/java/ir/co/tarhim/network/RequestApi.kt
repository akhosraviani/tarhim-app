package ir.co.tarhim.network

import ir.co.tarhim.model.confirmotp.ConfirmOtpDataModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.confirmpass.ConfirmDataModel
import ir.co.tarhim.model.confirmpass.ConfirmPasswordRequest
import ir.co.tarhim.model.deceased.*
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.model.otp.OtpDataModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RequestApi {


    @POST("api/v1/account/mobile")
    fun requestCheckRegister(@Body checkRegisterRequest: CheckPhoneNumber): Call<CheckRegisterModel>

    @POST("api/v1/account/otp")
    fun requestOtp(@Body checkPhoneNumber: CheckPhoneNumber): Call<OtpDataModel>

    @POST("api/v1/account/confirmOtp")
    fun confirmOtp(@Body confirmOtpRequest: ConfirmOtpRequest): Call<ConfirmDataModel>

    @POST("api/v1/account/confirmPassword")
    fun confirmPassword(@Body confirmPassword: ConfirmPasswordRequest): Call<ConfirmDataModel>

    @POST("api/v1/account/setPassword")
    fun setPassword(@Body setPassword: ConfirmPasswordRequest): Call<ConfirmDataModel>


    @GET("api/v1/deceased/latestsearch")
    fun requestLatestSearch(@Query("mobile") mobile: String): Call<List<DeceasedDataModel>>

    @GET("api/v1/deceased/getfromsearch")
    fun requestDeceaedProfile(@Query("id") id: Int, @Query("mobile") mobile: String): Call<DeceasedProfileDataModel>

    @POST("api/v1/deceased/search")
    fun requestSearch(@Body keyword: SearchDeceasedRequest): Call<List<DeceasedDataModel>>


    @GET("api/v1/deceased/mydeceased")
    fun requestMyDeceased(@Query("mobile")mobile:String): Call<List<MyDeceasedDataModel>>

    @POST("api/v1/deceased/create")
    fun requestCreateDeceased(@Body createdeceased: CreateDeceasedRequest,@Query("mobile")mobile:String
    ): Call<ConfirmDataModel>

    @PUT("api/v1/deceased/edit")
    fun requestEditDeceased(@Body createdeceased: CreateDeceasedRequest, @Query("id")id:Int, @Query("mobile")mobile:String
    ): Call<ConfirmDataModel>


    @Multipart
    @POST("api/v1/utility/image")
    fun requestUploadFile(@Part file:MultipartBody.Part):Call<UploadFileDataModel>

    @GET("api/v1/deceased/mygallery")
    fun requestGetGallery(@Query("id")id:Int):Call<GalleryDataModel>


    @GET("api/v1/comments")
    fun requestGetComment(@Query("id") id:Int,@Query("mobile") mobile:String):Call<List<CommentDataModel>>

    @POST("api/v1/comments")
    fun requestSendComment(@Body body:SendCommentRequest,@Query("mobile") mobile:String):Call<ConfirmDataModel>

}


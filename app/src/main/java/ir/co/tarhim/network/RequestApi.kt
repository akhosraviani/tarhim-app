package ir.co.tarhim.network

import ir.co.tarhim.model.confirmotp.ConfirmOtpDataModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.confirmpass.ConfirmPasswordDataModel
import ir.co.tarhim.model.confirmpass.ConfirmPasswordRequest
import ir.co.tarhim.model.deceased.*
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.model.otp.OtpDataModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RequestApi {


    @POST("api/v1/account/mobile")
    fun requestCheckRegister(@Body checkRegisterRequest: CheckPhoneNumber): Call<CheckRegisterModel>

    @POST("api/v1/account/otp")
    fun requestOtp(@Body checkPhoneNumber: CheckPhoneNumber): Call<OtpDataModel>

    @POST("api/v1/account/confirmOtp")
    fun confirmOtp(@Body confirmOtpRequest: ConfirmOtpRequest): Call<ConfirmOtpDataModel>

    @POST("api/v1/account/confirmPassword")
    fun confirmPassword(@Body confirmPassword: ConfirmPasswordRequest): Call<ConfirmPasswordDataModel>

    @POST("api/v1/account/setPassword")
    fun setPassword(@Body setPassword: ConfirmPasswordRequest): Call<ConfirmPasswordDataModel>


    @GET("api/v1/deceased/latestsearch")
    fun requestLatestSearch(@Query("mobile") mobile: String): Call<List<DeceasedDataModel>>

    @GET("api/v1/deceased/getfromsearch")
    fun requestDeceaedProfile(@Query("id") id: Int, @Query("mobile") mobile: String): Call<DeceasedProfileDataModel>

    @POST("api/v1/deceased/search")
    fun requestSearch(@Body keyword: SearchDeceasedRequest): Call<List<DeceasedDataModel>>


    @GET("api/v1/deceased/mydeceased")
    fun requestMyDeceased(@Query("mobile")mobile:String): Call<List<MydeceasedDataModel>>

    @POST("api/v1/deceased/create")
    fun requestCreateDeceased(@Body createdeceased: CreateDeceasedRequest,@Query("mobile")mobile:String
    ): Call<ConfirmOtpDataModel>


}


package ir.co.tarhim.network

import ir.co.tarhim.model.confirmotp.ConfirmOtpDataModel
import ir.co.tarhim.model.confirmotp.ConfirmOtpRequest
import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.mobile.CheckPhoneNumber
import ir.co.tarhim.model.otp.OtpDataModel
import ir.co.tarhim.model.password.PasswordDataModel
import ir.co.tarhim.model.password.PasswordRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestApi {

    @POST("api/v1/account/mobile")
   fun requestSignUp(@Body checkRegisterRequest: CheckPhoneNumber): Call<CheckRegisterModel>

    @POST("api/v1/account/otp")
    fun requestOtp(@Body checkPhoneNumber: CheckPhoneNumber): Call<OtpDataModel>

    @POST("api/v1/account/confirmOtp")
    fun confirmOtp(@Body confirmOtpRequest: ConfirmOtpRequest): Call<ConfirmOtpDataModel>

    @POST("api/v1/account/setPassword")
    fun setPassword(@Body setPasswordRequest: PasswordRequest): Call<PasswordDataModel>

    @POST("api/v1/account/confirmPassword")
    fun confirmPassword(@Body confirmPasswordRequest: PasswordRequest): Call<PasswordDataModel>


}


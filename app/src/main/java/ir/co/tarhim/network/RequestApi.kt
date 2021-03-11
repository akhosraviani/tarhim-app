package ir.co.tarhim.network

import ir.co.tarhim.model.mobile.CheckRegisterModel
import ir.co.tarhim.model.mobile.CheckRegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RequestApi {



    @POST("api/v1/account/mobile")
   fun requestSignUp(@Body checkRegisterRequest: CheckRegisterRequest): Call<CheckRegisterModel>


}


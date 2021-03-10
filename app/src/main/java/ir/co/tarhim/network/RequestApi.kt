package ir.co.tarhim.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface RequestApi {



    @POST("api/v1/account/mobile")
   fun requestSignUp(@Query("mobile") mobile:String): Call<ResponseBody>


}


package ir.co.tarhim.ui.repository

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.MutableLiveData
import ir.co.tarhim.network.RequestClient
import okhttp3.ResponseBody
import retrofit2.Callback
import retrofit2.Response

class LoginRepositoy {


    companion object{
        private const val TAG = "LoginRepositoy"
    }

    public lateinit var mldSignUp:MutableLiveData<ResponseBody>
    fun requestSignUp(number:String){

        RequestClient.makeRequest().requestSignUp(number).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                mldSignUp.postValue(response.body())
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Log.e(TAG, "onFailure: "+t.message )
            }

        })
    }

}

package ir.co.mazar.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object MapRequest {
    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor { chain ->
            val request = chain.request()
            val newRequest = request.newBuilder()
                .addHeader("Api-Key", "4nKLqeD9zgC7Hr4")
                .build()
            chain.proceed(newRequest)
        }
        .build()
    private val gson = GsonBuilder().setLenient().create()


    private var makeRetrofit = Retrofit
        .Builder()
        .baseUrl("https://api.neshan.org/")
        .client(client)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    public fun makeRequest(): RequestApi {
        return makeRetrofit.create(RequestApi::class.java)
    }
}

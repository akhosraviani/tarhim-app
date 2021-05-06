package ir.co.mazar.ui.fragments.news

import android.util.Log
import androidx.paging.PageKeyedDataSource
import ir.co.mazar.model.news.NewsDataModel
import ir.co.mazar.network.RequestClient
import retrofit2.Response

class NewsDataSource() : PageKeyedDataSource<Int, NewsDataModel>() {


    public val pageSize: String = "10"
    public val firstPage: Int = 0
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NewsDataModel>
    ) {
        RequestClient.makeRequest().requestNews(pageSize, firstPage.toString())
            .enqueue(object : retrofit2.Callback<List<NewsDataModel>> {
                override fun onResponse(
                    call: retrofit2.Call<List<NewsDataModel>>,
                    response: Response<List<NewsDataModel>>
                ) {

                    response?.let {

                        callback.onResult(it.body()!!, null, firstPage + 1)
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<NewsDataModel>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NewsDataModel>) {
        RequestClient.makeRequest().requestNews(pageSize, firstPage.toString())
            .enqueue(object : retrofit2.Callback<List<NewsDataModel>> {
                override fun onResponse(
                    call: retrofit2.Call<List<NewsDataModel>>,
                    response: Response<List<NewsDataModel>>
                ) {
                    response?.let {
                        Log.e("TAG", "onResponse Before size: " + response.body()!!.size)

                        val adjacentKey = if (params.key > 1) params.key - 1 else null
                        callback.onResult(it.body()!!, adjacentKey)
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<NewsDataModel>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NewsDataModel>) {
        RequestClient.makeRequest().requestNews(pageSize, firstPage.toString())
            .enqueue(object : retrofit2.Callback<List<NewsDataModel>> {
                override fun onResponse(
                    call: retrofit2.Call<List<NewsDataModel>>,
                    response: Response<List<NewsDataModel>>
                ) {

                    response?.let {
                        Log.e("TAG", "onResponse After size: " + response.body()!!.size)

                        val key =
                            if ( it.body()!!.size>0) params.key + 1 else null

                        callback.onResult(it.body()!!, key)
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<NewsDataModel>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
    }


}




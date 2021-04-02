package ir.co.tarhim.ui.fragments.news

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import ir.co.tarhim.model.news.NewsDataModel

class NewsDataSourceFactory : DataSource.Factory<Int, NewsDataModel>() {
    val itemLiveDataSoure = MutableLiveData<PageKeyedDataSource<Int, NewsDataModel>>()

    override fun create(): DataSource<Int, NewsDataModel> {
        val itemDataSource = NewsDataSource()

        itemLiveDataSoure.postValue(itemDataSource)
        itemDataSource?.let {

        }
        return itemDataSource
    }
}
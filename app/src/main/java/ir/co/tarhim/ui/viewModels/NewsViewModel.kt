package ir.co.tarhim.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.*
import ir.co.tarhim.model.news.NewsDataModel
import ir.co.tarhim.ui.fragments.news.NewsDataSource
import ir.co.tarhim.ui.fragments.news.NewsDataSourceFactory
import ir.co.tarhim.ui.repository.LoginRepository

class NewsViewModel() : ViewModel() {
//    var itemPageList: LiveData<PagedList<NewsDataModel>>
//    private var liveDataSource: LiveData<PageKeyedDataSource<Int,NewsDataModel>>
//

    private lateinit var repository: LoginRepository
     var ldNews: LiveData<List<NewsDataModel>>

    init {
        repository = LoginRepository()
//        var itemDataSourceFactory = NewsDataSourceFactory()
//        liveDataSource = itemDataSourceFactory.itemLiveDataSoure
//
//        var pagedListConfig = PagedList.Config.Builder()
//            .setEnablePlaceholders(false)
//            .setPageSize(20)
//            .build()
//        itemPageList = LivePagedListBuilder(itemDataSourceFactory,pagedListConfig).build()

        ldNews =repository.mldNews
    }


    fun requestNews(){
        repository.requestNews("0","10")
    }

}
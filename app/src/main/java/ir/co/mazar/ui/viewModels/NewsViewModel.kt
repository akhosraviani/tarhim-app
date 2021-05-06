package ir.co.mazar.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ir.co.mazar.model.news.NewsDataModel
import ir.co.mazar.ui.repository.LoginRepository

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
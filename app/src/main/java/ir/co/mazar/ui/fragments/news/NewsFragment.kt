package ir.co.mazar.ui.fragments.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.mazar.R
import ir.co.mazar.model.news.NewsDataModel
import ir.co.mazar.ui.adapter.NewsAdapter
import ir.co.mazar.ui.callback.NewsListener
import ir.co.mazar.ui.viewModels.NewsViewModel
import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment(),NewsListener {


    companion object{
        private const val TAG = "NewsFragment"
    }
    private lateinit var viewModel: NewsViewModel
    private lateinit var listNews:List<NewsDataModel>
    private val newsAdapter = NewsAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)
        NavOptions.Builder().setPopUpTo(R.id.fragment_cemetery, true).build()
        listNews=ArrayList()
        initializeRecycler()
        viewModel.requestNews()


        viewModel.ldNews.observe(viewLifecycleOwner, Observer {
            it?.let {

                Log.e(TAG, "onViewCreated: news List "+it.size )
                progress_bar.visibility = View.GONE
                newsAdapter.submitList(it)
                listNews = it
            }
        })

//        viewModel.itemPageList.observe(viewLifecycleOwner, {
//            progress_bar.visibility = View.VISIBLE
//
//            newsAdapter.submitList(it)
//            progress_bar.visibility = View.GONE
//
//        })


    }


    fun initializeRecycler() {
        NewsRecycler.adapter = newsAdapter
        NewsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    override fun newsCallBack(position: Int) {
        Log.e(TAG, "newsCallBack: "+listNews[position] )
        var args= Bundle()
        args.putParcelable("DetailsNews", listNews[position])
        findNavController().navigate(R
            .id.action_fragment_news_to_details_news_fragment,args)
//
    }
}
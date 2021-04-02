package ir.co.tarhim.ui.fragments.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.model.news.NewsDataModel
import kotlinx.android.synthetic.main.details_news.*

class DetailsNewsFragment : Fragment() {

    private lateinit var newItem: NewsDataModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (requireArguments().getParcelable<NewsDataModel>("DetailsNews") != null) {
            newItem = arguments?.getParcelable("DetailsNews")!!
            initUi(newItem)
        }
    }

    fun initUi(new: NewsDataModel) {

        TvTitleOfNews.text = new.topic
        Glide.with(requireContext())
            .load(new.imageurl)
            .into(IvDetailsNews)
        TvTopicDetailsNews.text = new.text


        BtnBackDetailsNews.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}
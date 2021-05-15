package ir.co.mazar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.mazar.R
import ir.co.mazar.model.news.NewsDataModel
import ir.co.mazar.ui.callback.NewsListener
import kotlinx.android.synthetic.main.row_news_recycler.view.*

class NewsAdapter(var callBack: NewsListener) :
    ListAdapter<NewsDataModel, NewsAdapter.NewsHolder>(NewsDiff()) {
    class NewsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgNews: AppCompatImageView
        val titleNews: AppCompatTextView
        val topicNews: AppCompatTextView


        init {
            imgNews = view.IvNews
            titleNews = view.TvTitleNews
            topicNews = view.TvdescriptionNews
        }

        fun bindPost(item: NewsDataModel) {

            val url = item.imageurl
            if (url != null) {
                if(url.startsWith("https")){
                    Glide.with(itemView.context)
                        .load(url)
                        .centerInside()
                        .circleCrop()
                        .into(imgNews)
                }else{
                    Glide.with(itemView.context)
                        .load(url.replace("http","https"))
                        .centerInside()
                        .into(imgNews)
                }
            }


            topicNews.text = "${item.text}..."
            titleNews.text = item.topic
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.row_news_recycler, parent, false)

        return NewsHolder(view)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.bindPost(getItem(position))

        holder.itemView.setOnClickListener {
            callBack.newsCallBack(position)
        }

    }


    class NewsDiff : DiffUtil.ItemCallback<NewsDataModel>() {
        override fun areItemsTheSame(oldItem: NewsDataModel, newItem: NewsDataModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NewsDataModel, newItem: NewsDataModel): Boolean {
            return oldItem.id == newItem.id

        }

    }

}
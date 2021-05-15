package ir.co.mazar.ui.activities.deceased.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.mazar.R
import ir.co.mazar.model.deceased.FollowersDataModel
import kotlinx.android.synthetic.main.row_followers_recycler.view.*


class FollowersRecyclerAdapter() :
    ListAdapter<FollowersDataModel, FollowersRecyclerAdapter.ViewHolder>(FollowersDiffCallBack()) {

    companion object {
        private const val TAG = "LatestSearchRecyclerAda"
    }

    open class FollowersDiffCallBack() : DiffUtil.ItemCallback<FollowersDataModel>() {
        override fun areItemsTheSame(
            oldItem: FollowersDataModel,
            newItem: FollowersDataModel
        ): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(
            oldItem: FollowersDataModel,
            newItem: FollowersDataModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val imageFollowers: AppCompatImageView
        val nameFollowers: AppCompatTextView

        init {
            imageFollowers = v.IvFollowers
            nameFollowers = v.TvFollowersName
        }

        fun bindTo(follower: FollowersDataModel) {
            val url = follower.imageurl
            if(url.startsWith("https")){
                Glide.with(itemView.context)
                    .load(follower.imageurl)
                    .centerInside()
                    .placeholder(R.drawable.profil_pic)
                    .circleCrop()
                    .into(imageFollowers)
            }else{
                Glide.with(itemView.context)
                    .load(url.replace("http","https"))
                    .centerInside()
                    .placeholder(R.drawable.profil_pic)
                    .circleCrop()
                    .into(imageFollowers)
            }
            nameFollowers.text = follower.name



        }

    }

    public fun clearAdapter() {
        var size = itemCount
        if (size > 0) {
            for (i in 0..size) {
                currentList.removeAt(i)
            }
            notifyItemRangeRemoved(0, size)
        }
    }

    override fun onCreateViewHolder(group: ViewGroup, p1: Int): ViewHolder {
        val view =
            LayoutInflater.from(group.context).inflate(R.layout.row_followers_recycler, group, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))

    }
}
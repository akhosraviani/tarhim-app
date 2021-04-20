package ir.co.tarhim.ui.activities.invite_friend.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.co.tarhim.R
import ir.co.tarhim.model.user.FollowersDataModel
import kotlinx.android.synthetic.main.row_contact_recycler.view.*

class FollowersAdapterRecycler() :
    ListAdapter<FollowersDataModel, FollowersAdapterRecycler.ViewHolder>(FollowersDiffUnit()) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var TvFollowersName: AppCompatTextView
        lateinit var TvFollowersNumber: AppCompatTextView
        lateinit var TvFollowersStatus: AppCompatImageView
        lateinit var BtnMoreFollowers: AppCompatImageButton

        init {
            TvFollowersName = view.TvFollowersName
            TvFollowersNumber = view.TvFollowersPhone
            TvFollowersStatus = view.TvFollowersStatus
            BtnMoreFollowers = view.BtnFollowersMore
        }


        fun bind(item: FollowersDataModel) {

            TvFollowersName.setText(item.name)
            TvFollowersNumber.setText(item.mobile)

        }

    }


    class FollowersDiffUnit : DiffUtil.ItemCallback<FollowersDataModel>() {
        override fun areItemsTheSame(p0: FollowersDataModel, p1: FollowersDataModel): Boolean {
            return p0.id == p1.id
        }

        override fun areContentsTheSame(p0: FollowersDataModel, p1: FollowersDataModel): Boolean {
            return p0 == p1
        }


    }

    override fun onCreateViewHolder(view: ViewGroup, p1: Int): FollowersAdapterRecycler.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(view.context).inflate(R.layout.row_contact_recycler, view, false)
        )
    }

    override fun onBindViewHolder(holder: FollowersAdapterRecycler.ViewHolder, p1: Int) {
        holder.bind(getItem(p1))

        holder.BtnMoreFollowers.setOnClickListener {
            Toast.makeText(holder.itemView.context, "در حال پیاده سازی", Toast.LENGTH_SHORT).show()
        }

    }
}
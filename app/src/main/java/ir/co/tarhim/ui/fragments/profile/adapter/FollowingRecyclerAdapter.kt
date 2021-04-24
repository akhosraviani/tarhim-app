package ir.co.tarhim.ui.fragments.profile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.MyDeceasedDataModel
import ir.co.tarhim.ui.callback.ProfileListener
import kotlinx.android.synthetic.main.row_recycler_following.view.*
import java.util.*

class FollowingRecyclerAdapter(
    var followingListener: ProfileListener.MyDeceasedListener,
    var unFollowDeceasedListener: ProfileListener.UnFollowDeceasedListener
) : ListAdapter<MyDeceasedDataModel, FollowingRecyclerAdapter.ViewHolder>(FollowingDiffUnit()) {

    class FollowingDiffUnit : DiffUtil.ItemCallback<MyDeceasedDataModel>() {
        override fun areItemsTheSame(
            oldItem: MyDeceasedDataModel,
            newItem: MyDeceasedDataModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MyDeceasedDataModel,
            newItem: MyDeceasedDataModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var IvFollowingImage: AppCompatImageView
        lateinit var TvFollowingName: AppCompatTextView
        lateinit var TvFollowingDate: AppCompatTextView
        lateinit var BtnRemoveFollowing: AppCompatButton
        lateinit var FollowingRowRoot: ConstraintLayout

        init {
            IvFollowingImage = itemView.IvFollowingImage
            TvFollowingName = itemView.TvFollowingName
            TvFollowingDate = itemView.TvFollowingDate
            BtnRemoveFollowing = itemView.BtnRemoveFollowing
            FollowingRowRoot = itemView.FollowingRowRoot
        }

        fun bind(item: MyDeceasedDataModel) {

            Glide
                .with(itemView.context)
                .load(item.imageurl)
                .circleCrop()
                .into(IvFollowingImage)

            TvFollowingName.setText(item.name)
//
//            var birthData= Date((item.birthday).toLong())
//            var deathData= Date((item.deathday).toLong())
//            var format=SimpleDateFormat("yyyy:dd:MM")
//
//            var birthTime=format.format(birthData)
//            var deathTime=format.format(deathData)
//
//            var birth="${PersianDate.SolarCalendar(Date(birthTime)).year}/${PersianDate.SolarCalendar(Date(birthTime)).month}/${PersianDate.SolarCalendar(Date(birthTime)).date}"
//            var death="${PersianDate.SolarCalendar(Date(deathTime)).year}/${PersianDate.SolarCalendar(Date(deathTime)).month}/${PersianDate.SolarCalendar(Date(deathTime)).date}"


            TvFollowingDate.setText("${item.deathday} - ${item.birthday}")
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        p1: Int
    ): FollowingRecyclerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_recycler_following, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FollowingRecyclerAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))

        holder.FollowingRowRoot.setOnClickListener {
            followingListener.myDeceasedCallBack(getItem(holder.adapterPosition).id)
        }
        holder.BtnRemoveFollowing.setOnClickListener {
            unFollowDeceasedListener.unFollowCallBack(getItem(position).id)
        }
    }
}
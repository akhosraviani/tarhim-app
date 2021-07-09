package ir.co.mazar.ui.fragments.profile.adapter

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
import ir.co.mazar.R
import ir.co.mazar.model.deceased.MyDeceasedDataModel
import ir.co.mazar.ui.callback.ProfileListener
import ir.co.mazar.utils.PersianDate
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
            val url = item.imageurl
            if(url.startsWith("https")){
                Glide.with(itemView.context)
                    .load(url)
                    .circleCrop()
                    .into(IvFollowingImage)
            }else{
                Glide.with(itemView.context)
                    .load(url.replace("http","https"))
                    .circleCrop()
                    .into(IvFollowingImage)
            }

            TvFollowingName.text = item.name
            var dateBirthDay = Date((item.birthday).toLong()*1000)
            var dateDeathDay = Date((item.deathday).toLong()*1000)
            val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
            val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)

            var birthDay = "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
            var deathDay = "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"


            TvFollowingDate.setText("${birthDay} - ${deathDay}")
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
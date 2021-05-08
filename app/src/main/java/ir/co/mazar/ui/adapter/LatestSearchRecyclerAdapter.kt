package ir.co.mazar.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.mazar.R
import ir.co.mazar.model.deceased.DeceasedDataModel
import ir.co.mazar.ui.callback.DeleteLatestListener
import ir.co.mazar.ui.callback.LatestRecyclerListener
import ir.co.mazar.utils.PersianDate
import kotlinx.android.synthetic.main.row_latest_deceased.view.*
import java.util.*


class LatestSearchRecyclerAdapter(
    private val latestListener: LatestRecyclerListener,
    var deleteLatestListener: DeleteLatestListener
) :
    ListAdapter<DeceasedDataModel, LatestSearchRecyclerAdapter.ViewHolder>(DeceasedDiffCallBack()) {

    companion object {
        private const val TAG = "LatestSearchRecyclerAda"
    }

    open class DeceasedDiffCallBack() : DiffUtil.ItemCallback<DeceasedDataModel>() {
        override fun areItemsTheSame(
            oldItem: DeceasedDataModel,
            newItem: DeceasedDataModel
        ): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(
            oldItem: DeceasedDataModel,
            newItem: DeceasedDataModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val imageDeceased: AppCompatImageView
        val nameDeceased: AppCompatTextView
        val birth_DeathDay: AppCompatTextView
        val BtnRemoveLatest: AppCompatButton
        val rowRoot: ConstraintLayout

        init {
            imageDeceased = v.IvFollowingImage
            nameDeceased = v.TvFollowingName
            birth_DeathDay = v.TvFollowingDate
            BtnRemoveLatest = v.BtnRemovelatest
            rowRoot = v.latest_row_root
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindTo(deceased: DeceasedDataModel) {
            Glide.with(itemView.context)
                .load(deceased.imageurl)
                .centerInside()
                .circleCrop()
                .into(imageDeceased)

            nameDeceased.text = deceased.name

//
            var dateBirthDay = Date((deceased.birthday).toLong())
            var dateDeathDay = Date((deceased.deathday).toLong())
            val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
            val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)

            var birthDay = "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
            var deathDay = "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"


            birth_DeathDay.text = "${birthDay} - ${deathDay}"


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
            LayoutInflater.from(group.context).inflate(R.layout.row_latest_deceased, group, false)

        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))

        holder.BtnRemoveLatest.setOnClickListener {
            deleteLatestListener.deleteCallback(getItem(holder.adapterPosition).recordid)
        }

        holder.rowRoot.setOnClickListener {
            latestListener.latestCallBack(getItem(holder.adapterPosition).id)
        }

    }
}
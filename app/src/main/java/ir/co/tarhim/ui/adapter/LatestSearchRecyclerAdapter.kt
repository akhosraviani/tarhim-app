package ir.co.tarhim.ui.adapter

import android.os.Build
import android.provider.ContactsContract
import android.util.Log
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
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.DeceasedDataModel
import ir.co.tarhim.ui.callback.DeceasedRecyclerCallBack
import ir.co.tarhim.utils.PersianDate
import ir.co.tarhim.utils.PersianDate.SolarCalendar
import kotlinx.android.synthetic.main.row_latest_deceased.view.*
import java.lang.String
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class LatestSearchRecyclerAdapter(private val deceasedCallBack: DeceasedRecyclerCallBack) :
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

    class ViewHolder(v: View, val callBack: DeceasedRecyclerCallBack) : RecyclerView.ViewHolder(v) {
        val imageDeceased: AppCompatImageView
        val nameDeceased: AppCompatTextView
        val birth_DeathDay: AppCompatTextView

        init {
            imageDeceased = v.IVDeceased
            nameDeceased = v.TvDeceasedName
            birth_DeathDay = v.TvBornDeceased
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindTo(deceased: DeceasedDataModel) {
            Glide.with(itemView.context)
                .load(deceased.imageurl)
                .centerInside()
                .circleCrop()
                .into(imageDeceased)

            nameDeceased.text = deceased.name

            val loc = Locale("en_US")
//
//            var dateBirthDay = Date(deceased.birthday)
//            var dateDeathDay = Date(deceased.deathday)
//            val scBirthDay = SolarCalendar(dateBirthDay)
//            val scDeathDay = SolarCalendar(dateDeathDay)
//
//            var birthDay = "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
//            var deathDay = "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"

            birth_DeathDay.text = "${deceased.birthday} - ${deceased.deathday}"

            itemView.setOnClickListener {

                callBack.getId(deceased.id)
            }

        }


    }

    override fun onCreateViewHolder(group: ViewGroup, p1: Int): ViewHolder {
        val view =
            LayoutInflater.from(group.context).inflate(R.layout.row_latest_deceased, group, false)

        return ViewHolder(view, deceasedCallBack)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}
package ir.co.mazar.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.mazar.R
import ir.co.mazar.model.deceased.DeceasedDataModel
import ir.co.mazar.ui.callback.SearchListener
import ir.co.mazar.utils.PersianDate
import kotlinx.android.synthetic.main.row_latest_deceased.view.*
import java.util.*


class SearchRecyclerAdapter(var searchCallBack:SearchListener) :
    ListAdapter<DeceasedDataModel, SearchRecyclerAdapter.ViewHolder>(DeceasedDiffCallBack()) {

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
        val btnRemoveDeceased: AppCompatButton

        init {
            imageDeceased = v.IvFollowingImage
            nameDeceased = v.TvFollowingName
            birth_DeathDay = v.TvFollowingDate
            btnRemoveDeceased = v.BtnRemovelatest
            btnRemoveDeceased.visibility = View.GONE
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindTo(deceased: DeceasedDataModel) {

            val url = deceased.imageurl
            if(url.startsWith("https")){
                Glide.with(itemView.context)
                    .load(url)
                    .centerInside()
                    .circleCrop()
                    .into(imageDeceased)
            }else{
                Glide.with(itemView.context)
                    .load(url.replace("http","https"))
                    .centerInside()
                    .circleCrop()
                    .into(imageDeceased)
            }

            nameDeceased.text = deceased.name

            val loc = Locale("en_US")

            var dateBirthDay = Date()
            var dateDeathDay = Date()

            if(deceased.birthday != ""){
                dateBirthDay = Date((deceased.birthday).toLong()*1000)
            }else{
                dateBirthDay =Date(("1619035200").toLong())
            }

            if(deceased.deathday != ""){
                dateDeathDay = Date((deceased.deathday).toLong()*1000)
            }else{
                dateDeathDay =Date(("1619035200").toLong())
            }

            val scBirthDay = PersianDate.SolarCalendar(dateBirthDay)
            val scDeathDay = PersianDate.SolarCalendar(dateDeathDay)

            val birthDay = "${scBirthDay.year}/${scBirthDay.month}/${scBirthDay.date}"
            val deathDay = "${scDeathDay.year}/${scDeathDay.month}/${scDeathDay.date}"

            birth_DeathDay.text = "${birthDay} - ${deathDay}"

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
        holder.itemView.setOnClickListener {
            searchCallBack.serachClickCallBack(getItem(holder.adapterPosition).id)
        }
    }
}
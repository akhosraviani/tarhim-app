package ir.co.mazar.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.mazar.R
import ir.co.mazar.model.deceased.DeceasedDataModel
import kotlinx.android.synthetic.main.row_latest_deceased.view.*

class DeceasedSearchRecyclerAdapter(private val listDeceased: List<DeceasedDataModel>) :
    RecyclerView.Adapter<DeceasedSearchRecyclerAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val imageDeceased: AppCompatImageView
        val nameDeceased: AppCompatTextView
        val birth_DeathDay: AppCompatTextView

        init {
            imageDeceased = v.IvFollowingImage
            nameDeceased = v.TvFollowingName
            birth_DeathDay = v.TvFollowingDate
            v.rootView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            v?.setOnClickListener { }
        }
    }

    override fun onCreateViewHolder(group: ViewGroup, p1: Int): ViewHolder {
        val view =
            LayoutInflater.from(group.context).inflate(R.layout.row_latest_deceased, group, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val url = listDeceased[position].imageurl
        if(url.startsWith("https")){
            Log.i("testTag333","tsss")
            Glide.with(holder.itemView.context)
                .load(url)
                .circleCrop()
                .into(holder.imageDeceased)
        }else{
            Log.i("testTag333","fgnss")
            Glide.with(holder.itemView.context)
                .load(url.replace("http","https"))
                .circleCrop()
                .into(holder.imageDeceased)
        }


        holder.nameDeceased.text = listDeceased.get(position).name
        holder.birth_DeathDay.text =
            "${listDeceased.get(position).birthday} - ${listDeceased.get(position).deathday}"
        holder.itemView.setOnClickListener {

//            startActivity(
//                Intent(holder.itemView.context, DeceasedPageActivity::class.java)
//                .putExtra("DeceasedId" , listDeceased.get(holder.adapterPosition).id)
//            )

        }
    }

    override fun getItemCount(): Int {
        return listDeceased.size
    }
}
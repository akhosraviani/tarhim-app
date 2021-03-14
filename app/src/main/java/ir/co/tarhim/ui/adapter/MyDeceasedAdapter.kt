package ir.co.tarhim.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.DeceasedDataModel
import ir.co.tarhim.model.deceased.MydeceasedDataModel
import kotlinx.android.synthetic.main.row_latest_deceased.view.*

class MyDeceasedAdapter(
    private val listDeceased: List<MydeceasedDataModel>,
    var nameParent: String
) :
    RecyclerView.Adapter<MyDeceasedAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        val imageDeceased: AppCompatImageView
        val nameDeceased: AppCompatTextView
        val birth_DeathDay: AppCompatTextView

        init {
            imageDeceased = v.IVDeceased
            nameDeceased = v.TvDeceasedName
            birth_DeathDay = v.TvBornDeceased
            v.rootView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            v?.setOnClickListener { }
        }
    }

    override fun onCreateViewHolder(group: ViewGroup, p1: Int): ViewHolder {
        val view =
            LayoutInflater.from(group.context)
                .inflate(R.layout.row_deceased_pages_recycler, group, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(listDeceased.get(position).imageurl)
            .circleCrop()
            .into(holder.imageDeceased)

        holder.nameDeceased.text = listDeceased.get(position).name
        holder.birth_DeathDay.text =
            "${listDeceased.get(position).birthday} - ${listDeceased.get(position).deathday}"
        holder.itemView.setOnClickListener {
            val args = bundleOf("DeceasedId" to listDeceased.get(holder.adapterPosition).id)
            if (nameParent.equals("MyDeceasedFragment"))
                holder.itemView.findNavController()
                    .navigate(R.id.action_fragment_profile_to_fragment_deceased_page, args)
            else
                holder.itemView.findNavController()
                    .navigate(R.id.action_fragment_cemetery_to_fragment_deceased_page, args)

        }
    }

    override fun getItemCount(): Int {
        return listDeceased.size
    }
}
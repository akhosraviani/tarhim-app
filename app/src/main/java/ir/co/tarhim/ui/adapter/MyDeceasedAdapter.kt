package ir.co.tarhim.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.MyDeceasedDataModel
import ir.co.tarhim.ui.callback.ProfileListener
import kotlinx.android.synthetic.main.row_deceased_pages_recycler.view.*
import kotlinx.android.synthetic.main.row_latest_deceased.view.*
import kotlinx.android.synthetic.main.row_latest_deceased.view.IVDeceased
import kotlinx.android.synthetic.main.row_latest_deceased.view.TvBornDeceased
import kotlinx.android.synthetic.main.row_latest_deceased.view.TvDeceasedName

class MyDeceasedAdapter(
    private val listDeceased: List<MyDeceasedDataModel>,
    var nameParent: String,
    private val editdeceasedCallBack: ProfileListener.MyDeceasedEditCallBack,
    private val deceasedCallBack: ProfileListener.MyDeceasedListener,
) :
    RecyclerView.Adapter<MyDeceasedAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        val imageDeceased: AppCompatImageView
        val nameDeceased: AppCompatTextView
        val birth_DeathDay: AppCompatTextView
        val editDeceased: AppCompatButton

        init {
            imageDeceased = v.IVDeceased
            nameDeceased = v.TvDeceasedName
            birth_DeathDay = v.TvBornDeceased
            editDeceased = v.BtnEditDeceased


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
            deceasedCallBack.myDeceasedCallBack(listDeceased.get(holder.adapterPosition).id)
        }
        holder.editDeceased.setOnClickListener {
            editdeceasedCallBack.editDeceased(listDeceased[position])
        }

    }

    override fun getItemCount(): Int {
        return listDeceased.size
    }
}
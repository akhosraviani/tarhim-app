package ir.co.tarhim.ui.fragments.deceased.adapter

import android.content.Context
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.contentValuesOf
import androidx.navigation.navOptions
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.CharityDataModel
import ir.co.tarhim.ui.callback.CharityListener
import kotlinx.android.synthetic.main.row_charity_recycler.view.*

class CharityRecyclerAdapter(var charityListener: CharityListener) :
    ListAdapter<CharityDataModel, CharityRecyclerAdapter.ViewHolder>(CharityDiffUnit()) {


    class CharityDiffUnit() : DiffUtil.ItemCallback<CharityDataModel>() {
        override fun areItemsTheSame(
            oldItem: CharityDataModel,
            newItem: CharityDataModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CharityDataModel,
            newItem: CharityDataModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var ImCharity: AppCompatImageView
        lateinit var TvNameCharity: AppCompatTextView

        init {
            TvNameCharity = view.TvCharityName

        }

        open fun bind(charityItem: CharityDataModel) {

            TvNameCharity.setText(charityItem.name)


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharityRecyclerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_charity_recycler, parent, false)
        )


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))


        holder.itemView.setOnClickListener {
            charityListener.chalityCallback(getItem(position).id)
        }


    }


}
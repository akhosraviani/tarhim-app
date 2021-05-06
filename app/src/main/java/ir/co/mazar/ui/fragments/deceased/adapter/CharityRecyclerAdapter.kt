package ir.co.mazar.ui.fragments.deceased.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.co.mazar.R
import ir.co.mazar.model.deceased.CharityDataModel
import ir.co.mazar.ui.callback.CharityListener
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
        lateinit var TvBadgeNotif: AppCompatTextView
        lateinit var BtnCharity: AppCompatButton
        lateinit var badgeViewRoot: LinearLayout

        init {
            BtnCharity = view.BtnCharity
            TvBadgeNotif = view.badge_notification
            badgeViewRoot = view.badgeViewRoot

        }

        open fun bind(charityItem: CharityDataModel) {

            badgeViewRoot.visibility = View.GONE
            BtnCharity.setText(charityItem.name)


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


        holder.BtnCharity.setOnClickListener {
            charityListener.chalityCallback(getItem(position).url)
        }


    }


}
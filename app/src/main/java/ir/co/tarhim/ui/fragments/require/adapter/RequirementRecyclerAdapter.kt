package ir.co.tarhim.ui.fragments.require.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.RequirementDataModel
import kotlinx.android.synthetic.main.row_requirement_recycler.view.*

class RequirementRecyclerAdapter :
    ListAdapter<RequirementDataModel, RequirementRecyclerAdapter.ViewHolder>(RequirementDiffUnit()) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var TvUserReq: AppCompatTextView
        lateinit var TvTypeReq: AppCompatTextView
        lateinit var IvIcon: AppCompatImageView
        lateinit var IvUserReq: AppCompatImageView
        lateinit var TvDecs: AppCompatTextView
        lateinit var BtnMore: AppCompatTextView
        var popupState = false

        init {

            TvUserReq = view.TvNameRequirement
            TvTypeReq = view.TvTypeRequirement
            IvUserReq = view.IvUserRequirement
            TvDecs = view.TvSubjectRequirement
            BtnMore = view.BtnMoreDescription
        }

       fun bind(item: RequirementDataModel) {

            Glide.with(itemView.context)
                .load(item.imageUrl)
                .circleCrop()
                .into(IvUserReq)
            TvUserReq.setText(item.name)
            TvTypeReq.setText(item.subject)
            TvDecs.setText(item.message)
            TvDecs.maxLines = 3

            var lineCount = TvDecs.lineCount
            if (lineCount > 3) {
                popupState = true
                BtnMore.visibility = View.VISIBLE
            }

            BtnMore.setOnClickListener {
                if (popupState) {
                    popupState = false
                    var animation = ObjectAnimator.ofInt(TvDecs, "maxLines", lineCount)
                    animation.startDelay = 50
                    BtnMore.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_arrow_down,
                        0,
                        0,
                        0
                    )
                } else {
                    popupState = false
                    var animation = ObjectAnimator.ofInt(TvDecs, "maxLines", 3)
                    animation.startDelay = 50
                    BtnMore.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        R.drawable.ic_arrow_up,
                        0,
                        0,
                        0
                    )
                }
            }

        }


    }


    class RequirementDiffUnit : DiffUtil.ItemCallback<RequirementDataModel>() {
        override fun areItemsTheSame(
            oldItem: RequirementDataModel,
            newItem: RequirementDataModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RequirementDataModel,
            newItem: RequirementDataModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_requirement_recycler, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position))

    }

}
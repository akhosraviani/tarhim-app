package ir.co.mazar.ui.fragments.require.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.mazar.R
import ir.co.mazar.model.deceased.RequirementDataModel
import kotlinx.android.synthetic.main.row_requirement_recycler.view.*

class RequirementRecyclerAdapter :
    ListAdapter<RequirementDataModel, RequirementRecyclerAdapter.ViewHolder>(RequirementDiffUnit()) {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        lateinit var TvUserReq: AppCompatTextView
        lateinit var TvTypeReq: AppCompatTextView
        lateinit var IvIcon: AppCompatImageView
        lateinit var IvUserReq: AppCompatImageView
        lateinit var moreMenuBtn: AppCompatImageButton
        lateinit var TvDecs: AppCompatTextView
        lateinit var BtnMore: AppCompatTextView
        var popupState = false

        init {

            TvUserReq = view.TvNameRequirement
            TvTypeReq = view.TvTypeRequirement
            IvUserReq = view.IvUserRequirement
            TvDecs = view.TvSubjectRequirement
            BtnMore = view.BtnMoreDescription
            moreMenuBtn = view.BtnMore
        }

        fun bind(item: RequirementDataModel) {

            item.imageUrl?.let {
                if (it.startsWith("https")) {
                    Glide.with(itemView.context)
                        .load(it)
                        .circleCrop()
                        .into(IvUserReq)
                } else {
                    Glide.with(itemView.context)
                        .load(it.replace("http", "https"))
                        .circleCrop()
                        .into(IvUserReq)
                }
            }

            moreMenuBtn.setOnClickListener{
                it?.let { v->
                    showMenu(v, R.menu.common_menu)
                }
            }


            TvUserReq.text = item.name
            TvTypeReq.text = item.subject
            TvDecs.text = item.message
            TvDecs.maxLines = 3

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

    private lateinit var activity: AppCompatActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        activity = parent.context as AppCompatActivity
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_requirement_recycler, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position))

    }

    private fun showMenu(v: View, @MenuRes menuRes: Int) {
        val popup = PopupMenu(activity, v)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.report -> {
                    Toast.makeText(activity, "گزارش شما با موفقیت ثبت گردید", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> throw UnsupportedOperationException("there is not this item")
            }
        }

        popup.show()
    }
}
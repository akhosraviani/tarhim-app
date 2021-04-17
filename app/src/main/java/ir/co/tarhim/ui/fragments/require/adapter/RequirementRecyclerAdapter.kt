package ir.co.tarhim.ui.fragments.require.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.PrayDataModel

class RequirementRecyclerAdapter :
    ListAdapter<PrayDataModel, RequirementRecyclerAdapter.ViewHolder>(RequirementDiffUnit()) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    }


    class RequirementDiffUnit : DiffUtil.ItemCallback<PrayDataModel>() {
        override fun areItemsTheSame(oldItem: PrayDataModel, newItem: PrayDataModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PrayDataModel, newItem: PrayDataModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_requirement_recycler,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}
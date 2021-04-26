package ir.co.tarhim.ui.activities.inbox.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.MyInboxDataModel
import ir.co.tarhim.utils.PersianDate
import kotlinx.android.synthetic.main.row_inbox_recycler.view.*
import java.text.SimpleDateFormat
import java.util.*

class InboxRecyclerAdapter(var admin:Boolean) :
    ListAdapter<MyInboxDataModel, InboxRecyclerAdapter.ViewHolder>(InboxDiffUnit()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var TvDate: AppCompatTextView
        private lateinit var TvFrom: AppCompatTextView
        private lateinit var TvStatus: AppCompatTextView
        private lateinit var TvDesc: AppCompatTextView
        private lateinit var BtnAcceptRequest: AppCompatButton
        private lateinit var BtnDeclineRequest: AppCompatButton

        init {

            TvDate = view.Tv_Inbox_message_date
            TvFrom = view.Tv_Inbox_from
            TvStatus = view.Tv_Inbox_message_status
            TvDesc = view.Tv_Inbox_message_description
            BtnAcceptRequest = view.BtnAcceptRequest
            BtnDeclineRequest = view.BtnDeclineRequest
        }


        open fun bindTo(item: MyInboxDataModel,admin:Boolean) {

            if(admin){
                BtnAcceptRequest.visibility=View.GONE
                BtnDeclineRequest.visibility=View.GONE
            }

            var d = (item.date).toLong()
            val formatData = SimpleDateFormat("yyyy/MM/dd")
            var time = formatData.format(d)

            var year = PersianDate.SolarCalendar(Date(time)).year
            var month = PersianDate.SolarCalendar(Date(time)).month
            var day = PersianDate.SolarCalendar(Date(time)).date
            TvDate.setText("${year}-${month}-${day}")

            TvFrom.setText(item.name)
            var description: String
            TvStatus.setText(item.subject)
            if (item.message.length > 30) {
                description = "${item.message.substring(0, 30)} ..."

            } else {
                description = item.message
            }
            TvDesc.setText(description)
        }

    }


    open class InboxDiffUnit : DiffUtil.ItemCallback<MyInboxDataModel>() {
        override fun areItemsTheSame(
            oldItem: MyInboxDataModel,
            newItem: MyInboxDataModel
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: MyInboxDataModel,
            newItem: MyInboxDataModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.row_inbox_recycler, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position),admin)

    }


}
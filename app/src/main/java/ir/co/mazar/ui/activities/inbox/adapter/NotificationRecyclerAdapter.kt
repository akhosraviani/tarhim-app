package ir.co.mazar.ui.activities.inbox.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.co.mazar.R
import ir.co.mazar.model.deceased.MyInboxDataModel
import ir.co.mazar.model.deceased.NotificationMessageDataModel
import ir.co.mazar.ui.callback.InboxListener
import ir.co.mazar.utils.PersianDate
import kotlinx.android.synthetic.main.row_inbox_recycler.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotificationRecyclerAdapter(var inboxListener: InboxListener) :
    ListAdapter<NotificationMessageDataModel, NotificationRecyclerAdapter.ViewHolder>(InboxDiffUnit()) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var TvDate: AppCompatTextView
        lateinit var TvFrom: AppCompatTextView
        lateinit var TvStatus: AppCompatTextView
        lateinit var TvDesc: AppCompatTextView
        lateinit var BtnAcceptRequest: AppCompatButton
        lateinit var BtnDeclineRequest: AppCompatButton

        init {

            TvDate = view.Tv_Inbox_message_date
            TvFrom = view.Tv_Inbox_from
            TvStatus = view.Tv_Inbox_message_status
            TvDesc = view.Tv_Inbox_message_description
            BtnAcceptRequest = view.BtnAcceptRequest
            BtnDeclineRequest = view.BtnDeclineRequest
        }


        open fun bindTo(item: NotificationMessageDataModel) {

            if (item.type.equals("FollowReq")) {
                BtnAcceptRequest.visibility = View.VISIBLE
                BtnDeclineRequest.visibility = View.VISIBLE
            }



            var d = (item.date).toLong()
            val formatData = SimpleDateFormat("yyyy/MM/dd")
            var time = formatData.format(d)

            var year = PersianDate.SolarCalendar(Date(time)).year
            var month = PersianDate.SolarCalendar(Date(time)).month
            var day = PersianDate.SolarCalendar(Date(time)).date
            TvDate.setText("${year}-${month}-${day}")

            TvFrom.setText(item.type)
            TvDesc.setText(item.text)
        }

    }


    open class InboxDiffUnit : DiffUtil.ItemCallback<NotificationMessageDataModel>() {
        override fun areItemsTheSame(
            oldItem: NotificationMessageDataModel,
            newItem: NotificationMessageDataModel
        ): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(
            oldItem: NotificationMessageDataModel,
            newItem: NotificationMessageDataModel
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
        holder.bindTo(getItem(position))


//        holder.BtnAcceptRequest.setOnClickListener {
//            inboxListener.acceptRequestCallback(getItem(holder.adapterPosition).notificationId)
//        }
//
//        holder.BtnDeclineRequest.setOnClickListener {
//            inboxListener.declineRequestCallback(getItem(holder.adapterPosition).notificationId)
//        }

    }


}
package ir.co.mazar.ui.activities.invite_friend.adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.co.mazar.R
import ir.co.mazar.model.deceased.FollowersDataModel
import ir.co.mazar.model.deceased.PrayDeceasedRequest
import ir.co.mazar.ui.activities.invite_friend.ContactModel
import ir.co.mazar.ui.adapter.MyDeceasedAdapter
import ir.co.mazar.utils.PrayDeceasedType
import kotlinx.android.synthetic.main.item_contact.view.*
import kotlinx.android.synthetic.main.row_contact_recycler.view.*

class InviteAdapterRecycler(private var context : Context, private val data : List<ContactModel>) :
    RecyclerView.Adapter<InviteAdapterRecycler.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

//        lateinit var TvFollowersName: AppCompatTextView
//        lateinit var TvFollowersNumber: AppCompatTextView
//        lateinit var TvFollowersStatus: AppCompatImageView
//        lateinit var BtnMoreFollowers: AppCompatImageButton


        lateinit var contactPhoneNumber: TextView
        lateinit var contactName: TextView
        lateinit var sendInvitation: TextView
        lateinit var inviteCheckBox: CheckBox


        init {
//            TvFollowersName = view.TvFollowersName
//            TvFollowersNumber = view.TvFollowersPhone
//            TvFollowersStatus = view.TvFollowersStatus
//            BtnMoreFollowers = view.BtnFollowersMore

            contactPhoneNumber = view.contactPhoneNumber
            contactName = view.contactName
            inviteCheckBox = view.inviteCheckBox
            sendInvitation = view.sendInvitation
        }


        fun bind(item: ContactModel) {
            contactPhoneNumber.text = item.phone
            contactName.text = item.name
//            TvFollowersName.text = item.name
//            TvFollowersNumber.text = item.mobile

        }

    }


    class FollowersDiffUnit : DiffUtil.ItemCallback<ContactModel>() {
        override fun areItemsTheSame(p0: ContactModel, p1: ContactModel): Boolean {
            return p0.name == p1.name
        }

        override fun areContentsTheSame(p0: ContactModel, p1: ContactModel): Boolean {
            return p0 == p1
        }


    }

    override fun onCreateViewHolder(view: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
//            LayoutInflater.from(view.context).inflate(R.layout.row_contact_recycler, view, false)
            LayoutInflater.from(view.context).inflate(R.layout.item_contact, view, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {

        holder.contactName.text = data[p1].name
        holder.contactPhoneNumber.text = data[p1].phone

        holder.inviteCheckBox.setOnClickListener {
            if (holder.inviteCheckBox.isChecked) {
                holder.sendInvitation.visibility = View.VISIBLE
            } else {
                holder.sendInvitation.visibility = View.GONE
            }
        }

        holder.sendInvitation.setOnClickListener {
            lateinit var dialog: Dialog
            Log.i("testTag4", "dialog")
            dialog = Dialog(context)
            dialog.setContentView(R.layout.dialog_charity)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val charityTitle: TextView = dialog.findViewById(R.id.charityTitle)
            val charityYes: TextView = dialog.findViewById(R.id.charityYes)
            val charityNo: TextView = dialog.findViewById(R.id.charityNo)

            charityTitle.text = "آیا می خواهید برای مخاطب مورد نظر دعوتنامه ارسال شود؟"


            charityYes.setOnClickListener {

            }
            charityNo.setOnClickListener {
                holder.inviteCheckBox.isChecked = false
                holder.sendInvitation.visibility = View.GONE
                dialog.dismiss()

            }
            dialog.show()
        }
    }



//        holder.bind(getItem(p1))

//        holder.BtnMoreFollowers.setOnClickListener {
//            Toast.makeText(holder.itemView.context, "در حال پیاده سازی", Toast.LENGTH_SHORT).show()
//        }



    override fun getItemCount(): Int {
       return data.size
    }


}
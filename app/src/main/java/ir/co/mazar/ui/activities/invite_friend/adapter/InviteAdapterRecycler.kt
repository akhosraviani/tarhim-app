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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.co.mazar.R
import ir.co.mazar.ui.activities.invite_friend.ContactModel
import ir.co.mazar.ui.callback.InviteFriendsListener
import kotlinx.android.synthetic.main.item_contact.view.*


class InviteAdapterRecycler(private var context : Context, private val data : List<ContactModel>
                            , private val inviteFriendsListener : InviteFriendsListener , private var addedContact : MutableList<ContactModel>) :
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
        for(i in data!!.indices){
            for(j in addedContact!!.indices){
                if(addedContact[j].phone==data[i].phone){
                    holder.sendInvitation.visibility=View.VISIBLE
                    holder.sendInvitation.text = "ارسال شد"
                }
            }
        }


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
            if(holder.sendInvitation.text != "ارسال شد"){
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
                dialog.dismiss()
             inviteFriendsListener.addContact(data[p1].phone)

            }
            charityNo.setOnClickListener {
                holder.inviteCheckBox.isChecked = false
                holder.sendInvitation.visibility = View.GONE
                dialog.dismiss()

            }
            dialog.show()
        }
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
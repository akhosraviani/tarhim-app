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


class InviteAdapterRecycler(
    private var context: Context,
    private val data: List<ContactModel>,
    private val inviteFriendsListener: InviteFriendsListener,
) :
    RecyclerView.Adapter<InviteAdapterRecycler.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var contactPhoneNumber: TextView
        var contactName: TextView
        var sendInvitation: TextView
        var sendInvitationLink: TextView
        var inviteCheckBox: CheckBox


        init {

            contactPhoneNumber = view.contactPhoneNumber
            contactName = view.contactName
            inviteCheckBox = view.inviteCheckBox
            sendInvitation = view.sendInvitation
            sendInvitationLink = view.sendInvitationLink
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

    override fun onCreateViewHolder(view: ViewGroup, position: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(view.context).inflate(R.layout.item_contact, view, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {


        holder.contactName.text = data[p1].name
        holder.contactPhoneNumber.text = data[p1].phone

        holder.inviteCheckBox.setOnClickListener {
            if (holder.inviteCheckBox.isChecked) {
                holder.sendInvitation.visibility = View.VISIBLE
                holder.sendInvitationLink.visibility = View.VISIBLE
            } else {
                holder.sendInvitation.visibility = View.GONE
                holder.sendInvitationLink.visibility = View.GONE
            }
        }

        holder.sendInvitation.setOnClickListener {
            if (holder.sendInvitation.text != "ارسال شد") {
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
                    inviteFriendsListener.addContact(data[p1].phone)
                    dialog.dismiss()

                }
                charityNo.setOnClickListener {
                    holder.inviteCheckBox.isChecked = false
                    holder.sendInvitation.visibility = View.GONE
                    dialog.dismiss()

                }
                dialog.show()
            }
        }
        holder.sendInvitationLink.setOnClickListener {
            inviteFriendsListener.sendInvitationLInk(data[p1].phone)
        }

    
}

override fun getItemCount(): Int {
    return data.size
}


}
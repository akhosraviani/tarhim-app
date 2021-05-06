package ir.co.mazar.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.mazar.R
import ir.co.mazar.model.deceased.FollowersDataModel
import ir.co.mazar.ui.activities.deceased.adapter.FollowersRecyclerAdapter
import kotlinx.android.synthetic.main.followers_list_dialog.view.*

open class DialogProvider {

    lateinit var alertDialog: AlertDialog
    fun dismiss(activity: Activity) {
        if (alertDialog != null)
            alertDialog.dismiss()
    }


    fun showFollowerListDialog(activity: Activity, followersList: List<FollowersDataModel>) {
        var viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
        var view =
            LayoutInflater.from(activity).inflate(R.layout.followers_list_dialog, viewGroup, false)
        alertDialog = AlertDialog.Builder(activity)
            .setView(view).create()
        alertDialog!!.show()
        alertDialog!!.setCancelable(true)
        alertDialog!!.setCanceledOnTouchOutside(true)
        alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        var followersAdapter = FollowersRecyclerAdapter()
        view.followersRecycler.adapter = followersAdapter
        view.followersRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        view.followersRecycler.addItemDecoration(
            DividerItemDecoration(
                activity,
                DividerItemDecoration.VERTICAL
            )
        )
        followersAdapter.submitList(followersList)
        view.followersRecycler.layoutAnimation = AnimationUtils.loadLayoutAnimation(
            activity,
            R.anim.up_to_bottom
        )
    }



}




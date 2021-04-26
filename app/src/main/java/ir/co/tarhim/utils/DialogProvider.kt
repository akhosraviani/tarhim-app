package ir.co.tarhim.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.FollowersDataModel
import ir.co.tarhim.model.deceased.GalleryDataModel
import ir.co.tarhim.ui.activities.deceased.adapter.FollowersRecyclerAdapter
import kotlinx.android.synthetic.main.followers_list_dialog.view.*
import kotlinx.android.synthetic.main.gallery_image_dialog.view.*
import kotlinx.android.synthetic.main.tarhim_dialog.view.*

 class DialogProvider {

     lateinit var alertDialog: AlertDialog

    fun dismiss(activity: Activity) {
        if (this::alertDialog.isInitialized) {
            alertDialog.dismiss()
        }else{
            alertDialog=AlertDialog.Builder(activity).create()
        }
    }


    fun showImageDialog(activity: Activity, item: GalleryDataModel) {
        var viewGroup = activity.findViewById(android.R.id.content) as ViewGroup
        var view =
            LayoutInflater.from(activity).inflate(R.layout.gallery_image_dialog, viewGroup, false)
        alertDialog = AlertDialog.Builder(activity)
            .setView(view).create()
        alertDialog!!.show()
        alertDialog!!.setCanceledOnTouchOutside(true)
        alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )


        Glide.with(activity)
            .load(item.imagespath)
            .into(view.ImgGalleryDialog)

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
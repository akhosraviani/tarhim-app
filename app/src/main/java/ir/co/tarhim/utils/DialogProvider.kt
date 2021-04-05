package ir.co.tarhim.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import kotlinx.android.synthetic.main.gallery_image_dialog.view.*

class DialogProvider {

    private lateinit var dialog: AlertDialog


    public fun dismiss() {
        if (dialog != null)
            dialog.dismiss()
        else return;
    }


    public fun showImageDialog(activity: Activity, imgPath: String) {
        var viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)

        var view =
            LayoutInflater.from(activity).inflate(R.layout.gallery_image_dialog, viewGroup, false)
        dialog = AlertDialog.Builder(activity).create()
        dialog.setView(view)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        Glide.with(activity)
            .load(imgPath)
            .centerInside()
            .into(view.img_load_gallery)

        
        dialog.show()
    }

}
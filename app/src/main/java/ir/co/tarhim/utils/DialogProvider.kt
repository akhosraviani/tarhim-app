package ir.co.tarhim.utils

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.GalleryDataModel
import kotlinx.android.synthetic.main.gallery_image_dialog.view.*
import kotlinx.android.synthetic.main.tarhim_dialog.view.*

class DialogProvider {


    private lateinit var alertDialog: AlertDialog

    fun dismiss() {
        alertDialog?.dismiss()
    }


    fun showImageDialog(activity: Activity, item: GalleryDataModel) {
        var viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
        var view =
            LayoutInflater.from(activity).inflate(R.layout.gallery_image_dialog, viewGroup, false)
        alertDialog = AlertDialog.Builder(activity)
            .setView(view).create()
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(true)
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)


        Glide.with(activity)
            .load(item.imagespath)
            .into(view.ImgGalleryDialog)

    }


    fun showConfirmDialog(
        activity: Activity,
        image: Int,
        message: String,
        accept: View.OnClickListener,
        cancel: View.OnClickListener
    ) {
        val viewGroup: ViewGroup = activity.findViewById(android.R.id.content)
        val view =
            LayoutInflater.from(activity).inflate(R.layout.tarhim_dialog, viewGroup, false)
        alertDialog = AlertDialog.Builder(activity).setView(view).create()
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
        alertDialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.TvMessageDialog.setText(message)

        view.BtnAcceptDialog.setOnClickListener(accept)
        view.BtnCloseDialog.setOnClickListener(cancel)

        view.IvImageDialog.setBackgroundResource(image)

    }


}
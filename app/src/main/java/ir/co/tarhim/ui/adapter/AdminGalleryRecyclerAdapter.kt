package ir.co.tarhim.ui.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.CommentDataModel
import ir.co.tarhim.model.deceased.GalleryDataModel
import ir.co.tarhim.ui.callback.GalleryListener
import ir.co.tarhim.ui.callback.PostListener
import ir.co.tarhim.ui.callback.RepostListener
import kotlinx.android.synthetic.main.gallery_image_dialog.view.*
import kotlinx.android.synthetic.main.row_add_image_gallery.view.*
import kotlinx.android.synthetic.main.row_gallery_recycler.view.*

class AdminGalleryRecyclerAdapter(
    var data: GalleryDataModel,
    var galleryCallBack: GalleryListener,
    var postCallback: PostListener,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val galleryIv: AppCompatImageView
        val loadinggallery: ProgressBar

        init {
            galleryIv = view.IvGallery
            loadinggallery = view.loadinggallery
        }


        open fun bindTo(pathImage: String) {

            Glide.with(itemView.context)
                .load(pathImage)
                .centerCrop()
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        loadinggallery.visibility = View.GONE

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        loadinggallery.visibility = View.GONE
                        return false
                    }

                })
                .into(galleryIv)



        }
    }

    class AddImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        public lateinit var IvAddImage: AppCompatImageView

        init {
            this.IvAddImage = view.IvAddImage
        }
    }

    override fun getItemViewType(position: Int): Int {

        return when (position) {
            0 -> return VIEW_TYPE_ONE
            else -> return VIEW_TYPE_TWO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//            if (viewType == VIEW_TYPE_ONE) {
                return AddImageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.row_add_image_gallery, parent, false)
                )
//            } else {
//
//                return GalleryViewHolder(
//                    LayoutInflater.from(parent.context)
//                        .inflate(R.layout.row_gallery_recycler, parent, false)
//                )
//            }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

//            if (holder.itemViewType == VIEW_TYPE_ONE) {
                AddImageViewHolder(holder.itemView)
                (holder as AddImageViewHolder).IvAddImage.setOnClickListener {
                    postCallback.postcallBack(data.id)
//                }
//            } else {
//                GalleryViewHolder(holder.itemView)
//                (holder as GalleryViewHolder).bindTo(data.imagespath[position])
//                (holder as GalleryViewHolder).galleryIv.setOnClickListener {
//                    galleryCallBack.galleryRecyclerCallBack(data.imagespath[holder.adapterPosition])
//                }
            }
    }

    override fun getItemCount(): Int {
        return data.imagespath.size
    }

}
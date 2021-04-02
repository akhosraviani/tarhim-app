package ir.co.tarhim.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ir.co.tarhim.R
import ir.co.tarhim.ui.callback.GalleryListener
import kotlinx.android.synthetic.main.row_gallery_recycler.view.*

class GalleryRecyclerAdapter(
    var pathList: List<String>, var galleryCallBack: GalleryListener
) :
    RecyclerView.Adapter<GalleryRecyclerAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val galleryIv: AppCompatImageView
        val loadinggallery: ProgressBar

        init {
            galleryIv = view.IvGallery
            loadinggallery = view.loadinggallery

        }


        open fun bindTo(pathImage: String) {

            Glide.with(itemView.context)
                .load(pathImage)
                .centerInside()
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

    class AddViewHolder(view: View) : RecyclerView.ViewHolder(view) {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_gallery_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(pathList[position])

        holder.galleryIv.setOnClickListener {
            galleryCallBack.galleryRecyclerCallBack(position)
        }

    }

    override fun getItemCount(): Int {
        return pathList.size
    }
}
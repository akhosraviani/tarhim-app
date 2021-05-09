package ir.co.mazar.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
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
import ir.co.mazar.R
import ir.co.mazar.model.deceased.GalleryDataModel
import ir.co.mazar.ui.callback.GalleryListener
import kotlinx.android.synthetic.main.row_gallery_recycler.view.*

class GalleryRecyclerViewAdapter(
    val context: Context,
    val data: List<GalleryDataModel>,
    val galleryListener: GalleryListener,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val galleryIv: AppCompatImageView
        val loadinggallery: ProgressBar

        init {
            galleryIv = view.IvGallery
            loadinggallery = view.loadinggallery
        }


        open fun bindTo(item: GalleryDataModel) {

            Log.e("bindTo", "bindTo: " + item.imagespath)

            val url: String = java.lang.String.valueOf(item.imagespath)
            if(url.startsWith("http")){
                Glide.with(itemView.context)
                    .load(url.replace("http","https"))
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
            }else{
                Glide.with(itemView.context)
                    .load(url)
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
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return GalleryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.row_gallery_recycler, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        (holder as GalleryViewHolder)
        holder.bindTo(data.get(position))

        holder.galleryIv.setOnClickListener {
                galleryListener.galleryRecyclerCallBack(position,data[holder.adapterPosition])

            }
        }



}
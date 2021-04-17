package ir.co.tarhim.ui.adapter

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.co.tarhim.model.deceased.GalleryDataModel


class GalleryAdapter(var data: GalleryDataModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return VIEW_TYPE_ONE;
        } else {
            return VIEW_TYPE_TWO;
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        var view: View? = null
        return if (viewType === VIEW_TYPE_ONE) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_add_image_gallery, parent, false)
            ViewHolder(view)
        } else if (viewType === ITEM_TYPE_TWO) {
            view =
                LayoutInflater.from(context).inflate(R.layout.row_add_image_gallery, parent, false)
            ButtonViewHolder(view)
        } else {
            null
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var itemType = getItemViewType(position);

        if (itemType == VIEW_TYPE_ONE) {
            AdminGalleryRecyclerAdapter.AddImageViewHolder(holder.itemView)
            (holder as AdminGalleryRecyclerAdapter.AddImageViewHolder).IvAddImage.setOnClickListener {
                postCallback.postcallBack(data.id)
            }
        } else if (itemType == VIEW_TYPE_TWO) {
            GalleryViewHolder(holder.itemView)
            (holder as GalleryViewHolder).bindTo(data.imagespath[position])
            (holder as GalleryViewHolder).galleryIv.setOnClickListener {
                galleryCallBack.galleryRecyclerCallBack(data.imagespath[holder.adapterPosition])
            }
        }

    }

    override fun getItemCount(): Int {
        return data.imagespath.size
    }

    class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView
        var tvId: TextView
        var tvSearchUrl: Te xtView
        var tvNativeUrl: TextView
        var tvIcon: ImageView

        init {
            tvName = itemView.findViewById(R.id.tvName)
            tvIcon = itemView.findViewById(R.id.tvIcon)
            tvId = itemView.findViewById(R.id.tvId)
            tvSearchUrl = itemView.findViewById(R.id.tvSearchUrl)
            tvNativeUrl = itemView.findViewById(R.id.tvNativeUrl)
        }
    }

}
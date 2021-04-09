package ir.co.tarhim.ui.adapter


import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
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
import ir.co.tarhim.ui.callback.TipsListener
import kotlinx.android.synthetic.main.row_left_forum.view.*
import kotlinx.android.synthetic.main.row_right_forum.view.*
import kotlinx.android.synthetic.main.row_right_forum.view.TVCommentForum
import kotlinx.android.synthetic.main.row_right_forum.view.TVNameRightForum

class CommentRecyclerAdapter(var callBack:TipsListener) :
    ListAdapter<CommentDataModel, CommentRecyclerAdapter.ViewHolder>(CommentDiffUnit()) {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtComment: AppCompatTextView
        val nameUser: AppCompatTextView
        val imageUser: AppCompatImageView

        init {
            txtComment = view.TVCommentForum
            nameUser = view.TVNameRightForum
            imageUser = view.IVRightForum
        }

        open fun bindTo(comment: CommentDataModel) {
            txtComment.text = comment.message
            nameUser.text = comment.name
            Glide.with(itemView.context)
                .load(comment.imageurl)
                .circleCrop()
                .into(imageUser)


        }
    }


    open class CommentDiffUnit() : DiffUtil.ItemCallback<CommentDataModel>() {
        override fun areItemsTheSame(
            oldItem: CommentDataModel,
            newItem: CommentDataModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CommentDataModel,
            newItem: CommentDataModel
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.row_right_forum, parent, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
        holder.itemView.BtnMore.setOnClickListener {
            callBack.tipsCallback(getItem(holder.adapterPosition).id)
        }

    }
}
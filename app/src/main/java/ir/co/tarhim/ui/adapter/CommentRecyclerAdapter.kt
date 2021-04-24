package ir.co.tarhim.ui.adapter


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.CommentDataModel
import ir.co.tarhim.ui.LikeCommentClicked
import ir.co.tarhim.ui.callback.TipsListener
import ir.co.tarhim.ui.fragments.LikedCommentChangeColor
import kotlinx.android.synthetic.main.row_right_forum.view.*

class CommentRecyclerAdapter(
    private var likeCommentClicked: LikeCommentClicked,
    var callBack: TipsListener,
    var status: Boolean
) :
    ListAdapter<CommentDataModel, CommentRecyclerAdapter.ViewHolder>(CommentDiffUnit()),
    LikedCommentChangeColor {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtComment: AppCompatTextView
        val nameUser: AppCompatTextView
        val TVCountLike: AppCompatTextView
        val imageUser: AppCompatImageView
        val likeIcon: AppCompatImageButton

        init {
            txtComment = view.TVCommentForum
            nameUser = view.TVNameRightForum
            imageUser = view.IVRightForum
            TVCountLike = view.TVCountLike
            likeIcon = view.BtnLikeComment
        }

        open fun bindTo(comment: CommentDataModel, status: Boolean) {
//            if (!status) {
                itemView.BtnMore.visibility = View.INVISIBLE
//            }
            TVCountLike.setText("${comment.likes}")
            txtComment.text = comment.message
            nameUser.text = comment.name
            Glide.with(itemView.context)
                .load(comment.imageurl)
                .circleCrop()
                .into(imageUser)

            Log.e("testTag", "bindTo: " + comment.favourite)
            if (comment.favourite) {
                Log.i("testTag", "liked adapter red")
                likeIcon.setImageResource(R.drawable.ic_do_favorite)
            } else {
                Log.i("testTag", "liked adapter grey")
                likeIcon.setImageResource(R.drawable.ic_non_favorite)
            }

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
        holder.bindTo(getItem(position), status)
        holder.itemView.BtnMore.setOnClickListener {
            callBack.tipsCallback(getItem(holder.adapterPosition).id)
        }

        holder.likeIcon.setOnClickListener {
            if (!getItem(holder.adapterPosition).favourite) {
                Log.i("testTag", "liked adapter red2")
                likeCommentClicked.likeCommentClicked(getItem(holder.adapterPosition).id, false)
            } else {
                Log.i("testTag", "liked adapter red3")
                likeCommentClicked.likeCommentClicked(getItem(holder.adapterPosition).id, true)
            }

        }

    }

    override fun changeColor(liked: Boolean) {

    }
}
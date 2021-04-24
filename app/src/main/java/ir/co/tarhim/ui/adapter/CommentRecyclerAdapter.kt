package ir.co.tarhim.ui.adapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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
import kotlinx.android.synthetic.main.row_left_forum.view.*
import kotlinx.android.synthetic.main.row_right_forum.view.*
import kotlinx.android.synthetic.main.row_right_forum.view.BtnMore
import kotlinx.android.synthetic.main.row_right_forum.view.TVCommentForum
import kotlinx.android.synthetic.main.row_right_forum.view.TVNameRightForum

class CommentRecyclerAdapter(private val context : Context, private var likeCommentClicked: LikeCommentClicked, var callBack:TipsListener) :
    ListAdapter<CommentDataModel, CommentRecyclerAdapter.ViewHolder>(CommentDiffUnit()) , LikedCommentChangeColor {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtComment: AppCompatTextView
        val nameUser: AppCompatTextView
        val imageUser: AppCompatImageView
        val likeIcon : AppCompatImageButton

        init {
            txtComment = view.TVCommentForum
            nameUser = view.TVNameRightForum
            imageUser = view.IVRightForum
            likeIcon = view.BtnLikeComment
        }

        open fun bindTo(comment: CommentDataModel) {

            txtComment.text = comment.message
            nameUser.text = comment.name
            Glide.with(itemView.context)
                .load(comment.imageurl)
                .circleCrop()
                .into(imageUser)

            if(comment.favourite){
                Log.i("testTag","liked adapter red")
                likeIcon.setImageResource(R.drawable.heart_full)
            }else{
                Log.i("testTag","liked adapter grey")
                likeIcon.setImageResource(R.drawable.heart_none)
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
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_right_forum, parent, false)
        return ViewHolder(view)


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
        holder.itemView.BtnMore.setOnClickListener {
            val popup = PopupMenu(context, holder.itemView.BtnMore)
            popup.menuInflater.inflate(R.menu.tool_tip_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.replayTool ->{
                  callBack.tipsCallback(getItem(holder.adapterPosition).id)
                }
            }
            true
        }
        }

        holder.likeIcon.setOnClickListener {
            if(!getItem(holder.adapterPosition).favourite){
                Log.i("testTag","liked adapter red2")
                likeCommentClicked.likeCommentClicked(getItem(holder.adapterPosition).id , false)
            }else{
                Log.i("testTag","liked adapter red3")
                likeCommentClicked.likeCommentClicked(getItem(holder.adapterPosition).id , true)
            }

        }

    }

    override fun changeColor(liked : Boolean) {

    }

}
package ir.co.tarhim.ui.adapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.CommentDataModel
import ir.co.tarhim.ui.LikeCommentClicked
import ir.co.tarhim.ui.callback.TipsListener
import ir.co.tarhim.ui.fragments.LikedCommentChangeColor

import ir.co.tarhim.ui.fragments.deceased.ReplayMessage

import kotlinx.android.synthetic.main.row_right_forum.view.*

class CommentRecyclerAdapter(private val context : Context, private var likeCommentClicked: LikeCommentClicked, var callBack:TipsListener) :
    ListAdapter<CommentDataModel, CommentRecyclerAdapter.ViewHolder>(CommentDiffUnit()) , LikedCommentChangeColor {
    var x : String = ""
    var selectedId : Int = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        val txtComment: AppCompatTextView
        val nameUser: AppCompatTextView
        val TVCountLike: AppCompatTextView
        val imageUser: AppCompatImageView

        val IVRightForum: AppCompatImageView
        val likeIcon : AppCompatImageButton
        val rightLayout : ConstraintLayout

        val adminImage: AppCompatImageView
        val replayLayout: ConstraintLayout
        val adminComment: AppCompatTextView
        val leftTVCommentForum: AppCompatTextView
        val TVNameLeftForum: AppCompatTextView

        init {
            txtComment = view.TVCommentForum
            nameUser = view.TVNameRightForum
            imageUser = view.IVRightForum
            TVCountLike = view.TVCountLike
            likeIcon = view.BtnLikeComment
            rightLayout = view.rightLayout
            IVRightForum = view.IVRightForum

            adminImage = view.IVLeftForum
            replayLayout = view.leftLayout
            adminComment = view.adminComment
            leftTVCommentForum = view.leftTVCommentForum
            TVNameLeftForum = view.TVNameLeftForum
        }

        open fun bindTo(comment: CommentDataModel) {


            if(comment.favourite){
                likeIcon.setImageResource(R.drawable.ic_do_favorite)
            }else{
                likeIcon.setImageResource(R.drawable.ic_non_favorite)

            }
            TVCountLike.setText("${comment.likes}")
            if(comment.reply!=null){
                txtComment.text = comment.reply
                rightLayout.visibility=View.GONE
                IVRightForum.visibility=View.GONE
                replayLayout.visibility=View.VISIBLE
                adminImage.visibility=View.VISIBLE
                Glide.with(itemView.context)
                    .load(comment.imageurl)
                    .circleCrop()
                    .into(adminImage)
                leftTVCommentForum.text=comment.reply
                adminComment.text=comment.message
                TVNameLeftForum.text=comment.name
            }else{
                nameUser.text = comment.name
                txtComment.text = comment.message
                Glide.with(itemView.context)
                    .load(comment.imageurl)
                    .circleCrop()
                    .into(imageUser)
                rightLayout.visibility=View.VISIBLE
                IVRightForum.visibility=View.VISIBLE
                replayLayout.visibility=View.GONE
                adminImage.visibility=View.GONE
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
        holder.bindTo(getItem(position))
        holder.itemView.BtnMore.setOnClickListener {
            callBack.tipsCallback(getItem(holder.adapterPosition).id)
        }

        holder.likeIcon.setOnClickListener {

//            if(holder.likeIcon.drawable.constantState ==
//                ContextCompat.getDrawable(context, R.drawable.ic_do_favorite)!!.constantState){
//                holder.likeIcon.setImageResource(R.drawable.ic_non_favorite)
//            }else{
//                holder.likeIcon.setImageResource(R.drawable.ic_do_favorite)
//            }

            if(!getItem(holder.adapterPosition).favourite){
                likeCommentClicked.likeCommentClicked(getItem(holder.adapterPosition).id , false)
            }else{
                likeCommentClicked.likeCommentClicked(getItem(holder.adapterPosition).id , true)

            }

        }

        if(x != "" && selectedId==getItem(holder.adapterPosition).id){
            holder.replayLayout.visibility=View.VISIBLE
            holder.adminImage.visibility=View.VISIBLE
            holder.adminComment.text=x
            holder.leftTVCommentForum.text=getItem(holder.adapterPosition).message
        }

    }

    override fun changeColor(liked: Boolean) {

    }


    fun setReplay(replay : String){
        x = replay
    }

    fun setId(id : Int){
        selectedId = id
    }

}
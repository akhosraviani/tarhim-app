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
import kotlinx.android.synthetic.main.row_right_forum.view.BtnMore
import kotlinx.android.synthetic.main.row_right_forum.view.TVCommentForum
import kotlinx.android.synthetic.main.row_right_forum.view.TVNameRightForum

class CommentRecyclerAdapter(private val context : Context, private var likeCommentClicked: LikeCommentClicked, var callBack:TipsListener) :
    ListAdapter<CommentDataModel, CommentRecyclerAdapter.ViewHolder>(CommentDiffUnit()) , LikedCommentChangeColor {
    var x : String = ""
    var selectedId : Int = 0
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        val txtComment: AppCompatTextView
        val nameUser: AppCompatTextView
        val imageUser: AppCompatImageView
        val likeIcon : AppCompatImageButton

        val adminImage: AppCompatImageView
        val replayLayout: ConstraintLayout
        val adminComment: AppCompatTextView

        init {
            txtComment = view.TVCommentForum
            nameUser = view.TVNameRightForum
            imageUser = view.IVRightForum
            likeIcon = view.BtnLikeComment

            adminImage = view.IVLeftForum
            replayLayout = view.leftLayout
            adminComment = view.adminComment
        }

        open fun bindTo(comment: CommentDataModel) {

            txtComment.text = comment.message
            nameUser.text = comment.name
            Glide.with(itemView.context)
                .load(comment.imageurl)
                .circleCrop()
                .into(imageUser)

            if(comment.favourite){
                likeIcon.setImageResource(R.drawable.heart_full)
            }else{
                likeIcon.setImageResource(R.drawable.heart_none)
            }

            if(comment.reply!=null){
                replayLayout.visibility=View.VISIBLE
                adminImage.visibility=View.VISIBLE
                adminComment.text=comment.reply.toString()
            }else{
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
            if(holder.likeIcon.drawable.constantState ==
                ContextCompat.getDrawable(context, R.drawable.heart_full)!!.constantState){
                holder.likeIcon.setImageResource(R.drawable.heart_none)
            }else{
                holder.likeIcon.setImageResource(R.drawable.heart_full)
            }

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
        }

    }

    override fun changeColor(liked : Boolean) {

    }

    fun setReplay(replay : String){
        x = replay
    }

    fun setId(id : Int){
        selectedId = id
    }


}
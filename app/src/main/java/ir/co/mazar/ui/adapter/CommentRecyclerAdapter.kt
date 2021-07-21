package ir.co.mazar.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.co.mazar.R
import ir.co.mazar.model.deceased.CommentDataModel
import ir.co.mazar.ui.LikeCommentClicked
import ir.co.mazar.ui.callback.TipsListener
import ir.co.mazar.ui.fragments.LikedCommentChangeColor
import kotlinx.android.synthetic.main.row_left_forum.view.*
import kotlinx.android.synthetic.main.row_right_forum.view.*

class CommentRecyclerAdapter(
    private val context: Context,
    private var likeCommentClicked: LikeCommentClicked,
    var tipsCallBack: TipsListener,
    var adminStatus: Boolean
) :
    ListAdapter<CommentDataModel, RecyclerView.ViewHolder>(CommentDiffUnit()),
    LikedCommentChangeColor {
    var x: String = ""
    var selectedId: Int = 0
    private val RIGHT_VIEW_TYPE = 1
    private val LEFT_VIEW_TYPE = 2

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtComment: AppCompatTextView
        val nameUser: AppCompatTextView
        val TVCountLike: AppCompatTextView
        val imageUser: AppCompatImageView

        val IVRightForum: AppCompatImageView
        val likeIcon: AppCompatImageButton
        val rightLayout: ConstraintLayout

        init {
            txtComment = view.TVCommentForum
            nameUser = view.TVNameRightForum
            imageUser = view.IVRightForum
            TVCountLike = view.TVCountLike
            likeIcon = view.BtnLikeComment
            rightLayout = view.rightLayout
            IVRightForum = view.IVRightForum

        }

        open fun bindTo(comment: CommentDataModel) {


            if (comment.favourite) {
                likeIcon.setImageResource(R.drawable.ic_do_favorite)
            } else {
                likeIcon.setImageResource(R.drawable.ic_non_favorite)

            }
            TVCountLike.text = "${comment.likes}"
            if (comment.reply == null) {
                nameUser.text = comment.name
                txtComment.text = comment.message

                val url = comment.imageurl
                url?.let {
                    if (url.startsWith("https")) {
                        Glide.with(itemView.context)
                            .load(url)
                            .circleCrop()
                            .into(imageUser)

                    } else {
                        Glide.with(itemView.context)
                            .load(url.replace("http", "https"))
                            .circleCrop()
                            .into(imageUser)
                    }
                }

                rightLayout.visibility = View.VISIBLE
                IVRightForum.visibility = View.VISIBLE
            } else {

                txtComment.text = comment.reply
                rightLayout.visibility = View.GONE
                IVRightForum.visibility = View.GONE
            }
        }
    }

    class ReplayViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val adminImage: AppCompatImageView
        val adminComment: AppCompatTextView
        val leftTVCommentForum: AppCompatTextView
        val TVNameLeftForum: AppCompatTextView

        init {
            adminImage = view.IvLeftForum
            adminComment = view.adminComment
            leftTVCommentForum = view.leftTVCommentForum
            TVNameLeftForum = view.TVNameLeftForum
        }

        open fun bindTo(comment: CommentDataModel) {

            val url = comment.imageurl
            if (url.startsWith("https")) {
                Glide.with(itemView.context)
                    .load(url)
                    .circleCrop()
                    .into(adminImage)
            } else {
                Glide.with(itemView.context)
                    .load(url.replace("http", "https"))
                    .circleCrop()
                    .into(adminImage)
            }

            leftTVCommentForum.text = comment.reply
            adminComment.text = comment.message
            TVNameLeftForum.text = comment.name

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view: View
        if (viewType == RIGHT_VIEW_TYPE) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_right_forum, parent, false)
            return CommentViewHolder(view)

        }
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_left_forum, parent, false)
        return ReplayViewHolder(view)


    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).reply != null) {
            LEFT_VIEW_TYPE
        } else {
            RIGHT_VIEW_TYPE
        }
    }

    override fun changeColor(liked: Boolean) {

    }

    fun setReplay(replay: String) {
        x = replay
    }

    fun setId(id: Int) {
        selectedId = id
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            LEFT_VIEW_TYPE -> {
                (holder as ReplayViewHolder).bindTo(getItem(position))
                if (x != "" && selectedId == getItem(holder.adapterPosition).id) {
                    holder.adminComment.text = x
                    holder.leftTVCommentForum.text = getItem(holder.adapterPosition).message
                }
            }
            else -> {
                (holder as CommentViewHolder).bindTo(getItem(position))

                var popup = PopupMenu(holder.itemView.context, holder.itemView)
                popup.inflate(R.menu.tool_tip_menu)
                adminStatus?.let {
                    if (!adminStatus) {
                        popup.menu.findItem(R.id.deleteTool).isVisible = false
                    }
                }
                holder.itemView.BtnMore.setOnClickListener {
                    popup.setOnMenuItemClickListener {

                        when (it.itemId) {
                            R.id.deleteTool -> {
                                tipsCallBack.deleteCallback(
                                    getItem(holder.adapterPosition).id,
                                    false
                                )
                            }
                            R.id.reportTool -> {
                                tipsCallBack.reportCallback(
                                    getItem(holder.adapterPosition).id,
                                    false
                                )
                            }
                            R.id.replayTool -> {
                                if (getItem(holder.adapterPosition).reply != null)
                                    tipsCallBack.replyCallback(
                                        getItem(holder.adapterPosition).id,
                                        true
                                    )
                                else
                                    tipsCallBack.replyCallback(
                                        getItem(holder.adapterPosition).id,
                                        false
                                    )

                            }
                            else -> false

                        }
                        false
                    }
                    popup.show()

                }
                holder.likeIcon.setOnClickListener {

                    if (!getItem(holder.adapterPosition).favourite) {
                        likeCommentClicked.likeCommentClicked(
                            getItem(holder.adapterPosition).id,
                            false
                        )
                    } else {
                        likeCommentClicked.likeCommentClicked(
                            getItem(holder.adapterPosition).id,
                            true
                        )

                    }

                }
            }

        }
    }
}
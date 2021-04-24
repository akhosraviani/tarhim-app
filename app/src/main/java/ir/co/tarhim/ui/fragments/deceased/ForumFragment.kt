package ir.co.tarhim.ui.fragments.deceased

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.ReportRequest
import ir.co.tarhim.model.deceased.SendCommentRequest
import ir.co.tarhim.model.deceased.like.LikeCommentRequest
import ir.co.tarhim.ui.LikeCommentClicked
import ir.co.tarhim.ui.adapter.CommentRecyclerAdapter
import ir.co.tarhim.ui.callback.TipsListener
import ir.co.tarhim.ui.viewModels.DeceasedViewModel
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.ReportEntityType
import ir.co.tarhim.utils.TarhimToast
import kotlinx.android.synthetic.main.fragment_forum.*
import kotlinx.android.synthetic.main.row_right_forum.*

class ForumFragment : Fragment(), TipsListener, LikeCommentClicked {

    private val ADMIN_STATUE = "adminStatus"
    private var adminStatus: Boolean? = false

    fun newInstance(id: Int, adminStatus: Boolean): ForumFragment {
        val fragment = ForumFragment()
        val args = Bundle()
        fragment.arguments = args
        args.putInt("Id", id)
        args.putBoolean(ADMIN_STATUE, adminStatus)
        return fragment
    }

    private var deceasedId: Int? = 0
    private lateinit var commentAdapter: CommentRecyclerAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var deceasedViewModel: DeceasedViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forum, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        deceasedViewModel = ViewModelProvider(this).get(DeceasedViewModel::class.java)
        deceasedId = requireArguments()!!.getInt("Id")
        adminStatus = requireArguments()!!.getBoolean(ADMIN_STATUE)
        initRecycler(adminStatus!!)
        viewModel.requestGetComment(deceasedId!!)

        viewModel.ldGetCommnet.observe(viewLifecycleOwner, Observer {
            it.let {
                commentAdapter.submitList(it)
            }
        })

        viewModel.ldSendCommnet.observe(viewLifecycleOwner, Observer {
            it.let {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })


        deceasedViewModel.ldLikeComment.observe(viewLifecycleOwner, Observer {
            it.let {
                Log.i("testTag", "liked fragment")
                Log.i("testTag", "liked fragment= " + it.toString())
                viewModel.requestGetComment(deceasedId!!)
                TarhimToast
                    .Builder()
                    .setActivity(requireActivity())

                    .message(it.message)
                    .build()
                commentAdapter.notifyDataSetChanged()
            }
        })

        BtnSendComment.setOnClickListener {
            if (ETComment.text.toString().isNotEmpty()) {
                viewModel.requestSendComment(
                    SendCommentRequest(
                        deceasedId!!,
                        ETComment.text.toString(),
                        (System.currentTimeMillis()).toInt()
                    )

                )

                ETComment.setText("")
            } else {

            }
        }


        viewModel.ldReport.observe(viewLifecycleOwner, Observer {
            it.also {
                when (it.code) {
                    200 -> TarhimToast.Builder()
                        .setActivity(requireActivity())
                        .message(it.message)
                        .build()
                    else -> TarhimToast.Builder()
                        .setActivity(requireActivity())
                        .message(it.message)
                        .build()
                }
            }
        })
    }


    private fun showPopupMenu(commentId: Int) {
        var popup = PopupMenu(requireActivity(), BtnMore)
        popup.menuInflater.inflate(R.menu.tool_tip_menu, popup.menu)
        if (!adminStatus!!) {
            popup.menu.findItem(R.id.deleteTool).setVisible(false)
            popup.menu.findItem(R.id.replayTool).setVisible(false)
        }
        popup.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.deleteTool -> {
                    TarhimToast.Builder()
                        .setActivity(requireActivity())
                        .message("در حال پیاده سازی")

                        .build()
                }
                R.id.reportTool -> {
                    viewModel.grequestReport(
                        ReportRequest(
                            true,
                            ReportEntityType.Comment.name,
                            commentId
                        )
                    )
                }
                R.id.replayTool -> {
                    TarhimToast.Builder()
                        .setActivity(requireActivity())
                        .message("در حال پیاده سازی")
                        .build()
                }

            }
            popup.show()
            true
        }
    }

    private fun initRecycler(status: Boolean) {
        commentAdapter = CommentRecyclerAdapter(this, this, status)
        ForumRecycler.adapter = commentAdapter
        ForumRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    private fun controllKeyboard() {

    }

    override fun tipsCallback(msgId: Int) {

        showPopupMenu(msgId)

    }

    override fun likeCommentClicked(id: Int, like: Boolean) {
        if (like) {
            //mikhaym toosi she
            deceasedViewModel.requestLikeComment(LikeCommentRequest(id, false))
        } else {
            //mikhaym ghermez she
            deceasedViewModel.requestLikeComment(LikeCommentRequest(id, true))
        }

    }

}
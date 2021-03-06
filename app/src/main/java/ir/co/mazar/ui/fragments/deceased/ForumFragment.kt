package ir.co.mazar.ui.fragments.deceased

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.mazar.R
import ir.co.mazar.model.deceased.DeleteCommentRequestModel
import ir.co.mazar.model.deceased.ReportRequest
import ir.co.mazar.model.deceased.SendCommentRequest
import ir.co.mazar.model.deceased.comment.ReplyCommentRequest
import ir.co.mazar.model.deceased.like.LikeCommentRequest
import ir.co.mazar.ui.LikeCommentClicked
import ir.co.mazar.ui.adapter.CommentRecyclerAdapter
import ir.co.mazar.ui.callback.TipsListener
import ir.co.mazar.ui.fragments.LikedCommentChangeColor
import ir.co.mazar.ui.viewModels.DeceasedViewModel
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.ReportEntityType
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.fragment_forum.*

class ForumFragment : Fragment(), TipsListener, LikeCommentClicked {


    companion object {
        private const val TAG = "ForumFragment"
    }

    private val ADMIN_STATUE = "adminStatus"
    private var adminStatus: Boolean? = false
    private var replayStatus: Boolean = false

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

    private lateinit var likedCommentChangeColor: LikedCommentChangeColor
    private lateinit var popState: PopUpState
    private var selectedCommentId: Int = 0
    private lateinit var selectedCommentResponse: String
    private lateinit var imm: InputMethodManager
    private var checkReplay: Boolean = false
    private var recyclerBody: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forum, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        deceasedViewModel = ViewModelProvider(this).get(DeceasedViewModel::class.java)
        deceasedId = requireArguments()!!.getInt("Id")
        adminStatus = requireArguments()!!.getBoolean(ADMIN_STATUE)
        initRecycler(adminStatus!!)
        viewModel.requestGetComment(deceasedId!!)


        Log.e(TAG, "onViewCreated :adminStatus " + adminStatus)
        viewModel.ldGetCommnet.observe(viewLifecycleOwner, Observer {
            it.let {
                commentAdapter.submitList(it)
            }
        })

        viewModel.ldSendCommnet.observe(viewLifecycleOwner, Observer {
            it.let {
                ETComment.setText("")
//                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                viewModel.requestGetComment(deceasedId!!)

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


        deceasedViewModel.ldReplayComment.observe(viewLifecycleOwner, Observer {
            it.let {
                if (it.code == 200) {
                    Log.i("testTag2","replay 200")
                    commentAdapter.setReplay(selectedCommentResponse)
                    commentAdapter.setId(selectedCommentId)
                    ForumRecycler.adapter = commentAdapter
                        viewModel.requestGetComment(deceasedId!!)
                    ETComment.setText("")
                    replayStatus = false
                }
            }
        })
        viewModel.ldDeleteComment.observe(viewLifecycleOwner, Observer {
            it.also {
                when (it.code) {
                    200 -> {
                        TarhimToast.Builder()
                            .setActivity(requireActivity())
                            .message(it.message)
                            .build()
                        viewModel.requestGetComment(deceasedId!!)

                    }
                    else -> TarhimToast.Builder()
                        .setActivity(requireActivity())
                        .message(it.message)
                        .build()
                }
            }
        })


        BtnSendComment.setOnClickListener {
            if (replayStatus) {
                Log.i("testTag2","replayStatus true")
                selectedCommentResponse = ETComment.text.toString()
                deceasedViewModel.requestReplyComment(
                    ReplyCommentRequest(
                        selectedCommentId,
                        deceasedId!!,
                        ETComment.text.toString()
                    )
                )
            } else {
                Log.i("testTag2","replayStatus false")
                if (ETComment.text.toString().isNotEmpty()) {
                    viewModel.requestSendComment(
                        SendCommentRequest(
                            deceasedId!!,
                            ETComment.text.toString(),
                            (System.currentTimeMillis()).toInt()
                        )
                    )
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

            viewModel.ldDeleteComment.observe(viewLifecycleOwner, Observer {
                it.also {
                    when (it.code) {
                        200 -> {
                            TarhimToast.Builder()
                                .setActivity(requireActivity())
                                .message(it.message)
                                .build()
                            viewModel.requestGetComment(deceasedId!!)

                        }
                        else -> TarhimToast.Builder()
                            .setActivity(requireActivity())
                            .message(it.message)
                            .build()
                    }
                }
            })

        }
    }



    private fun initRecycler(status: Boolean) {
        commentAdapter = CommentRecyclerAdapter(requireContext(), this, this,adminStatus!!)
        ForumRecycler.adapter = commentAdapter
        ForumRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
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

    override fun deleteCallback(msgId: Int, reply: Boolean) {
        viewModel.requestDeleteComment(
                                   DeleteCommentRequestModel(
                                       msgId,
                                       deceasedId!!
                                   )
                               )
    }

    override fun reportCallback(msgId: Int, reply: Boolean) {
        viewModel.grequestReport(
            ReportRequest(
                true,
                ReportEntityType.Comment.name,
                msgId
            )
        )
    }

    override fun replyCallback(msgId: Int, reply: Boolean) {
            imm.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
            ETComment.requestFocus()
            ETComment.setText("")
            if (commentAdapter.itemCount > 0) {
                ForumRecycler.layoutManager?.scrollToPosition(commentAdapter.itemCount - 1)
            }
            selectedCommentId = msgId
        replayStatus = true

    }

}



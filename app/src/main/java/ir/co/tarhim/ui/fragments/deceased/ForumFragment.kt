package ir.co.tarhim.ui.fragments.deceased

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.SendCommentRequest
import ir.co.tarhim.model.deceased.like.LikeCommentRequest
import ir.co.tarhim.ui.LikeCommentClicked
import ir.co.tarhim.ui.adapter.CommentRecyclerAdapter
import ir.co.tarhim.ui.callback.TipsListener
import ir.co.tarhim.ui.fragments.LikedCommentChangeColor
import ir.co.tarhim.ui.viewModels.DeceasedViewModel
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_forum.*


class ForumFragment : Fragment(), TipsListener , LikeCommentClicked {

    fun newInstance(id: Int): ForumFragment {
        val fragment = ForumFragment()
        val args = Bundle()
        fragment.arguments = args
        args.putInt("Id", id)
        return fragment
    }

    private var deceasedId: Int? = 0
    private lateinit var commentAdapter: CommentRecyclerAdapter
    private lateinit var viewModel: HomeViewModel
    private lateinit var deceasedViewModel: DeceasedViewModel
    private lateinit var likedCommentChangeColor: LikedCommentChangeColor
    private lateinit var popState : PopUpState
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
        initRecycler()
        deceasedId = requireArguments().getInt("Id")
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
                Log.i("testTag","liked fragment")
                Log.i("testTag","liked fragment= "+it.toString())
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


    }


    private fun initRecycler() {
        commentAdapter = CommentRecyclerAdapter(requireContext(),this,this)
        ForumRecycler.adapter = commentAdapter
        ForumRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    private fun controllKeyboard() {

    }

    private fun showSoftKeyboard(context: Context, view: View) {
        ETComment.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )

//            val imm =
//                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun tipsCallback(msgId: Int) {
        popState = PopUpState.REPLAY
        ETComment.requestFocus()
        showSoftKeyboard(requireContext(),ETComment)

//                  deceasedViewModel.requestReplyComment(ReplyCommentRequest(
//
//                  ))
//                      .requestSendComment(
//                      SendCommentRequest(
//                          deceasedId!!,
//                          ETComment.text.toString(),
//                          (System.currentTimeMillis()).toInt()
//                      )
//
//                  )

    }

    override fun likeCommentClicked(id: Int , like : Boolean) {
        if(like){
            //mikhaym toosi she
            deceasedViewModel.requestLikeComment(LikeCommentRequest(id,true))
        }else{
            //mikhaym ghermez she
            deceasedViewModel.requestLikeComment(LikeCommentRequest(id,false))
        }

    }

}
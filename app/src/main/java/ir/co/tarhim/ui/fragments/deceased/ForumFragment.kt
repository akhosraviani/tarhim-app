package ir.co.tarhim.ui.fragments.deceased

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.SendCommentRequest
import ir.co.tarhim.ui.adapter.CommentRecyclerAdapter
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_forum.*

class ForumFragment : Fragment() {

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
        initRecycler()
        deceasedId = requireArguments()!!.getInt("Id")
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


        BtnSendComment.setOnClickListener {
            if (ETComment.text.toString().length > 0) {
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
        commentAdapter = CommentRecyclerAdapter()
        ForumRecycler.adapter = commentAdapter
        ForumRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
    }


    private fun controllKeyboard(){

    }

}
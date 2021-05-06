package ir.co.mazar.ui.fragments.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.mazar.R
import ir.co.mazar.ui.activities.deceased.DeceasedProfileActivity
import ir.co.mazar.ui.callback.ProfileListener
import ir.co.mazar.ui.fragments.profile.adapter.FollowingRecyclerAdapter
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.my_deceased_fragment.*
import kotlinx.android.synthetic.main.tarhim_dialog.view.*

class MyFollowingFragment : Fragment(), ProfileListener.MyDeceasedListener,
    ProfileListener.UnFollowDeceasedListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var followingAdapter: FollowingRecyclerAdapter
    fun newInstance(layout: Boolean): MyFollowingFragment {
        val fragment = MyFollowingFragment()
        val args = Bundle()
        fragment.arguments = args
        args.putBoolean("Condition", layout)
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.my_deceased_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        showLoading(true)
        initFollowingRecycler()
        viewModel.requestFollowing()


        viewModel.ldFollowing.observe(viewLifecycleOwner, Observer {
            showLoading(false)
            it.also {
                if (it != null && it.size > 0) {
                    followingAdapter.submitList(it)
                } else {
                    followingAdapter.submitList(null)
                }
            }
        })

        viewModel.ldUnFollow.observe(viewLifecycleOwner, Observer {
            showLoading(false)
            it.also { x ->
                when (x.code) {
                    200 -> {
                        TarhimToast.Builder()
                            .setActivity(requireActivity())
                            .message(x.message).build()
                     alertDialog.dismiss()
                        viewModel.requestFollowing()


                    }
                    else -> {
                        TarhimToast.Builder()
                            .setActivity(requireActivity())
                            .message(x.message).build()

                    }
                }
            }
        })


    }


    fun showLoading(status: Boolean) {
        if (status) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            PrgFollowing.visibility = View.VISIBLE
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            PrgFollowing.visibility = View.GONE
        }
    }


    private fun initFollowingRecycler() {

        followingAdapter = FollowingRecyclerAdapter(this, this)
        mydeceasedRecycler.adapter = followingAdapter
        mydeceasedRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.up_to_bottom)
        mydeceasedRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mydeceasedRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.up_to_bottom)
        mydeceasedRecycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )

    }

    override fun myDeceasedCallBack(deceasedId: Int) {
        startActivity(
            Intent(requireActivity(), DeceasedProfileActivity::class.java)
                .putExtra("FromPersonal", deceasedId)
        )
    }

    override fun unFollowCallBack(deceasedId: Int) {

       showConfirmDialog(
            requireActivity(), R.drawable.request,
            "آیا مطمن هستید؟",
            {
                viewModel.requestUnFollowDeceased(deceasedId)
                showLoading(true)
            },
            {alertDialog.dismiss()
            }
        )

    }

    private lateinit var alertDialog:AlertDialog
     fun showConfirmDialog(
        activity: Activity,
        image: Int,
        message: String,
        accept: View.OnClickListener,
        cancel: View.OnClickListener
    ) {
        val viewGroup: ViewGroup = activity.findViewById(android.R.id.content)
        val view =
            LayoutInflater.from(activity).inflate(R.layout.tarhim_dialog, viewGroup, false)
        alertDialog = AlertDialog.Builder(activity).setView(view).create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.setCanceledOnTouchOutside(false)
        alertDialog!!.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view.TvMessageDialog.setText(message)

        view.BtnAcceptDialog.setOnClickListener(accept)
        view.BtnCloseDialog.setOnClickListener(cancel)

        view.IvImageDialog.setBackgroundResource(image)

        alertDialog.show()
    }
}

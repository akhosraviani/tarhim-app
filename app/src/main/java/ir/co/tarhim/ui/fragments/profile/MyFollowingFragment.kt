package ir.co.tarhim.ui.fragments.profile

import android.content.Intent
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
import ir.co.tarhim.R
import ir.co.tarhim.ui.callback.ProfileListener
import ir.co.tarhim.ui.activities.deceased.DeceasedProfileActivity
import ir.co.tarhim.ui.fragments.profile.adapter.FollowingRecyclerAdapter
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.TarhimToast
import kotlinx.android.synthetic.main.my_deceased_fragment.*

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
       startActivity( Intent(requireActivity(), DeceasedProfileActivity::class.java)
           .putExtra("FromPersonal", deceasedId))
    }

    override fun unFollowCallBack(deceasedId: Int) {
        viewModel.requestUnFollowDeceased(deceasedId)
        showLoading(true)
    }

}

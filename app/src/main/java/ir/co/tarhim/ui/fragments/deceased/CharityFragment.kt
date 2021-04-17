package ir.co.tarhim.ui.fragments.deceased

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.ui.callback.CharityListener
import ir.co.tarhim.ui.fragments.deceased.adapter.CharityRecyclerAdapter
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_charity.*
import kotlinx.android.synthetic.main.fragment_gallery.*

class CharityFragment : Fragment(), CharityListener {

    companion object {
        private const val TAG = "CharityFragment"
    }

    private lateinit var charityAdapter: CharityRecyclerAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_charity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        initCharityRecycler()

        viewModel.requestGetCharity()


        viewModel.ldCharity.observe(viewLifecycleOwner, Observer {

            it.also {
                charityAdapter.submitList(it)
            }
        })
    }


    private fun initCharityRecycler() {
        charityAdapter = CharityRecyclerAdapter(this)
        charitycenterRecycler.adapter = charityAdapter
        charitycenterRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.up_to_bottom)
        charitycenterRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2)
    }

    override fun chalityCallback(charityId: Int) {

    }


}
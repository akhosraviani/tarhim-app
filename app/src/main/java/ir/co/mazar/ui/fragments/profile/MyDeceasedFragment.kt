package ir.co.mazar.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.mazar.R
import ir.co.mazar.model.deceased.MyDeceasedDataModel
import ir.co.mazar.ui.adapter.MyDeceasedAdapter
import ir.co.mazar.ui.callback.ProfileListener
import ir.co.mazar.ui.activities.deceased.DeceasedProfileActivity
import ir.co.mazar.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.my_deceased_fragment.*

class MyDeceasedFragment : Fragment(), ProfileListener.MyDeceasedEditCallBack,
    ProfileListener.MyDeceasedListener {

    private lateinit var viewModel: HomeViewModel
    private lateinit var myDeceasedAdapter: MyDeceasedAdapter
    fun newInstance(layout: Boolean): MyDeceasedFragment {
        val fragment = MyDeceasedFragment()
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
        if (arguments?.getBoolean("Condition") == true) {
            viewModel.requestMydeceased()
        }

        viewModel.ldMyDeceased.observe(viewLifecycleOwner, Observer { data ->

            Log.e("TAG", "onViewCreated:data " + data.size)
            data!!.let {
                if (data.size != 0) {
                    initMyDeceasedRecycler(data)
                    myDeceasedAdapter.notifyDataSetChanged()
                } else {
                    TvNullMydeceased.text = "محتوایی برای نمایش وجود ندارد"
                }
            }
        })


    }


    private fun initMyDeceasedRecycler(listMyDeceased: List<MyDeceasedDataModel>) {
        myDeceasedAdapter = MyDeceasedAdapter(listMyDeceased, "MyDeceasedFragment", this, this)
        mydeceasedRecycler.adapter = myDeceasedAdapter
        mydeceasedRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mydeceasedRecycler.layoutAnimation= AnimationUtils.loadLayoutAnimation(requireContext(),R.anim.up_to_bottom)

        mydeceasedRecycler.addItemDecoration(
            DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL)
        )

    }

    override fun editDeceased(item: MyDeceasedDataModel) {

        startActivity(
            Intent(requireActivity(), DeceasedProfileActivity::class.java)
                .putExtra("EditDetails", item)
        )
    }

    override fun myDeceasedCallBack(deceasedId: Int) {
        startActivity(
            Intent(requireActivity(), DeceasedProfileActivity::class.java)
                .putExtra("FromPersonal", deceasedId)
        )

    }


}

package ir.co.tarhim.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.MydeceasedDataModel
import ir.co.tarhim.ui.adapter.MyDeceasedAdapter
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.my_deceased_fragment.*

class MyDeceasedFragment : Fragment() {

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
            run {
                if (data.size != 0) {
                    initMyDeceasedRecycler(data)
                    myDeceasedAdapter.notifyDataSetChanged()
                }
            }
        })


    }


    private fun initMyDeceasedRecycler(listMyDeceased: List<MydeceasedDataModel>) {
        myDeceasedAdapter = MyDeceasedAdapter(listMyDeceased,"MyDeceasedFragment")
        mydeceasedRecycler.adapter = myDeceasedAdapter
        mydeceasedRecycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }


}

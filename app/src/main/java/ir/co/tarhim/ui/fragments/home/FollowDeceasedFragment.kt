package ir.co.tarhim.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.MydeceasedDataModel
import ir.co.tarhim.ui.adapter.MyDeceasedAdapter
import ir.co.tarhim.ui.adapter.ViewPagerAdapter
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.my_deceased_fragment.*

class FollowDeceasedFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var myDeceasedAdapter: MyDeceasedAdapter
    fun newInstance(layout: Boolean): FollowDeceasedFragment {
        val fragment = FollowDeceasedFragment()
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



    }




}

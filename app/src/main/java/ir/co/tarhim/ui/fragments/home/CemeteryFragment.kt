package ir.co.tarhim.ui.fragments.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.DeceasedDataModel
import ir.co.tarhim.ui.adapter.DeceasedSearchRecyclerAdapter
import ir.co.tarhim.ui.adapter.LatestSearchRecyclerAdapter
import ir.co.tarhim.ui.callback.DeceasedRecyclerCallBack
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_cemetery.*
import kotlinx.android.synthetic.main.fragment_charity.*
import kotlinx.android.synthetic.main.latest_deceased_bottom_sheet.view.*

class CemeteryFragment : Fragment(), DeceasedRecyclerCallBack {


    companion object {
        private const val TAG = "CemeteryFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var searchAdapter: DeceasedSearchRecyclerAdapter
    private lateinit var latestAdapter: LatestSearchRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cemetery, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        initUi()
        viewModel.ldLatestSearch.observe(viewLifecycleOwner, Observer {
            showLoading(false)
            it.let {
                latestAdapter.submitList(it)
            }
        })



        SearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.e(TAG, "onQueryTextSubmit: ")
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query != null && TextUtils.getTrimmedLength(query) > 0) {
                    latestAdapter.submitList(null)
                    viewModel.requestSearch(query!!)
                    showLoading(true)
                    Log.e(TAG, "onQueryTextChange: ")
                }
                return false
            }
        })



        viewModel.ldSearch.observe(viewLifecycleOwner, { data ->
            showLoading(false)
            TvLatestSearch.visibility = View.GONE
            data.let {
                latestAdapter.submitList(data)
            }
            if (data == null)
                Toast.makeText(getActivity(), "محتوایی وجود ندار", Toast.LENGTH_SHORT).show()
        }
        )

        SearchView.setOnCloseListener {
            TvLatestSearch.visibility = View.VISIBLE
            viewModel.requestlatestSearch()
            false
        }

        BtnCreateDeceased.setOnClickListener({
            findNavController().navigate(R.id.action_fragment_cemetery_to_fragment_create_deceased)
        })

    }

    private fun initUi() {
        showLoading(true)
        initRecycler()
        viewModel.requestlatestSearch()
    }


    private fun initRecycler() {
        latestAdapter = LatestSearchRecyclerAdapter(this)
        var resanim = R.anim.up_to_bottom
        var animation = AnimationUtils.loadLayoutAnimation(requireContext(), resanim)
        Deceased_Recycler.adapter = latestAdapter
        Deceased_Recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        Deceased_Recycler.layoutAnimation = animation
    }

    private fun showLoading(visibility: Boolean) {
        if (visibility) {
            loadingProgress.visibility = View.VISIBLE
            Deceased_Recycler.visibility = View.GONE
        } else {
            loadingProgress.visibility = View.GONE
            Deceased_Recycler.visibility = View.VISIBLE
        }
    }

    override fun getId(decId: Int) {
        val args = bundleOf("DeceasedId" to decId)
        findNavController().navigate(R.id.action_fragment_cemetery_to_fragment_deceased_page, args)
    }

}
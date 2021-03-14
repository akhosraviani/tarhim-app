package ir.co.tarhim.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.solver.GoalRow
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.DeceasedDataModel
import ir.co.tarhim.model.deceased.DeceasedProfileDataModel
import ir.co.tarhim.ui.adapter.DeceasedSearchRecyclerAdapter
import ir.co.tarhim.ui.adapter.LatestSearchRecyclerAdapter
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_cemetery.*
import kotlinx.android.synthetic.main.latest_deceased_bottom_sheet.view.*

class CemeteryFragment : Fragment() {


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

//        showUi(true)
        viewModel.requestlatestSearch()
        SearchIcon.setOnClickListener({
            Log.e(TAG, "onViewCreated: " + searchbar.text.toString())
            viewModel.requestSearch(searchbar.text.toString())
        })


        viewModel.ldSearch.observe(viewLifecycleOwner, { data ->
            Log.e(TAG, "onCreateView: " + data.get(0).name)
            showUi(false)

            if (data.size != 0) {
                initRecycler(data)
            } else {
                Toast.makeText(getActivity(), "محتوایی وجود ندار", Toast.LENGTH_SHORT).show()
            }


        })
        viewModel.ldLatestSearch.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.e(TAG, "onViewCreated:  latest"+it.size )
                if (it.size != 0) {
//                    showUi(true)
                    initLatestRecycler(it)
                } else {
                    latestLayout.visibility = View.GONE
                }
            }
        })


        create_deceased.setOnClickListener({
            findNavController().navigate(R.id.action_fragment_cemetery_to_fragment_create_deceased)
        })

    }


    private fun initRecycler(listDeceased: List<DeceasedDataModel>) {
        searchAdapter = DeceasedSearchRecyclerAdapter(listDeceased)
        search_Deceased_Recycler.adapter = searchAdapter
        search_Deceased_Recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchAdapter.notifyDataSetChanged()
    }

    private fun initLatestRecycler(listDeceased: List<DeceasedDataModel>) {
        latestAdapter = LatestSearchRecyclerAdapter(listDeceased)
        latestLayout.latestRecycler.adapter = latestAdapter
        latestLayout.latestRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        latestAdapter.notifyDataSetChanged()
    }

    private fun showUi(visibil: Boolean) {
        if (visibil) {
            search_layout.visibility = View.VISIBLE
            search_Deceased_Recycler.visibility = View.GONE
        } else {
            search_layout.visibility = View.GONE
            search_Deceased_Recycler.visibility = View.VISIBLE
        }
    }

}
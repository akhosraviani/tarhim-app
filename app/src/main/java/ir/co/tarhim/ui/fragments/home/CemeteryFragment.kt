package ir.co.tarhim.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.DeceasedDataModel
import ir.co.tarhim.model.deceased.LatestDeceasedDataModel
import ir.co.tarhim.model.deceased.LatestDeceasedDataModelItem
import ir.co.tarhim.ui.adapter.DeceasedSearchRecyclerAdapter
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_cemetery.*

class CemeteryFragment : Fragment() {

    
    companion object{
        private const val TAG = "CemeteryFragment"
    }
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchAdapter: DeceasedSearchRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cemetery, container, false)


        viewModel.ldSearch.observe(viewLifecycleOwner, Observer { data ->
            {
                Log.e(TAG, "onCreateView: "+data.get(0).name )
                showUi(false)

                if (data.size != 0) {
                    initRecycler(data)
                }

            }
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        showUi(true)
        SearchIcon.setOnClickListener({
            Log.e(TAG, "onViewCreated: "+searchbar.text.toString() )
            viewModel.requestSearch(searchbar.text.toString())
        })

    }


    private fun initRecycler(listDeceased: List<DeceasedDataModel>) {
        searchAdapter = DeceasedSearchRecyclerAdapter(listDeceased)
        search_Deceased_Recycler.adapter = searchAdapter
        search_Deceased_Recycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchAdapter.notifyDataSetChanged()
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
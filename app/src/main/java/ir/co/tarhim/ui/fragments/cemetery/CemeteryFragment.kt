package ir.co.tarhim.ui.fragments.cemetery

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
import androidx.recyclerview.widget.RecyclerView
import ir.co.tarhim.R
import ir.co.tarhim.ui.adapter.LatestSearchRecyclerAdapter
import ir.co.tarhim.ui.callback.DeceasedRecyclerCallBack
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.ManageKeyboard
import kotlinx.android.synthetic.main.fragment_cemetery.*
import kotlinx.android.synthetic.main.fragment_cemetery.TvLatestSearch
import javax.crypto.SealedObject

class CemeteryFragment : Fragment(), DeceasedRecyclerCallBack {


    companion object {
        private const val TAG = "CemeteryFragment"
    }

    private lateinit var viewModel: HomeViewModel
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
        //<editor-fold desc="show EllipizeTitle">
        var titlesrc = "${getString(R.string.salavat)} ♦ ${getString(R.string.ill)}"
        TitleCemetery.text = titlesrc
        TitleCemetery.ellipsize = TextUtils.TruncateAt.MARQUEE
        TitleCemetery.setSingleLine(true)
        TitleCemetery.marqueeRepeatLimit = 1
        TitleCemetery.isSelected = true
        //</editor-fold>

        viewModel.ldLatestSearch.observe(viewLifecycleOwner, Observer {
//            Log.e(TAG, "onViewCreated: " + it.size)
            showLoading(false)
            it.let {
                latestAdapter.submitList(it)
            }
        })

        SearchLayout.setOnClickListener {
            initRecycler(DeceasedSearchRecycler)
            showView(false)
        }

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
                }else{
                    latestAdapter.submitList(null)
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
        initRecycler(LatestSearchRecycler)
        viewModel.requestlatestSearch()
    }


    private fun initRecycler(recyclerView: RecyclerView) {
        latestAdapter = LatestSearchRecyclerAdapter(this)
        var resanim = R.anim.up_to_bottom
        var animation = AnimationUtils.loadLayoutAnimation(requireContext(), resanim)
        recyclerView.adapter = latestAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutAnimation = animation
    }

    private fun showLoading(visibility: Boolean) {
        if (visibility) {
            loadingProgress.visibility = View.VISIBLE
            LatestSearchRecycler.visibility = View.GONE
        } else {
            loadingProgress.visibility = View.GONE
            LatestSearchRecycler.visibility = View.VISIBLE
        }
    }

    override fun getId(decId: Int) {
        val args = bundleOf("DeceasedId" to decId)
        findNavController().navigate(R.id.action_fragment_cemetery_to_fragment_deceased_page, args)
    }


    private fun showView(status: Boolean) {
        if (!status) {
            SearchView.animate().alpha(1f).duration = 600
            LatestSearchRecycler.animate().alpha(0f).duration = 600
            SearchLayout.animate().alpha(0f).duration = 600
            TvLatestSearch.animate().alpha(0f).duration = 600
            BtnCreateDeceased.animate().alpha(0f).duration = 600
            SearchView.visibility = View.VISIBLE
//            LatestSearchRecycler.visibility=View.GONE
//            SearchLayout.visibility=View.GONE
//            TvLatestSearch.visibility=View.GONE
//            BtnCreateDeceased.visibility=View.GONE

        }
    }


}
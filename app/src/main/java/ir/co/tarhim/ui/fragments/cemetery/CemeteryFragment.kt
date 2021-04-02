package ir.co.tarhim.ui.fragments.cemetery

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.ui.adapter.LatestSearchRecyclerAdapter
import ir.co.tarhim.ui.adapter.SearchRecyclerAdapter
import ir.co.tarhim.ui.callback.LatestRecyclerListener
import ir.co.tarhim.ui.callback.SearchListener
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.fragment_cemetery.*


class CemeteryFragment : Fragment(), LatestRecyclerListener, SearchListener {


    companion object {
        private const val TAG = "CemeteryFragment"
    }

    private lateinit var imm: InputMethodManager
    private lateinit var viewModel: HomeViewModel
    private lateinit var latestAdapter: LatestSearchRecyclerAdapter
    private lateinit var searchAdapter: SearchRecyclerAdapter
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
            showLoading(false)
            it.let {
                if (it.size > 0)
                    latestAdapter.submitList(it)
                else
                    TvNullLatest.text = "محتوایی برای نمایش وجود ندارد"
            }
        })

        SearchLayout.setOnClickListener {
            showView(false)
            imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            SearchView.requestFocus()
        }

        SearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {

                if (query != null && TextUtils.getTrimmedLength(query) > 0) {

                    viewModel.requestSearch(query!!)

                    Log.e(TAG, "onQueryTextChange:query " + query)

                    showLoading(true)
                }
                return false
            }
        })



        viewModel.ldSearch.observe(viewLifecycleOwner, { data ->
            showLoading(false)
            data.let {
                searchAdapter.submitList(data)
            }
            if (data == null)
                Toast.makeText(getActivity(), "موردی یافت نشد", Toast.LENGTH_SHORT).show()
        }
        )

        SearchView.setOnCloseListener {
            viewModel.requestlatestSearch()
            showView(true)
            false
        }

        BtnCreateDeceased.setOnClickListener({
            findNavController().navigate(R.id.action_fragment_cemetery_to_fragment_create_deceased)
        })

    }

    private fun initUi() {
        showLoading(true)
        initLatestRecycler()
        initSearchRecycler()
        viewModel.requestlatestSearch()
        imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    }


    private fun initLatestRecycler() {
        latestAdapter = LatestSearchRecyclerAdapter(this)
        var resanim = R.anim.up_to_bottom
        var animation = AnimationUtils.loadLayoutAnimation(requireContext(), resanim)
        LatestSearchRecycler.adapter = latestAdapter
        LatestSearchRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        LatestSearchRecycler.layoutAnimation = animation
        latestAdapter.submitList(ArrayList())

    }

    private fun initSearchRecycler() {
        searchAdapter = SearchRecyclerAdapter(this)
        var resanim = R.anim.up_to_bottom
        var animation = AnimationUtils.loadLayoutAnimation(requireContext(), resanim)
        DeceasedSearchRecycler.adapter = searchAdapter
        DeceasedSearchRecycler.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        DeceasedSearchRecycler.layoutAnimation = animation
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


    private fun showView(status: Boolean) {
        if (!status) {
            SearchRoot.visibility = View.VISIBLE
            SearchRoot.animate().alpha(1f).duration = 600
            latestRoot.animate().alpha(0f).duration = 600
//            LatestSearchRecycler.visibility=View.GONE
//            SearchLayout.visibility=View.GONE
//            TvLatestSearch.visibility=View.GONE
//            BtnCreateDeceased.visibility=View.GONE
        } else {
            imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
            SearchRoot.animate().alpha(0f).duration = 600
            latestRoot.animate().alpha(1f).duration = 600
        }

    }

    override fun latestCallBack(decId: Int) {
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        val args: Bundle

        args = bundleOf("LatestSearch" to decId)
        Log.e(TAG, "getId:latestSearch " + decId)

        findNavController().navigate(R.id.action_fragment_cemetery_to_fragment_deceased_page, args)
    }

    override fun serachClickCallBack(deceasedId: Int) {
        val args: Bundle
        args = bundleOf("GetFromSearch" to deceasedId)
        SearchView.setQuery("", false)
        SearchView.clearFocus()
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        findNavController().navigate(R.id.action_fragment_cemetery_to_fragment_deceased_page, args)
    }
}



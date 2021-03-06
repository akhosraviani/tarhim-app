package ir.co.mazar.ui.fragments.cemetery

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.hawk.Hawk
import ir.co.mazar.R
import ir.co.mazar.model.deceased.DeceasedDataModel
import ir.co.mazar.ui.adapter.LatestSearchRecyclerAdapter
import ir.co.mazar.ui.adapter.SearchRecyclerAdapter
import ir.co.mazar.ui.callback.DeleteLatestListener
import ir.co.mazar.ui.callback.LatestRecyclerListener
import ir.co.mazar.ui.callback.SearchListener
import ir.co.mazar.ui.activities.deceased.CreateDeceasedActivity
import ir.co.mazar.ui.activities.deceased.DeceasedProfileActivity
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.TarhimConfig.Companion.FIRST_VISIT
import ir.co.mazar.utils.TarhimToast
import kotlinx.android.synthetic.main.fragment_cemetery.*


class CemeteryFragment : Fragment(), LatestRecyclerListener, SearchListener, DeleteLatestListener {


    companion object {
        private const val TAG = "CemeteryFragment"
    }

    private lateinit var imm: InputMethodManager
    private lateinit var viewModel: HomeViewModel
    private lateinit var latestAdapter: LatestSearchRecyclerAdapter
    private lateinit var searchAdapter: SearchRecyclerAdapter
    private var deceasedList: List<DeceasedDataModel> = ArrayList()

    private lateinit var queryStr: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cemetery, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        Hawk.put(FIRST_VISIT, true)
        initUi()


        //<editor-fold desc="show EllipizeTitle">
        var titlesrc = "${getString(R.string.salavat)} ??? ${getString(R.string.ill)}"
        TitleCemetery.text = titlesrc
        TitleCemetery.ellipsize = TextUtils.TruncateAt.MARQUEE
        TitleCemetery.setSingleLine(true)
        TitleCemetery.marqueeRepeatLimit = 1
        TitleCemetery.isSelected = true

        //</editor-fold>
//        keyBoardStatus()

        viewModel.ldLatestSearch.observe(viewLifecycleOwner, Observer {
            showLoading(false)
            it.let {
                Log.i("testTag3", "hi =$it")
                if (it!=null && it.isNotEmpty()) {
                    latestAdapter.submitList(it)
                }else{
                    latestAdapter.submitList(null)
                    TvLatestSearch.visibility=View.GONE
                }
            }
        })

        SearchLayout.setOnClickListener {
            showView(false)
            imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            SearchView.requestFocus()
        }


        SearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (query != null && TextUtils.getTrimmedLength(query) > 0) {
                    closeSearchBtn.animate().alpha(1f).duration = 600
                    closeSearchBtn.visibility = View.VISIBLE
                    viewModel.requestSearch(query!!.toString())
                    queryStr = query!!.toString()

                    Log.e(TAG, "onQueryTextChange:query " + query)

                    showLoading(true)
                }
            }

            override fun afterTextChanged(query: Editable?) {

            }
        })


        viewModel.ldSearch.observe(viewLifecycleOwner, { data ->
            showLoading(false)
            data.let {
                searchAdapter.submitList(it)
            }
        }
        )

        viewModel.ldDeleteLatest.observe(viewLifecycleOwner, Observer {
            it.also {x->
                when(x.code){
                    200->{
                        viewModel.requestlatestSearch()

                        TarhimToast.Builder()
                            .setActivity(requireActivity())
                            .message(x.message)
                            .build()
                    }
                    else->{
                        TarhimToast.Builder()
                            .setActivity(requireActivity())
                            .message(x.message)
                            .build()
                    }
                }
            }
        })

        closeSearchBtn.setOnClickListener {
            imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
            SearchView.clearFocus()
            SearchView.setText("")
            closeSearchBtn.visibility = View.GONE
            viewModel.requestlatestSearch()
            showView(true)


        }
        BtnCreateDeceased.setOnClickListener({
            startActivity(Intent(requireActivity(), CreateDeceasedActivity::class.java))

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
        latestAdapter = LatestSearchRecyclerAdapter(this,this)
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
            SearchLayout.visibility = View.VISIBLE
            latestRoot.animate().alpha(0f).duration = 600
            latestRoot.visibility = View.GONE


        } else {
            imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
            SearchRoot.animate().alpha(0f).duration = 600
            SearchRoot.visibility = View.GONE
            latestRoot.animate().alpha(1f).duration = 600
            latestRoot.visibility = View.VISIBLE

        }

    }

    override fun latestCallBack(decId: Int) {
        Log.i("testTag7","cemetery latest call id= "+decId.toString())
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        SearchView.setText("")
        SearchView.clearFocus()
        startActivity(
            Intent(requireActivity(), DeceasedProfileActivity::class.java)
                .putExtra("FromPersonal", decId)
        )
    }

    override fun serachClickCallBack(deceasedId: Int) {
        Log.i("testTag7","search cemetery id= "+deceasedId.toString())
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        SearchView.setText("")
        SearchView.clearFocus()
        startActivity(
            Intent(requireActivity(), DeceasedProfileActivity::class.java)
                .putExtra("SearchPersonal", deceasedId)
        )

    }


    private fun keyBoardStatus() {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        var contentView = requireActivity().findViewById<View>(android.R.id.content)
        contentView.getViewTreeObserver()
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {

                override fun onGlobalLayout() {
                    val r = Rect()
                    contentView.getWindowVisibleDisplayFrame(r)
                    val screenHeight: Int = contentView.getRootView().getHeight()
                    val keypadHeight: Int = screenHeight - r.bottom
                    if (keypadHeight > screenHeight * 0.15) {
                        if (searchAdapter.itemCount > 0)
                            DeceasedSearchRecycler.scrollToPosition(searchAdapter.itemCount - 1)
                    } else {
                        return
                    }

                }
            })

    }

    override fun deleteCallback(recordedId: Int) {
        viewModel.requestDeleteLatest(recordedId)
        showLoading(true)
    }


}



package ir.co.tarhim.ui.fragments.deceased

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ir.co.tarhim.R
import ir.co.tarhim.ui.adapter.GalleryRecyclerAdapter
import ir.co.tarhim.ui.callback.GalleryListener
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.DialogProvider
import kotlinx.android.synthetic.main.fragment_gallery.*

class GalleryFragment : Fragment(), GalleryListener {

    companion object {
        private const val TAG = "GalleryFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private var deceasedId: Int = -1
    private lateinit var pathlist: List<String>
    private lateinit var galleryAdapter: GalleryRecyclerAdapter
    fun newInstance(id: Int): GalleryFragment {
        val fragment = GalleryFragment()
        val args = Bundle()
        fragment.arguments = args
        args.putInt("Id", id)
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        pathlist = ArrayList()
//        pathlist.toMutableList().add(R.drawable.shape_upload_img_gallery.toString())

        deceasedId = arguments?.getInt("Id")!!
        viewModel.requestGetGallery(deceasedId)

        viewModel.ldGetGallery.observe(viewLifecycleOwner, Observer {

            it!!.let {
                pathlist.toMutableList().addAll(it.imagespath!!)
                initRecycler(it.imagespath)
                Log.e(TAG, "onViewCreated: " + it.imagespath.size)
            }

        })
    }


    private fun initRecycler(paths: List<String>) {
        galleryAdapter = GalleryRecyclerAdapter(paths, this)
        galleryrecycler.adapter = galleryAdapter
        galleryrecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(context, R.anim.up_to_bottom)
        galleryrecycler.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        galleryAdapter.notifyDataSetChanged()
    }

    override fun galleryRecyclerCallBack(rowId: String) {
        DialogProvider().showImageDialog(requireActivity(), rowId)
    }
}

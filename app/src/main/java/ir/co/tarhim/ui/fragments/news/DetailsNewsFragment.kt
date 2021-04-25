package ir.co.tarhim.ui.fragments.news

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import ir.co.tarhim.R
import ir.co.tarhim.model.news.NewsDataModel
import ir.co.tarhim.utils.BaseBottomSheetDialog
import kotlinx.android.synthetic.main.details_news.*


class DetailsNewsFragment : BaseBottomSheetDialog() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            var bottomSheet =
                (it as BottomSheetDialog).findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            var behavior=BottomSheetBehavior.from(bottomSheet)
            behavior.isDraggable=false
            val layoutParams = bottomSheet.layoutParams
            val displayMetrics = DisplayMetrics()
            (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
            var widthHeight=displayMetrics.heightPixels
            if(layoutParams!=null){
                layoutParams.height=widthHeight
            }
            bottomSheet.layoutParams=layoutParams
            behavior.state=BottomSheetBehavior.STATE_EXPANDED

        }
        return dialog
    }


    private lateinit var newItem: NewsDataModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.details_news, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (requireArguments().getParcelable<NewsDataModel>("DetailsNews") != null) {
            newItem = arguments?.getParcelable("DetailsNews")!!
            initUi(newItem)
        }

    }

    fun initUi(new: NewsDataModel) {

        TvTitleOfNews.text = new.topic
        Glide.with(requireContext())
            .load(new.imageurl)
            .into(IvDetailsNews)
        TvTopicDetailsNews.text = new.text


        BtnBackDetailsNews.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}
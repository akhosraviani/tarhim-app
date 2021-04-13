package ir.co.tarhim.ui.fragments.contact

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.co.tarhim.R
import ir.co.tarhim.utils.BaseBottomSheetDialog
import kotlinx.android.synthetic.main.contact_fragment.*
import kotlin.system.measureNanoTime

class ContactPage() : BaseBottomSheetDialog() {

    private lateinit var bottomSheet: FrameLayout
    private lateinit var manager: LinearLayoutManager


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            bottomSheet =
                (it as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.isDraggable = false

        }


        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.contact_fragment, container, false)

    }


    private fun initContactRecycler() {
        manager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        contactRecycler.layoutManager = manager

    }

}
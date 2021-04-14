package ir.co.tarhim.ui.fragments.add_firends

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import ir.co.tarhim.R
import ir.co.tarhim.utils.BaseBottomSheetDialog
import kotlinx.android.synthetic.main.contact_fragment.*

class AddFriendsDialogFragment() : BaseBottomSheetDialog() {

    private lateinit var bottomSheet: FrameLayout
    private lateinit var manager: LinearLayoutManager
    private var popupState=false


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


    private fun showAddFriendLayout(){

        AddFriendsTv.setOnClickListener {
            if(!popupState){

            }
        }
    }

}
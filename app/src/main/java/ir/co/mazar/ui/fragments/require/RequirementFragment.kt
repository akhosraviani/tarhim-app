package ir.co.mazar.ui.fragments.require

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.co.mazar.R
import ir.co.mazar.model.deceased.PrayDataRequest
import ir.co.mazar.ui.callback.SpinnerListener
import ir.co.mazar.ui.fragments.require.adapter.RequirementRecyclerAdapter
import ir.co.mazar.ui.viewModels.HomeViewModel
import ir.co.mazar.utils.SpinnerTarhim
import ir.co.mazar.utils.TarhimToast
import ir.co.mazar.utils.TypePray
import kotlinx.android.synthetic.main.dialog_requirement_pray.view.*
import kotlinx.android.synthetic.main.fragment_requirement.*

class RequirementFragment : Fragment(), SpinnerListener {

    companion object {
        private const val TAG = "RequirementFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var dialog: AlertDialog
    private lateinit var prayType: String
    private lateinit var requirementAdapter: RequirementRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_requirement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        showLoading(true)

        initRequirementRecycler()
        viewModel.requestGetPray()
//        OnBackPressed().pressedCallBack(findNavController())
        viewModel.ldPray.observe(viewLifecycleOwner, Observer {
            showLoading(false)
            it.also {
                requirementAdapter.submitList(it)
            }
        })
        if (arguments != null) {
            if (requireArguments().getBoolean("PrayLink")) {
                showRequirementPray(requireActivity())
            }
        }


        viewModel.ldSendPray.observe(viewLifecycleOwner, Observer {
            it.also { x ->
                showLoading(true)
                when (x.code) {
                    200 -> {
                        TarhimToast.Builder()
                            .setActivity(requireActivity())
                            .message(x.message)
                            .build()
                        dialog.dismiss()
                        viewModel.requestGetPray()
                    }
                }
            }
        })



        BtnAddREquirementPray.setOnClickListener {
            showRequirementPray(requireActivity())
        }


    }


    private fun initRequirementRecycler() {
        requirementAdapter = RequirementRecyclerAdapter()
        requirementRecycler.adapter = requirementAdapter
        requirementRecycler.layoutAnimation =
            AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.up_to_bottom)
        requirementRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }


    private fun showLoading(status: Boolean) {
        if (status) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            prgRequirePage.visibility = View.VISIBLE
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            prgRequirePage.visibility = View.GONE
        }
    }

    override fun onNothing() {
        prayType = TypePray.Salavat.name
    }

    override fun onSelected(adapter: AdapterView<*>, position: Int) {
        when (position) {
            0 -> {
                prayType = TypePray.Salavat.name
            }
            1 -> {
                prayType = TypePray.Quran.name
            }
            2 -> {
                prayType = TypePray.Money.name
            }
        }

    }

    public fun showRequirementPray(activity: Activity) {
        var view: ViewGroup = activity.findViewById(android.R.id.content)
        var root =
            LayoutInflater.from(activity).inflate(R.layout.dialog_requirement_pray, view, false)

        dialog = AlertDialog.Builder(activity).setView(root).create()
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val listPrayType: Array<String> = resources.getStringArray(R.array.list_pray_type)
        SpinnerTarhim().initSpinner(
            this,
            requireActivity(),
            R.layout.row_spinner,
            root.SpinnerTypePray,
            listPrayType
        )



        root.BtnSubmitPray.setOnClickListener {
            if (root.EtSubjectPray.text!!.length > 0
             && root.EtMessagePray.text!!.length > 0
            ) {

                viewModel.requestSendPray(
                    PrayDataRequest(
                        root.EtMessagePray.text.toString(),
                        root.EtSubjectPray.text.toString(),
                        prayType
                    )
                )
                showLoading(true)

            } else {
                TarhimToast.Builder()
                    .setActivity(requireActivity())
                    .message("???????? ???????? ???????? ???? ???? ???? ?????????? ?????????? ????????.")
            }
        }
    }


}
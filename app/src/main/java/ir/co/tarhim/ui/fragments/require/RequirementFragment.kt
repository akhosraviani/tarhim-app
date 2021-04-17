package ir.co.tarhim.ui.fragments.require

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ir.co.tarhim.R
import ir.co.tarhim.ui.viewModels.HomeViewModel
import ir.co.tarhim.utils.OnBackPressed
import kotlinx.android.synthetic.main.fragment_requirement.*

class RequirementFragment : Fragment() {


    companion object {
        private const val TAG = "RequirementFragment"
    }

    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_requirement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.requestGetPray()
        OnBackPressed().pressedCallBack(findNavController())

        TvNullRequirement.text = "در حال پیاده سازی"
    }

    private fun showLoading(status: Boolean) {
        if (status) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            prgRequirePage.visibility=View.VISIBLE
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            prgRequirePage.visibility=View.GONE
        }
    }
}
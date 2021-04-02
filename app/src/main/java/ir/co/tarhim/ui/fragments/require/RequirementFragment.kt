package ir.co.tarhim.ui.fragments.require

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ir.co.tarhim.R
import ir.co.tarhim.utils.OnBackPressed
import kotlinx.android.synthetic.main.fragment_requirement.*

class RequirementFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_requirement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        OnBackPressed().pressedCallBack(findNavController())

        TvNullRequirement.text="در حال پیاده سازی"
    }
}
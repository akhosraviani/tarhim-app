package ir.co.tarhim.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import ir.co.tarhim.R
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.page_of_deceased.*

class DeceasedPageFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private var deceasedId: Int = -1


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.page_of_deceased, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        if (arguments != null) {
            deceasedId = arguments?.getInt("DeceasedId")!!
            viewModel.requestDeceasedProfile(deceasedId)
        }

        viewModel.ldDeceasedProfile.observe(viewLifecycleOwner, Observer {
            TvDeseacesName.text = it.name
            TvDeathDateDeseaces.text = "${it.birthday}"
            TvBornDateDeseaces.text = "${it.deathday}"
            TvBioDeseaces.text = it.description


            Glide.with(requireActivity())
                .load(it.imageurl)
                .circleCrop()
                .into(ImVDeceased)

        })

    }

}
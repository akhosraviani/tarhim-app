package ir.co.tarhim.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ir.co.tarhim.R
import ir.co.tarhim.model.deceased.CreateDeceasedRequest
import ir.co.tarhim.ui.viewModels.HomeViewModel
import kotlinx.android.synthetic.main.create_deceased.*

class CreateDeceased : Fragment() {

    companion object {
        private const val TAG = "CreateDeceased"
    }

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.create_deceased, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)




        btnSaveDeceased.setOnClickListener {
            val birthDay = ETBirthDateDeceased.text.toString().toInt()
            val deathDay = ETBirthDateDeceased.text.toString().toInt()
            viewModel.requestCreateDeceased(
                CreateDeceasedRequest(
                    "Public",
                    birthDay,
                    deathDay,
                    ETBurialLocation.text.toString(),
                    ETdeceasedDescription.text.toString(),
                    "",
                    ETNameDeceased.text.toString()
                )
            )
        }


        viewModel.ldcreateDeceased.observe(viewLifecycleOwner, Observer {
            if (it.code == 200) {
                Toast.makeText(activity, "با موفقیت ثبت شد", Toast.LENGTH_SHORT).show()
            }
        })
    }

}

package ir.co.tarhim.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ir.co.tarhim.R
import ir.co.tarhim.network.RequestClient
import ir.co.tarhim.ui.viewModels.HomeViewModel

class Home : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        viewModel.requestSignUp("09198995587")


        viewModel.ldSignUp.observe(this,{

        })
    }

}
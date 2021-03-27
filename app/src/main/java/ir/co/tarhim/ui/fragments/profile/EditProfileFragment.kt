package ir.co.tarhim.ui.fragments.profile

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ir.co.tarhim.R
import ir.co.tarhim.ui.viewModels.HomeViewModel
import java.util.jar.Manifest

class EditProfileFragment : Fragment() {

    companion object {
        private const val TAG = "EditProfileFragment"
    }

    private lateinit var viewModel: HomeViewModel
    private val GALLERY_CODE = 101
    private lateinit var filePath:String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }


    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            var galleryIntent=Intent(Intent.ACTION_PICK)
            galleryIntent.setType("images/*")
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
            requireActivity().startActivity(galleryIntent)

        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                GALLERY_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            GALLERY_CODE->{
                if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    var galleryIntent=Intent(Intent.ACTION_PICK)
                    galleryIntent.setType("images/*")
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
                    requireActivity().startActivity(galleryIntent)
                }else{
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),GALLERY_CODE)
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var dataUri=data?.data

    }



    private fun getRealFilePath(contentUi:Uri):String{

        return filePath
    }

}
package ir.co.mazar.ui.fragments.deceased

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.carto.styles.AnimationStyleBuilder
import com.carto.styles.AnimationType
import com.carto.styles.MarkerStyleBuilder
import com.orhanobut.hawk.Hawk
import ir.co.mazar.databinding.FragmentMapsBinding
import ir.co.mazar.model.deceased.DeceasedProfileDataModel
import kotlinx.android.synthetic.main.fragment_maps.*
import org.neshan.common.model.LatLng
import org.neshan.mapsdk.MapView
import org.neshan.mapsdk.model.Marker


class   MapsFragment : Fragment(),
    MapView.OnMapClickListener {

    private var checkLocaion = false

    companion object {
        private const val TAG = "MapsFragment"
    }

    private lateinit var binding: FragmentMapsBinding

    private var lastedLocation: LatLng? = null
    private var cemeteryLocate: LatLng? = null
    private var info: DeceasedProfileDataModel? = null
    private lateinit var marker: Marker
    fun newInstance(locaion: LatLng): MapsFragment {
        val args = Bundle()
        val fragment = MapsFragment()
        fragment.arguments = args
        args.putDoubleArray("LOCATION", doubleArrayOf(locaion.latitude, locaion.longitude))
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMapsBinding.inflate(inflater, container, false)
        .also {
            binding = it
        }.root

    public fun initLayoutReference() {

        map.setOnMapClickListener(this)
        if (checkLocaion) {
            map.moveCamera(LatLng(35.5413334, 51.3784391), 0.25f)
            binding.map.setZoom(15f, 0f)
            marker = addMarker(LatLng(35.5413334, 51.3784391))

        } else {
            map.moveCamera(LatLng(cemeteryLocate!!.latitude, cemeteryLocate!!.longitude), 0.25f)
            binding.map.setZoom(15f, 0f)
            marker = addMarker(LatLng(cemeteryLocate!!.latitude, cemeteryLocate!!.longitude))
            map.addMarker(marker);

        }
    }

    private fun addMarker(lnglt: LatLng): Marker {
        val animStBl = AnimationStyleBuilder()
        animStBl.fadeAnimationType = AnimationType.ANIMATION_TYPE_SMOOTHSTEP
        animStBl.sizeAnimationType = AnimationType.ANIMATION_TYPE_SPRING
        animStBl.phaseInDuration = 0.5f
        animStBl.phaseOutDuration = 0.5f
        var animSt = animStBl.buildStyle()

        val markStCr = MarkerStyleBuilder()
        markStCr.size = 30f
//        markStCr.bitmap = BitmapUtils.createBitmapFromAndroidBitmap(
//            BitmapFactory.decodeResource(
//                resources,R.drawable.icba)
//        )

        markStCr.animationStyle = animSt
        val markSt = markStCr.buildStyle()


        return Marker(lnglt, markSt)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Hawk.get("locationBurial",null) != null) {
            cemeteryLocate =Hawk.get("locationBurial",null)

        } else {
            checkLocaion = true
        }

        initLayoutReference()

        BtnSubmitLocation.setOnClickListener {
            detectLocationListenr.locationCallback(lastedLocation!!)
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this).commit();
        }
    }

    private lateinit var detectLocationListenr: DetectLocationListenr
    override fun onAttach(context: Context) {
        super.onAttach(context)
        detectLocationListenr = context as DetectLocationListenr
    }

    interface DetectLocationListenr {
        fun locationCallback(location: LatLng)
    }


    override fun onMapClick(p0: LatLng?) {
        map.removeMarker(marker)
        Log.e(TAG, "onMapClick: " + p0!!.latitude)
        marker = addMarker(p0!!)
        map.addMarker(marker);
        lastedLocation = p0!!
        activity?.runOnUiThread(Runnable {
            // Stuff that updates the UI
            BtnSubmitLocation.visibility = View.VISIBLE
        })
    }


}




package ir.co.mazar.ui.fragments.deceased

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import ir.co.mazar.R
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : Fragment(),
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMarkerClickListener {




    companion object {
        private const val TAG = "MapsFragment"
    }

    private lateinit var mGoogleMap: GoogleMap
    private var lastedLocation: LatLng? = null
    private var cemeteryLocate: DoubleArray? = null

    private val callback = OnMapReadyCallback { googleMap ->
        mGoogleMap = googleMap

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    cemeteryLocate!![0],
                    cemeteryLocate!![1]
                ), 15f
            )
        )
        ImMarkerMap.visibility = View.VISIBLE
        mGoogleMap.setOnCameraMoveListener(this)
        mGoogleMap.setOnCameraIdleListener(this)
        mGoogleMap.setOnMarkerClickListener(this)

    }


    fun newInstance(latLong: LatLng): MapsFragment {
        val fragment = MapsFragment()
        val args = Bundle()
        fragment.arguments = args
        args.putDoubleArray("LOCATION", doubleArrayOf(latLong.latitude, latLong.longitude))
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


        if (arguments != null) {
            cemeteryLocate = requireArguments().getDoubleArray("LOCATION")

            Log.e(TAG, "onViewCreated: " + cemeteryLocate!!.get(0))
            Log.e(TAG, "onViewCreated: " + cemeteryLocate!!.get(1))
        } else {
            cemeteryLocate = doubleArrayOf(35.53, 51.37)
            Log.e(TAG, "onViewCreated: Locate " + cemeteryLocate!!.get(0))
            Log.e(TAG, "onViewCreated: Locate " + cemeteryLocate!!.get(1))
        }
        BtnSubmitLocation.setOnClickListener {

            detectLocationListenr.locationCallback(lastedLocation!!)
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this).commit();
        }


    }

    override fun onCameraMove() {
        mGoogleMap.clear()
        ImMarkerMap.visibility = View.VISIBLE
    }
    override fun onCameraIdle() {
        ImMarkerMap.visibility = View.GONE
        mGoogleMap.addMarker(
            MarkerOptions()
                .draggable(false).position(mGoogleMap.cameraPosition.target).icon(
                    BitmapDescriptorFactory
                        .fromResource(R.drawable.orijin_black)
                )
        )


    }
    override fun onMarkerClick(marker: Marker?): Boolean {
        lastedLocation = marker!!.position
        Log.e("locationBurial", "onMarkerClick: " + lastedLocation)
        BtnSubmitLocation.visibility = View.VISIBLE
        return false
    }

    private lateinit var detectLocationListenr: DetectLocationListenr
    override fun onAttach(context: Context) {
        super.onAttach(context)
        detectLocationListenr=context as DetectLocationListenr
    }

    interface DetectLocationListenr {
        fun locationCallback(location: LatLng)
    }
}




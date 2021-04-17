package ir.co.tarhim.ui.fragments.deceased

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
import ir.co.tarhim.R
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : Fragment(),
    GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraIdleListener,
    GoogleMap.OnMarkerClickListener {

    private lateinit var locationListener: LocateListenr

    fun setLocation(location: LocateListenr) {
        locationListener = location
    }

    companion object {
        private const val TAG = "MapsFragment"
    }

    private lateinit var mGoogleMap: GoogleMap
    private var lastedLocation: LatLng? = null

    private val callback = OnMapReadyCallback { googleMap ->
        mGoogleMap = googleMap
        val cemeteryLocate = LatLng(35.536270, 51.370183)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cemeteryLocate, 15f))
        ImMarkerMap.visibility = View.VISIBLE
        mGoogleMap.setOnCameraMoveListener(this)
        mGoogleMap.setOnCameraIdleListener(this)
        mGoogleMap.setOnMarkerClickListener(this)

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


        BtnSubmitLocation.setOnClickListener {
            locationListener.locationCallback(lastedLocation!!)
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
                        .fromResource(R.drawable.marker)
                )
        )


    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        lastedLocation = marker!!.position
        Log.e(TAG, "onMarkerClick: " + lastedLocation)
        BtnSubmitLocation.visibility = View.VISIBLE
        return false
    }


    interface LocateListenr {
        fun locationCallback(location: LatLng)
    }
}
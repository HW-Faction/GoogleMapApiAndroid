package map.api.assignment

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMapView: MapView

    private lateinit var googleMap: GoogleMap
    private lateinit var mapBoundry: LatLngBounds
    private lateinit var geoPosition: GeoPosition

    private val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mMapView = findViewById(R.id.map_view)

        geoPosition = GeoPosition(intent.getDoubleExtra("lat", 0.00), intent.getDoubleExtra("long", 0.00))
        Log.d("__trace", "onCreate@Map -> lat: " + geoPosition.lat)
        Log.d("__trace", "onCreate@Map -> lat: " + geoPosition.long)

        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    override fun onMapReady(map: GoogleMap) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) { return }
        map.isMyLocationEnabled = true
        googleMap = map
        setCameraView()
    }

    private fun setCameraView() {
        // Set a boundary to start
        val bottomBoundary: Double = geoPosition.lat - .001
        val leftBoundary: Double = geoPosition.long - .001
        val topBoundary: Double = geoPosition.lat + .001
        val rightBoundary: Double = geoPosition.long + .001

        mapBoundry = LatLngBounds(
            LatLng(bottomBoundary, leftBoundary),
            LatLng(topBoundary, rightBoundary)
        )

        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels

        Log.d("__trace", "setCameraView -> mapBoundry: $mapBoundry")
        // Log.d("__trace", "setCameraView -> lat: " + geoPosition.long)

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mapBoundry, width, height, 0))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        mMapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mMapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }

    override fun onStart() {
        super.onStart()
        mMapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMapView.onStop()
    }
}

data class GeoPosition(val lat: Double, val long: Double)

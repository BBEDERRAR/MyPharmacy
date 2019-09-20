package esi.tdm.mypharmacy.fragments


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import esi.tdm.mypharmacy.R
import esi.tdm.mypharmacy.adapter.PharmacyAdapter
import esi.tdm.mypharmacy.entity.Pharmacy
import esi.tdm.mypharmacy.models.MyLocation
import esi.tdm.mypharmacy.retrofit.RetrofitService
import esi.tdm.mypharmacy.roomDatabase.RoomService
import kotlinx.android.synthetic.main.fragment_pharmacies_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val PERMISSION_REQUEST = 10


class NearbyPharmaciesFragment : Fragment(), OnMapReadyCallback {
    private var mMap: MapView? = null
    protected var mLastLocation: Location? = null
    var listPharmaciesResponse: List<Pharmacy>? = null;

    lateinit var locationManager: LocationManager
    private var hasGps = false
    private var hasNetwork = false
    private var locationGps: Location? = null
    private var locationNetwork: Location? = null


    private var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    var myLocation = MyLocation(0.0, 0.0)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_nearby_pharmacies, container, false)




        mMap = view?.findViewById(R.id.mapView) as MapView
        mMap?.onCreate(savedInstanceState)
        mMap?.getMapAsync(this)


        listPharmaciesResponse = RoomService.appDataBase.getPharmacyDao().getPharmacies()
        Log.i("listPharmaciesFromDB ", listPharmaciesResponse.toString())
        if (listPharmaciesResponse!!.isEmpty()) {
            var call = RetrofitService.PharmacyEndpoint.getPharmacies();
            call.enqueue(object : Callback<List<Pharmacy>> {
                override fun onFailure(call: Call<List<Pharmacy>>, t: Throwable) {
                    Log.e("GetPharmacies", "GetPharmacies Not Working")
                }

                override fun onResponse(call: Call<List<Pharmacy>>, response: Response<List<Pharmacy>>) {
                    if (response.isSuccessful) {
                        listPharmaciesResponse = response.body()
                        Log.i("listPharmaciesFromAPI ", listPharmaciesResponse.toString())
                        RoomService.appDataBase.getPharmacyDao().addPharmacies(listPharmaciesResponse)
                    }
                }
            })
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMISSION_REQUEST)
            Log.d("something", "${getLocation()}")
        }



        return view;
    }

    override fun onResume() {
        super.onResume()
        mMap?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMap?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mMap?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mMap?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap?.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap) {

        var currentLoction = Location("");
        currentLoction.latitude = myLocation.lat.toDouble();
        currentLoction.longitude = myLocation.lng.toDouble();
        listPharmaciesResponse!!.forEach {

            var loctionMarker = Location("");
            loctionMarker.latitude = it!!.latitude.toDouble();
            loctionMarker.longitude = it!!.longitude.toDouble();

            var distanceInMeters = currentLoction.distanceTo(loctionMarker);

            Log.e("distanceInKilometres", (distanceInMeters / 1000).toString())

            googleMap.addMarker(
                MarkerOptions().position(
                    LatLng(
                        it!!.latitude.toDouble(),
                        it!!.longitude.toDouble()
                    )
                ).title(it!!.id.toString())
            )

        }
        googleMap.setOnMarkerClickListener {
            if (it.title != "My location") {
                val bundle = Bundle()
                bundle.putString("id", it.title)
                view?.findNavController()
                    ?.navigate(R.id.action_nearbyPharmaciesFragment_to_pharmacyDetailsFragment, bundle)
                true
            }
            false
        }

        val myLocationNow = LatLng(myLocation.lat, myLocation.lng)
        googleMap.addMarker(
            MarkerOptions().position(myLocationNow).title("My location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        )
        val zoomLevel = 16.0f

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                myLocationNow, zoomLevel
            )
        );
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.toFloat()), 2000, null);

    }


    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGps || hasNetwork) {

            if (hasGps) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationGps = location

                                myLocation.lat = locationGps!!.latitude
                                myLocation.lng = locationGps!!.longitude
                            }
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {

                        }

                        override fun onProviderEnabled(provider: String?) {
                        }

                        override fun onProviderDisabled(provider: String?) {
                        }
                    })

                val localGpsLocation =
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (localGpsLocation != null)
                    locationGps = localGpsLocation
            }
            if (hasNetwork) {
                Log.d("CodeAndroidLocation", "hasGps")
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    5000,
                    0F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                locationNetwork = location

                                myLocation.lat = locationNetwork!!.latitude
                                myLocation.lng = locationNetwork!!.longitude

                            }
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {

                        }

                        override fun onProviderEnabled(provider: String?) {

                        }

                        override fun onProviderDisabled(provider: String?) {

                        }

                    })

                val localNetworkLocation =
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (localNetworkLocation != null)
                    locationNetwork = localNetworkLocation
            }

            if (locationGps != null && locationNetwork != null) {
                if (locationGps!!.accuracy > locationNetwork!!.accuracy) {

                    myLocation.lat = locationNetwork!!.latitude
                    myLocation.lng = locationNetwork!!.longitude

                } else {

                    myLocation.lat = locationGps!!.latitude
                    myLocation.lng = locationGps!!.longitude

                }
            }

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            var allSuccess = true
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    allSuccess = false
                    val requestAgain =
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(
                            permissions[i]
                        )
                    if (requestAgain) {
                        Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            activity,
                            "Go to settings and enable the permission",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }

}

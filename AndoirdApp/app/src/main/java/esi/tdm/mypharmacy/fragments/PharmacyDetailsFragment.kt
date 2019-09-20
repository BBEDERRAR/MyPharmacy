package esi.tdm.mypharmacy.fragments


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import esi.tdm.mypharmacy.R
import esi.tdm.mypharmacy.entity.Pharmacy
import esi.tdm.mypharmacy.retrofit.RetrofitService
import esi.tdm.mypharmacy.roomDatabase.RoomService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import esi.tdm.mypharmacy.config.PREFS_NAME
import kotlinx.android.synthetic.main.fragment_pharmacy_details.view.*


class PharmacyDetailsFragment : Fragment(), OnMapReadyCallback {
    private var mMap: MapView? = null
    var pharmacy: Pharmacy? = null;

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState!!)

        mMap?.onSaveInstanceState(outState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_pharmacy_details, container, false)

        val name = view.findViewById<TextView>(R.id.name);
        val phone = view.findViewById<TextView>(R.id.phone);
        val checkIn = view.findViewById<TextView>(R.id.checkIn);
        val checkOut = view.findViewById<TextView>(R.id.checkOut);
        val facebook = view.findViewById<TextView>(R.id.facebook);
        val address = view.findViewById<TextView>(R.id.address);






        mMap = view?.findViewById(R.id.mapView) as MapView
        mMap?.onCreate(savedInstanceState)
        mMap?.getMapAsync(this)


        val pharmacyId = arguments!!.get("id").toString();
        Log.i("pharmacyId", pharmacyId)
        pharmacy = RoomService.appDataBase.getPharmacyDao().getPharmacyById(pharmacyId.toInt())
        Log.i("pharmacy ", pharmacy.toString())


        val orderButton = view.findViewById<Button>(R.id.order);
        val sharedPref: SharedPreferences = context!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userLogged = sharedPref.contains("phone");
        if (userLogged) {
            orderButton.text = "Create New Order"
        } else {
            orderButton.text = "Login To Create Order"
        }

        orderButton.setOnClickListener {
            if (userLogged) {
                val bundle = Bundle()
                bundle.putString("idPharmacy", pharmacyId)
                view.findNavController().navigate(R.id.action_pharmacyDetailsFragment_to_createOrderFragement, bundle)
            } else {
                view.findNavController().navigate(R.id.action_pharmacyDetailsFragment_to_loginFragment)
            }
        }


        if (pharmacy == null) {
            var call = RetrofitService.PharmacyEndpoint.getPharmacy(pharmacyId);
            call.enqueue(object : Callback<Pharmacy> {
                override fun onFailure(call: Call<Pharmacy>, t: Throwable) {
                    Log.e("getPharmacy", "getPharmacy Not Working")
                }

                override fun onResponse(call: Call<Pharmacy>, response: Response<Pharmacy>) {
                    if (response.isSuccessful) {
                        pharmacy = response.body()

                        name.text = pharmacy?.name.toString();
                        phone.text = pharmacy?.phone.toString();
                        checkIn.text = pharmacy?.check_in.toString();
                        checkOut.text = pharmacy?.check_out.toString();
                        address.text = pharmacy?.address.toString();


                        Log.i("PharmacyFromAPI", pharmacy.toString())
                        RoomService.appDataBase.getPharmacyDao().addPharmacy(pharmacy)
                    }
                }
            })
        } else {
            name.text = pharmacy?.name.toString();
            phone.text = pharmacy?.phone.toString();
            checkIn.text = pharmacy?.check_in.toString();
            checkOut.text = pharmacy?.check_out.toString();
            address.text = pharmacy?.address.toString();
            facebook.text= pharmacy?.facebook_page.toString();
        }


        return view
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
        googleMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    pharmacy!!.latitude.toDouble(),
                    pharmacy!!.longitude.toDouble()
                )
            ).title(pharmacy!!.name)
        )

        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    pharmacy!!.latitude.toDouble(),
                    pharmacy!!.longitude.toDouble()
                ), 10.toFloat()
            )
        );
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.toFloat()), 2000, null);

    }
}

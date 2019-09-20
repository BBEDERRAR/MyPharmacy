package esi.tdm.mypharmacy.fragments


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController

import esi.tdm.mypharmacy.R
import esi.tdm.mypharmacy.config.PREFS_NAME
import esi.tdm.mypharmacy.entity.Order
import esi.tdm.mypharmacy.entity.Pharmacy
import esi.tdm.mypharmacy.entity.User
import esi.tdm.mypharmacy.models.LoginResponse
import esi.tdm.mypharmacy.retrofit.RetrofitService
import esi.tdm.mypharmacy.roomDatabase.RoomService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateOrderFragement : Fragment() {
    private val REQUEST_GALLERY_CODE = 200
    private val READ_REQUEST_CODE = 300
    private val SERVER_PATH = "Path_to_your_server"
    var pharmacy: Pharmacy? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_order_fragement, container, false)

        // pharmacy details
        val name = view.findViewById<TextView>(R.id.name);
        val phone = view.findViewById<TextView>(R.id.phone);
        val checkIn = view.findViewById<TextView>(R.id.checkIn);
        val checkOut = view.findViewById<TextView>(R.id.checkOut);
        val facebook = view.findViewById<TextView>(R.id.facebook);
        val address = view.findViewById<TextView>(R.id.address);



        val pharmacyId = arguments!!.get("idPharmacy").toString();
        Log.i("pharmacyId", pharmacyId)
        pharmacy = RoomService.appDataBase.getPharmacyDao().getPharmacyById(pharmacyId.toInt())
        Log.i("pharmacy ", pharmacy.toString())

        name.text = pharmacy?.name.toString();
        phone.text = pharmacy?.phone.toString();
        checkIn.text = pharmacy?.check_in.toString();
        checkOut.text = pharmacy?.check_out.toString();
        facebook.text = pharmacy?.facebook_page.toString();
        address.text = pharmacy?.address.toString();

        val chooseImageButton = view.findViewById<Button>(R.id.chooseImage);
        val sendButton = view.findViewById<Button>(R.id.sendOrder);

        chooseImageButton.setOnClickListener {
            val openGalleryIntent = Intent(Intent.ACTION_PICK)
            openGalleryIntent.type = "image/*"
            startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE)
        }

        val sharedPref: SharedPreferences = context!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userLogged= sharedPref.contains("phone");
        if (userLogged) {
            val userId= sharedPref.getInt("id",-1).toString();
            sendButton.setOnClickListener{

                var call = RetrofitService.OrderEndpoint.createOrder(
                    userId, pharmacyId
                )
                call.enqueue(object : Callback<Order> {
                    override fun onFailure(call: Call<Order>, t: Throwable) {
                        Log.e("createOrder", "failed")
                    }

                    override fun onResponse(call: Call<Order>, response: Response<Order>) {
                        if (response.isSuccessful) {
                            val result = response.body()
                            Log.i("createOrder", "successfully")
                            view.findNavController().navigate(R.id.action_createOrderFragement_to_orderListFragment)


                        } else {

                        }
                    }
                })
            }

        }




        return view;
    }


}

package esi.tdm.mypharmacy.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import esi.tdm.mypharmacy.R
import esi.tdm.mypharmacy.entity.Pharmacy
import esi.tdm.mypharmacy.retrofit.RetrofitService
import esi.tdm.mypharmacy.roomDatabase.RoomService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val PREFS_NAME = "LOGIN_CREDENTIALS"
    var listPharmaciesResponse: List<Pharmacy>? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nav = findNavController(R.id.fragment)
        NavigationUI.setupWithNavController(bottomNavigation, nav)
        val sharedPref: SharedPreferences = applicationContext!!.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val userLogged = sharedPref.contains("phone");
        if (userLogged) {
//            bottomNavigation.menu.removeItem(R.id.loginFragment);
            bottomNavigation.menu.findItem(R.id.loginFragment).setVisible(false)
        } else {
//            bottomNavigation.menu.removeItem(R.id.orderListFragment);
            bottomNavigation.menu.findItem(R.id.orderListFragment).setVisible(false)

        }

        listPharmaciesResponse = RoomService.appDataBase.getPharmacyDao().getPharmacies()
        Log.i("listPharmaciesFromDB ", listPharmaciesResponse.toString())
        // check if pharmacies exist in localDB
        if (listPharmaciesResponse!!.isEmpty()) {
            var call = RetrofitService.PharmacyEndpoint.getPharmacies();
            call.enqueue(object : Callback<List<Pharmacy>> {
                override fun onFailure(call: Call<List<Pharmacy>>, t: Throwable) {
                    Log.e("GetPharmaciesByCity", "GetPharmaciesByCity Not Working")
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

    }

}

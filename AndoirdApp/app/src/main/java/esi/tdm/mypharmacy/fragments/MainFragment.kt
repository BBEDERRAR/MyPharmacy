package esi.tdm.mypharmacy.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import esi.tdm.mypharmacy.R
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.findNavController
import esi.tdm.mypharmacy.entity.City
import esi.tdm.mypharmacy.retrofit.RetrofitService
import esi.tdm.mypharmacy.roomDatabase.RoomService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainFragment : Fragment() {
    var cityListResponse:List<City>?=null;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_main, container, false);
        //Sample String ArrayList
        val spinner = view.findViewById(R.id.spinner_cities_list) as Spinner

        cityListResponse=RoomService.appDataBase.getCityDao().getCities()
        Log.e("cityListResponse",cityListResponse.toString())
        if (cityListResponse!!.isEmpty()){
            var call = RetrofitService.CityEndpoint.getCities()
            call.enqueue(object : Callback<List<City>> {
                override fun onFailure(call: Call<List<City>>, t: Throwable) {
                    Log.e("GetCities", "GetCities Not Working")
                }

                override fun onResponse(call: Call<List<City>>, response: Response<List<City>>) {
                    if (response.isSuccessful) {
                        cityListResponse = response.body()
                        RoomService.appDataBase.getCityDao().addCities(cityListResponse)
                        val citiesList = ArrayList<String>()
                        cityListResponse?.forEach {
                            citiesList.add(it.name)
                        }

                        val adp = ArrayAdapter<String>(
                            this@MainFragment.context,
                            android.R.layout.simple_spinner_dropdown_item,
                            citiesList
                        )
                        spinner.adapter = adp

                    } else {
                        // no cities
                    }
                }
            })
        }else{
            val citiesList = ArrayList<String>()
            cityListResponse?.forEach {
                citiesList.add(it.name)
            }

            val adp = ArrayAdapter<String>(
                this@MainFragment.context,
                android.R.layout.simple_spinner_dropdown_item,
                citiesList
            )
            spinner.adapter = adp

        }





        val searchButton = view.findViewById<Button>(R.id.search_button);

        searchButton.setOnClickListener {
            val selectedCity = spinner.selectedItem.toString()

            var bundle = Bundle()


            val city: City? = cityListResponse?.find { it.name == selectedCity }

            bundle.putString("cityId", city?.id.toString())
            view.findNavController().navigate(R.id.action_mainFragment_to_pharmaciesListFragment, bundle)

        }


        return view;
    }


}

package esi.tdm.mypharmacy.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import androidx.navigation.findNavController

import esi.tdm.mypharmacy.R
import esi.tdm.mypharmacy.adapter.PharmacyAdapter
import esi.tdm.mypharmacy.entity.Pharmacy
import esi.tdm.mypharmacy.retrofit.RetrofitService
import esi.tdm.mypharmacy.roomDatabase.RoomService
import kotlinx.android.synthetic.main.fragment_pharmacies_list.*
import org.jetbrains.anko.bundleOf
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PharmaciesListFragment : Fragment() {
    var listPharmaciesResponse: List<Pharmacy>? = null;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_pharmacies_list, container, false)
        var pharmaciesList = view.findViewById(R.id.pharmaciesList) as ListView
        var progressBar = view.findViewById(R.id.progressBar1) as ProgressBar



        if (getArguments() != null) {
            val cityId = getArguments()!!.getString("cityId");

            listPharmaciesResponse = RoomService.appDataBase.getPharmacyDao().getPharmacyByCityId(cityId.toInt())
            Log.i("listPharmaciesFromDB ", listPharmaciesResponse.toString())

            if (listPharmaciesResponse!!.isEmpty()) {

                var call = RetrofitService.CityEndpoint.getPharmaciesByCity(cityId);
                call.enqueue(object : Callback<List<Pharmacy>> {
                    override fun onFailure(call: Call<List<Pharmacy>>, t: Throwable) {
                        Log.e("GetPharmaciesByCity", "GetPharmaciesByCity Not Working")
                    }

                    override fun onResponse(call: Call<List<Pharmacy>>, response: Response<List<Pharmacy>>) {
                        if (response.isSuccessful) {
                            progressBar.visibility = View.INVISIBLE
                            listPharmaciesResponse = response.body()
                            Log.i("listPharmaciesFromAPI ", listPharmaciesResponse.toString())
                            RoomService.appDataBase.getPharmacyDao().addPharmacies(listPharmaciesResponse)
                            pharmaciesList.adapter =
                                PharmacyAdapter(this@PharmaciesListFragment.context!!, listPharmaciesResponse!!)
                            pharmaciesList.setOnItemClickListener { adapterView, view, i, l ->
                                val pharmacy = (adapterView.getItemAtPosition(i) as Pharmacy)
                                val bundle = Bundle()
                                bundle.putString("id", pharmacy.id.toString())
                                view.findNavController().navigate(R.id.action_pharmaciesListFragment_to_pharmacyDetailsFragment, bundle)
                            }
                        }
                    }
                })
            } else {
                Log.i("listPharmacies ", listPharmaciesResponse.toString())
                progressBar.visibility = View.INVISIBLE

                pharmaciesList.adapter =
                    PharmacyAdapter(this@PharmaciesListFragment.context!!, listPharmaciesResponse!!)

                pharmaciesList.setOnItemClickListener { adapterView, view, i, l ->
                    val pharmacy = (adapterView.getItemAtPosition(i) as Pharmacy)
                    var bundle = bundleOf("id" to pharmacy.id)
                    view.findNavController()
                        .navigate(R.id.action_pharmaciesListFragment_to_pharmacyDetailsFragment, bundle)
                }
            }

        }

        return view
    }


}

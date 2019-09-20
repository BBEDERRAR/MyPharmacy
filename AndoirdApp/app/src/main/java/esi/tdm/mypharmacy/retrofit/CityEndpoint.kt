package esi.tdm.mypharmacy.retrofit

import esi.tdm.mypharmacy.entity.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CityEndpoint{


    @GET("city")
    fun getCities(): Call<List<City>>


    @GET("city/{id}")
    fun getCity(@Path("id") id: String): Call<City>


    @GET("city/{id}/pharmacies")
    fun getPharmaciesByCity(@Path("id") id: String): Call<List<Pharmacy>>

}
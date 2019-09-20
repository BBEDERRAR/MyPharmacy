package esi.tdm.mypharmacy.retrofit

import esi.tdm.mypharmacy.entity.Pharmacy
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PharmacyEndpoint {

    @GET("pharmacy/{id}")
    fun getPharmacy(@Path("id") id: String): Call<Pharmacy>

    @GET("pharmacy")
    fun getPharmacies(): Call<List<Pharmacy>>

}
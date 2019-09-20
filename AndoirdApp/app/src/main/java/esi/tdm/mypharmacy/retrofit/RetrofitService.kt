package esi.tdm.mypharmacy.retrofit

import esi.tdm.mypharmacy.config.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



object RetrofitService {

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    val PharmacyEndpoint = retrofit.create(PharmacyEndpoint::class.java)
    val CityEndpoint = retrofit.create(CityEndpoint::class.java)
    val UserEndpoint = retrofit.create(UserEndpoint::class.java)
    val OrderEndpoint= retrofit.create(OrderEndpoint::class.java)


}
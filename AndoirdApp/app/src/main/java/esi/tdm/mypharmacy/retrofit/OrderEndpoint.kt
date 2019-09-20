package esi.tdm.mypharmacy.retrofit

import esi.tdm.mypharmacy.entity.Order
import esi.tdm.mypharmacy.entity.User
import retrofit2.Call
import retrofit2.http.*

interface OrderEndpoint {

    @GET("order")
    fun getOrders(): Call<List<Order>>

    @GET("order/{id}")
    fun getOrder(@Path("id") id: String): Call<Order>

    @GET("order/{user_id}/my_orders")
    fun getOrderByUserId(@Path("user_id") userId: String): Call<List<Order>>

    @FormUrlEncoded
    @POST("order")
    fun createOrder(
        @Field("user_id") user_id: String,
        @Field("pharmacy_id") pharmacy_id: String
    ): Call<Order>

}
package esi.tdm.mypharmacy.retrofit

import esi.tdm.mypharmacy.entity.User
import esi.tdm.mypharmacy.models.LoginResponse
import retrofit2.Call
import retrofit2.http.*

interface UserEndpoint {
    @FormUrlEncoded
    @POST("user/sign_up")
    fun signUp(
        @Field("firstName") first_name: String,
        @Field("lastName") last_name: String,
        @Field("phone") phone: String,
        @Field("nss") nss: String
    ): Call<User>

    @FormUrlEncoded
    @POST("user/authenticate")
    fun login(
        @Field("phone") phone: String,
        @Field("password") nss: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("user/change_password")
    fun changePassword(
        @Field("phone") phone: String?,
        @Field("old_password") old_password: String,
        @Field("new_password") new_password: String
    ): Call<LoginResponse>


}
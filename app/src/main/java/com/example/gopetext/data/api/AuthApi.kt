package com.example.gopetext.data.api
/**
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

//ESTE ES PARA PRUEBAS CON POSTMAN, NO SE USA CUANDO CRISTIAN HAGA LA API EN SYMFONY
interface AuthApi {
    @Headers(
        "x-api-key: TPMAK-685ca2af9a8d6b000199ed65-72b01c0dd1a225c2f24ed68ab388420885",
        "Content-Type: application/json"
        ,"x-mock-match-request-body: true"
    )
    @POST("api/login")
    fun login(@Body data: Map<String, String>): Call<Map<String, String>>
}**/

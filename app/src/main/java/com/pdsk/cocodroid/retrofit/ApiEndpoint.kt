package com.pdsk.cocodroid.retrofit

import com.pdsk.cocodroid.models.ProdukModel
import com.pdsk.cocodroid.models.TokoModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoint {
    @GET("toko")
    fun getData(): Call<TokoModel>

    @GET("toko?")
    fun searchData(@Query("search") sQuery:String): Call<TokoModel>

    @GET("produk?")
    fun getProdukByIdToko(@Query("idtoko") idToko:Int): Call<ProdukModel>
}
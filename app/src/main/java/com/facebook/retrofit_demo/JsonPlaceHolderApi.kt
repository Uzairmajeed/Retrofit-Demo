package com.facebook.retrofit_demo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface JsonPlaceHolderApi {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @POST("posts")
    fun postdata(@Body post:Post):Call<Post>
}
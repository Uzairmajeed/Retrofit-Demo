package com.facebook.retrofit_demo

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface JsonPlaceHolderApi {
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    @POST("posts")
    fun postdata(@Body post:Post):Call<Post>

    /*@Multipart
    @POST("posts")
    fun postdata(
        @Part parts: List<MultipartBody.Part>
    ): Call<Post>*/

    @PUT("posts/{id}")
    fun putPost(@Path("id") id: Int, @Body post: Post): Call<Post>
}
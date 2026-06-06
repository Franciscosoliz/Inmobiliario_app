package com.ute.app.data.api

import com.ute.app.data.model.PostDTO
import retrofit2.http.*

interface s {
    @GET("posts")
    suspend fun getPosts(): List<PostDTO>

    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Int): PostDTO

    @POST("posts")
    suspend fun createPost(@Body post: PostDTO): PostDTO
}
package com.lazyhound.hnmobile.rest

import com.lazyhound.hnmobile.models.NewsList
import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("search_by_date?query=mobile")
    fun fetchNews(): Call<NewsList>
}
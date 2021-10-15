package com.lazyhound.hnmobile

import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("search_by_date?query=mobile")
    fun fetchNews(): Call<NewsList>
}
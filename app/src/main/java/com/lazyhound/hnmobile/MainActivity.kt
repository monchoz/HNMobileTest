package com.lazyhound.hnmobile

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private var news: MutableList<News> = ArrayList()

    private val lastVisibleItemPosition: Int
        get() = linearLayoutManager.findLastVisibleItemPosition()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayoutManager = LinearLayoutManager(this)
        hnRecyclerView.layoutManager = linearLayoutManager

        var apiService: APIService = RestClient.client.create(APIService::class.java);
        val call = apiService.fetchNews();
        call.enqueue(object : Callback<NewsList> {

            override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
                val body = response.body()
                if (body != null) {
                    news.addAll(body.hits!!)
                    adapter = RecyclerAdapter(news)
                    hnRecyclerView.adapter = adapter
                    progressBar.visibility = GONE
                }
            }

            override fun onFailure(call: Call<NewsList>, t: Throwable) {
                progressBar.visibility = GONE
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
package com.lazyhound.hnmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayoutManager = LinearLayoutManager(this)
        hnRecyclerView.layoutManager = linearLayoutManager

        val news = News()
        val newsList = ArrayList<News>()
        news.title = "News 1"
        news.subtitle = "Subtitle"
        news.url = "https://google.com"
        newsList.add(news)

        adapter = RecyclerAdapter(newsList)
        hnRecyclerView.adapter = adapter
    }
}
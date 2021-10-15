package com.lazyhound.hnmobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lazyhound.hnmobile.utils.SwipeToDelete
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private var news: MutableList<News> = ArrayList()


    private fun setRecyclerViewItemTouchListener() {
        val swipeHandler = object : SwipeToDelete(applicationContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                news.removeAt(position)
                hnRecyclerView.adapter!!.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(hnRecyclerView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linearLayoutManager = LinearLayoutManager(this)
        hnRecyclerView.layoutManager = linearLayoutManager
        setRecyclerViewItemTouchListener()
        swipeContainer.setOnRefreshListener {
            refresh()
        }
        refresh()
    }

    private fun refresh() {
        val apiService: APIService = RestClient.client.create(APIService::class.java)
        val call = apiService.fetchNews()
        news.clear()
        call.enqueue(object : Callback<NewsList> {

            override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
                val body = response.body()
                if (body != null) {
                    news.addAll(body.hits!!)
                    adapter = RecyclerAdapter(news)
                    hnRecyclerView.adapter = adapter
                }
                swipeContainer.isRefreshing = false;
            }

            override fun onFailure(call: Call<NewsList>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                swipeContainer.isRefreshing = false;
            }
        })
    }
}
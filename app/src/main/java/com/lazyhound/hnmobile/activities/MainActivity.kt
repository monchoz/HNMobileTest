package com.lazyhound.hnmobile.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lazyhound.hnmobile.rest.APIService
import com.lazyhound.hnmobile.R
import com.lazyhound.hnmobile.adapters.RecyclerAdapter
import com.lazyhound.hnmobile.rest.RestClient
import com.lazyhound.hnmobile.db.NewsRealm
import com.lazyhound.hnmobile.models.NewsList
import com.lazyhound.hnmobile.utils.Connectivity
import com.lazyhound.hnmobile.utils.SwipeToDelete
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: RecyclerAdapter
    private lateinit var config: RealmConfiguration
    private lateinit var realm: Realm
    private var newsList: MutableList<NewsRealm> = ArrayList()

    private fun setRecyclerViewItemTouchListener() {
        val swipeHandler = object : SwipeToDelete(applicationContext) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val position = viewHolder.adapterPosition
                delete(newsList[position].story_id)
                newsList.removeAt(position)
                hnRecyclerView.adapter!!.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(hnRecyclerView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        realmSetup()
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
        newsList.clear()
        // request local items when no internet connection
        if (Connectivity.checkForInternet(applicationContext)) {
            call.enqueue(object : Callback<NewsList> {
                override fun onResponse(call: Call<NewsList>, response: Response<NewsList>) {
                    val body = response.body()
                    if (body != null) {
                        body.hits!!.forEach {
                            // avoid adding deleted items
                            val news = realm.where(NewsRealm::class.java).equalTo("story_id", it.story_id).findFirst()
                            if (news == null) {
                                // insert news to local database
                                insert(it)
                            }
                        }
                        queryNews()
                    }
                    swipeContainer.isRefreshing = false
                }

                override fun onFailure(call: Call<NewsList>, t: Throwable) {
                    Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_SHORT).show()
                    swipeContainer.isRefreshing = false
                }
            })
        } else {
            queryNews()
        }
    }

    private fun realmSetup() {
        // Ready our SDK
        Realm.init(this)
        // Creating our db with custom properties
        config = RealmConfiguration.Builder()
            .name("news.db")
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getInstance(config)
    }

    private fun queryNews() {
        val list: MutableList<NewsRealm>
        val realmResults = realm.copyFromRealm(realm.where(NewsRealm::class.java).equalTo("active", true).findAll())
        list = realmResults.subList(0, realmResults.size)
        newsList = list
        newsList.sortByDescending { it.created_at }
        adapter = RecyclerAdapter(newsList)
        hnRecyclerView.adapter = adapter
        swipeContainer.isRefreshing = false
    }

    fun insert(news: NewsRealm) {
        realm.executeTransaction { realmTransaction ->
            realmTransaction.insertOrUpdate(news)
        }
    }

    fun delete(id: Int) {
        val news = realm.where(NewsRealm::class.java).equalTo("story_id", id).findFirst()
        realm.beginTransaction()
        news?.apply {
            active = false
        }
        realm.commitTransaction()
    }
}
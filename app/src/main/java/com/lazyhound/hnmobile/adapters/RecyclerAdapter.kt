package com.lazyhound.hnmobile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.lazyhound.hnmobile.R
import com.lazyhound.hnmobile.activities.NewsActivity
import com.lazyhound.hnmobile.db.NewsRealm
import kotlinx.android.synthetic.main.item_news.view.*
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*
import java.time.temporal.TemporalQueries.localDate




class RecyclerAdapter(private val news: MutableList<NewsRealm>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.item_news, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemNews = news[position]
        holder.bind(itemNews)
    }

    override fun getItemCount() = news.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var news: NewsRealm? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = itemView.context
            news?.let {
                val showIntent = NewsActivity.newIntent(context, it)
                context.startActivity(showIntent)
            }
        }

        fun bind(news: NewsRealm) {
            this.news = news
            val datetime = LocalDateTime.parse(news.created_at, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            val date = Date.from(datetime.toInstant(ZoneOffset.UTC))
            val prettyTime = PrettyTime()
            val ago: String = prettyTime.format(date)
            view.hnItemTitle.text = if (news.story_title.isNullOrBlank()) news.title else news.story_title
            view.hnItemSubTitle.text = news.author + " - " + ago
        }
    }
}

private fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

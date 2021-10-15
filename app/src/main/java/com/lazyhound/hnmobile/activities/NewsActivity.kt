package com.lazyhound.hnmobile.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_news.*
import android.webkit.WebView

import android.webkit.WebViewClient
import android.widget.Toast
import com.lazyhound.hnmobile.R
import com.lazyhound.hnmobile.db.NewsRealm
import com.lazyhound.hnmobile.models.News

class NewsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_URL = "url"

        fun newIntent(context: Context, news: NewsRealm): Intent {
            val detailIntent = Intent(context, NewsActivity::class.java)

            detailIntent.putExtra(EXTRA_TITLE, news.story_title)
            detailIntent.putExtra(EXTRA_URL, news.story_url)

            return detailIntent
        }
    }

    class webClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val title = intent.extras?.getString(EXTRA_TITLE)
        val url = intent.extras?.getString(EXTRA_URL)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setTitle(title)

        if (url != null && url.isNotEmpty()) {
            webView.settings.setJavaScriptEnabled(true)
            webView.setWebViewClient(webClient())
            webView.loadUrl(url)
        } else {
            Toast.makeText(applicationContext, "Empty URL", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
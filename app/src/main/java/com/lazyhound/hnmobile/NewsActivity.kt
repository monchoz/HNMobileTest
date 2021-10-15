package com.lazyhound.hnmobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_news.*
import android.webkit.WebView

import android.webkit.WebViewClient

class NewsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_URL = "url"

        fun newIntent(context: Context, news: News): Intent {
            val detailIntent = Intent(context, NewsActivity::class.java)

            detailIntent.putExtra(EXTRA_TITLE, news.title)
            detailIntent.putExtra(EXTRA_URL, news.url)

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

        if (url != null) {
            webView.settings.setJavaScriptEnabled(true)
            webView.setWebViewClient(webClient())
            webView.loadUrl(url)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
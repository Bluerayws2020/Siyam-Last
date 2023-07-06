package com.example.siyam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.databinding.BindingAdapter
import com.example.siyam.R
import com.example.siyam.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val url = intent.getStringExtra("url").toString()

        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(url!!)
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.userAgentString = "Custom User Agent"
        val webSettings: WebSettings = binding.webView.settings
        webSettings.userAgentString = "Mozilla/5.0 (Linux; Android 11; Pixel 5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Mobile Safari/537.36"
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}
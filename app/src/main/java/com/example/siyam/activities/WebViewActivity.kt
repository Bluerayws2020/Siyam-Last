package com.example.siyam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.databinding.BindingAdapter
import androidx.lifecycle.lifecycleScope
import com.example.siyam.R
import com.example.siyam.databinding.ActivityWebViewBinding
import com.example.siyam.helpers.ViewUtils.hide
import com.example.siyam.helpers.ViewUtils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding : ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pb.show()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val url = intent.getStringExtra("url").toString()

        loadPdfFromUrl(url)



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
    private fun loadPdfFromUrl( pdfUrl: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                Log.e("url", pdfUrl)
                val url = URL(pdfUrl)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.connect()
                val length = connection.contentLength
                val inputStream: InputStream = BufferedInputStream(url.openStream(), length)

                launch(Dispatchers.Main) {
                    binding.webView.fromStream(inputStream)
                        .onLoad {
                            binding.pb.hide()
                        }
                        .onPageChange { page, pageCount ->
                            // Page has changed
                        }
                        .load()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ayham",e.toString())
            }
        }
    }


}
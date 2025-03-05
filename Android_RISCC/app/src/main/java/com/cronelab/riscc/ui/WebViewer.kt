package com.cronelab.riscc.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import com.cronelab.riscc.BuildConfig
import com.cronelab.riscc.R
import com.cronelab.riscc.support.constants.URL_ABOUT_US
import com.cronelab.riscc.support.data.database.table.DBResourceFile
import com.cronelab.riscc.ui.resources.ResourceViewModel
import com.cronelab.riscc.ui.resources.ResourceViewModelFactory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.pdf_view.*
import org.kodein.di.generic.instance
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class WebViewer : AppCompatActivity() {
    private var webView: WebView? = null
    private var progressBar: ProgressBar? = null
    val TAG = "SignUp"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_viewer)
        initUI()
        if (intent != null) {
            val url = intent.getStringExtra("Url")
            Log.e("R.id.aboutRootLayout", "webview start in webView class  $url")
            loadWebView(url)
        } else {
            Toast.makeText(this, getString(R.string.unabletoLoadWebPages), Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }


    private fun initUI() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun loadWebView(url: String?) {
        webView!!.settings.javaScriptEnabled = true // enable javascript
        webView!!.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        val settings = webView!!.settings
        settings.domStorageEnabled = true
        progressBar!!.visibility = View.VISIBLE
        webView!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                if (progressBar!!.visibility == View.VISIBLE) {
                    progressBar!!.visibility = View.GONE
                }
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(this@WebViewer, "Error:$description", Toast.LENGTH_SHORT).show()
            }
        }
        webView!!.loadUrl(url!!)
    }


    fun navigateBack(view: View) {
        onBackPressed()
    }


}
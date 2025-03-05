package com.cronelab.riscc.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.cronelab.riscc.BuildConfig
import com.cronelab.riscc.R
import com.cronelab.riscc.support.data.database.table.DBResourceFile
import com.cronelab.riscc.ui.resources.ResourceViewModel
import com.cronelab.riscc.ui.resources.ResourceViewModelFactory
import com.google.gson.Gson
import kotlinx.android.synthetic.main.pdf_view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class PdfViewer : AppCompatActivity(), KodeinAware {

    val TAG = "SignUp"
    override val kodein by kodein()
    private lateinit var resourcesViewModel: ResourceViewModel
    private val resourcesViewModelFactory: ResourceViewModelFactory by instance()
    var aboutUs:Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdf_view)
        resourcesViewModel =
            ViewModelProviders.of(this, resourcesViewModelFactory)[ResourceViewModel::class.java]
        initObservers()
        clickListeners()

        if (intent != null) {
            aboutUs = intent.getBooleanExtra("AboutUs",true)
            getResourceUrlFromLanguage()

        } else {
            Toast.makeText(this, getString(R.string.unabletoLoadWebPages), Toast.LENGTH_SHORT)
                .show()
            finish()
        }
    }

    private fun clickListeners() {
       backPdf.setOnClickListener{
           onBackPressed()
       }
    }

    private fun getResourceUrlFromLanguage() {
        progressBarPdf.visibility = View.VISIBLE
        val languageCode = Locale.getDefault().language
        var title: String? = null
        if (languageCode.equals("SE", ignoreCase = true) || languageCode.equals(
                "SV",
                ignoreCase = true
            )
        ) {
            title = if(aboutUs) "ABOUT_US_SE" else "PRIVACY_POLICY_SE"



        } else if (languageCode.equals("ES", ignoreCase = true)) {
            title = if(aboutUs) "ABOUT_US_ES" else "PRIVACY_POLICY_ES"



        } else if (languageCode.equals("NE", ignoreCase = true)) {

            title = if(aboutUs) "ABOUT_US" else "PRIVACY_POLICY"

        } else {
            title = if(aboutUs) "ABOUT_US" else "PRIVACY_POLICY"
        }
        titlePdf.text =  title.toString()
        resourcesViewModel.getResourceUrlFromDB(title)
    }


    private fun initObservers() {
        resourcesViewModel.resourceUrl.observe(this){
            it?.let{
                Log.e("ResourceUrl", "got url from the database ${Gson().toJson(it)}")
                titlePdf.text = it.description.toString()
                loadPdf(BuildConfig.SERVER_URL + it.url)

            }

        }


    }
    var thread: Thread? = null
    private fun loadPdf(url: String) {
        @SuppressLint("HandlerLeak") val handler: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                //progressDialog.dismiss()
                val bundle = msg.data
                if (bundle.getString("error").equals("0", ignoreCase = true)) { // no error
                    if (bundle.getString("Content-Type")
                            .equals("application/pdf", ignoreCase = true)
                    ) {
                        showPDF(bundle.getString("URI"))
                    }
                } else {
                    titleParentLayoutPdf.visibility = View.GONE
                    ErrorMsg.visibility = View.VISIBLE
                }
            }
        }
        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    val name = System.currentTimeMillis().toString()
                    val pdfFileName = "$name.pdf"
                    val imageFileName = "$name.jpeg"
                    val ulrn = URL(url)
                    Log.d("D2R", ulrn.toString())
                    val con = ulrn.openConnection() as HttpURLConnection
                    con.requestMethod = "GET"

                    val contentType = con.getHeaderField("Content-Type")
                    var sourceFile: File? = null
                    if (contentType.equals("application/pdf", ignoreCase = true)) {
                        sourceFile = File("$cacheDir/$pdfFileName")
                    } else if (contentType.equals(
                            "image/jpeg",
                            ignoreCase = true
                        ) || contentType.equals("image/png", ignoreCase = true)
                    ) {
                        sourceFile = File("$cacheDir/$imageFileName")
                    }
                    val inputStream: InputStream = BufferedInputStream(con.inputStream)
                    Log.e("content Type", con.getHeaderField("Content-Type"))
                    val fos = FileOutputStream(sourceFile!!)
                    val buffer = ByteArray(1024)
                    var len1: Int
                    while (inputStream.read(buffer).also { len1 = it } > 0) {
                        fos.write(buffer, 0, len1)
                    }
                    fos.close()
                    val f = File(sourceFile.path)
                    if (!f.exists()) {
                        Log.e(TAG, "File '" + f.absolutePath + "' not found")
                        throw FileNotFoundException("The file does not exists")
                    }
                    val len = f.length()
                    if (len == 0L) {
                        Log.e(TAG, "File '" + f.absolutePath + "' has no length")
                        throw IllegalArgumentException("File has no length")
                    }
                    val msg = handler.obtainMessage()
                    val bundle = Bundle()
                    bundle.putString("URI", f.absolutePath)
                    bundle.putString("Content-Type", contentType)
                    msg.data = bundle
                    bundle.putString("error", "0")
                    Log.e("bundle", f.absolutePath)
                    handler.sendMessage(msg)
                } catch (e: Exception) {
                    // progressDialog.dismiss()
                    Log.e(TAG, "Exception while writing to device! " + e.message)
                    progressBarPdf.visibility = View.GONE
                    //sending error msg
                    val msg = handler.obtainMessage()
                    val bundle = Bundle()
                    bundle.putString("error", "1")
                    msg.data = bundle
                    handler.sendMessage(msg)
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    private fun showPDF(pdfPath: String?) {
        pdfPath?.let {
            progressBarPdf.visibility = View.GONE
            pdfViewer.fromFile(File(pdfPath)).enableSwipe(true).swipeHorizontal(false)
                .enableDoubletap(true).defaultPage(0).enableAnnotationRendering(false)
                .password(null).scrollHandle(null).enableAntialiasing(true).spacing(0).load()
        }
    }
}
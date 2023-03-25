@file:Suppress("FunctionName")

package com.tg.tools

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.View.VISIBLE
import android.widget.TextView
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.concurrent.thread

class Logging : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logging)
        val login = intent.getStringExtra("login").toString()
        val password = intent.getStringExtra("password").toString()
        val url = intent.getStringExtra("url").toString()
        val urlView = findViewById<TextView>(R.id.textView12)
        val loginView = findViewById<TextView>(R.id.loginLogging)
        val passwordView = findViewById<TextView>(R.id.passwordLogging)
        val result = findViewById<TextView>(R.id.textView10)
        val phrase = findViewById<TextView>(R.id.textView14)
        urlView.text = url.replace("www.", "")
        loginView.text = login
        passwordView.text = password
        thread {
            if ("wp-login.php" in url) {
                try {
                    val res: Connection.Response = Jsoup
                        .connect(url)
                        .data("log", login)
                        .data("pwd", password)
                        .method(Connection.Method.POST)
                        .execute()
                    val doc: Document = res.parse()
                    if (("Unknown" in doc.text()) or ("incorrect" in doc.text()) or ("ERROR" in doc.text())) {
                        runOnUiThread {
                            phrase.visibility = VISIBLE
                            result.text = "Неверный пароль..."
                            result.visibility = VISIBLE
                            result.setTextColor(Color.rgb(255, 255, 255))
                        }
                    } else {
                        runOnUiThread {
                            result.text = "Пароль подобран!"
                            result.visibility = VISIBLE
                            result.setTextColor(Color.rgb(255, 255, 255))
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        result.text = "Ошибка при отправке запроса."
                        result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22.0f)
                        result.visibility = VISIBLE
                        result.setTextColor(Color.rgb(255, 255, 255))
                    }
                }
            } else{
                runOnUiThread {
                    result.text = "Форма входа не найдена..."
                    result.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22.0f)
                    result.visibility = VISIBLE
                    result.setTextColor(Color.rgb(255, 255, 255))
                }
            }
        }
    }

    fun Back(view: View) {
        finish()
    }
}
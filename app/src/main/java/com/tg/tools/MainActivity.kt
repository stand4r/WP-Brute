package com.tg.tools

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText


const val SITE = "https://www.fruitforest.in/wp-login.php"


class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "CheckResult", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.textView2)
        val btn = findViewById<Button>(R.id.buttonAuth)
        val mode = findViewById<Switch>(R.id.switch1)
        mode.setOnCheckedChangeListener { buttonView, isChecked ->
            if(!isChecked){
                tv.text = "Password"
            }else{
                tv.text = "File"
            }
        }
        btn.setOnClickListener {
            btn.isEnabled = false
            if (!mode.isChecked) {
                startLogging()
            } else {
                startLogFile()
            }
            btn.isEnabled = true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            val uri: Uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            startActivity(Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri))
        }
    }

    private fun startLogging() {
        val login = findViewById<TextInputEditText>(R.id.loginInput).text.toString()
        val pass = findViewById<TextInputEditText>(R.id.passInput).text.toString()
        val url = findViewById<TextInputEditText>(R.id.inp).text.toString()
        val intentLog = Intent(this, Logging::class.java)
        intentLog.putExtra("login", login)
        intentLog.putExtra("password", pass)
        intentLog.putExtra("url", url)
        startActivity(intentLog)
    }

    fun openSettings(view: View) {
        val intentSettings = Intent(this, SettingsActivity::class.java)
        startActivity(intentSettings)
    }


    private fun startLogFile() {
        val intentLogFile = Intent(this, LoggingFile::class.java)
        val login = findViewById<TextInputEditText>(R.id.loginInput).text.toString()
        val url = findViewById<TextInputEditText>(R.id.inp).text.toString()
        val fileName = findViewById<TextInputEditText>(R.id.passInput).text.toString()
        intentLogFile.putExtra("login", login)
        intentLogFile.putExtra("url", url)
        intentLogFile.putExtra("name", fileName)
        startActivity(intentLogFile)
    }
}

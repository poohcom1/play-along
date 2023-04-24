package com.poohcom1.playalong

import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: wtf is this for?
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        try {
            YoutubeDL.getInstance().init(applicationContext)
            YoutubeDL.getInstance()
                .updateYoutubeDL(applicationContext, YoutubeDL.UpdateChannel.STABLE)
        } catch (e: YoutubeDLException) {
            Log.e("YoutubeDL", e.toString())
        }

        val urlDebugText: TextView = findViewById(R.id.urlText)
        val urlInput: EditText = findViewById(R.id.urlInput)
        val urlSubmitButton: Button = findViewById(R.id.urlSubmitButton)

        var updated = false

        urlSubmitButton.setOnClickListener {
            val request = YoutubeDLRequest(urlInput.text.toString())
            request.addOption("-f", "best");
            val streamInfo = YoutubeDL.getInstance().getInfo(request)

            urlDebugText.text = streamInfo.url
        }
    }
}
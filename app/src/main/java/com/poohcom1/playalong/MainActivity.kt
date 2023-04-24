package com.poohcom1.playalong

import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.mapper.VideoInfo


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

        val videoView: VideoView = findViewById(R.id.videoView)

        val urlInput: EditText = findViewById(R.id.urlInput)
        val urlSubmitButton: Button = findViewById(R.id.urlSubmitButton)

        urlSubmitButton.setOnClickListener {
            val getUrlRequest = YoutubeDLRequest(urlInput.text.toString())
            getUrlRequest.addOption("-f", "best")

            val streamInfo: VideoInfo = YoutubeDL.getInstance().getInfo(getUrlRequest)
            Log.d("YoutubeDL", streamInfo.url)
            videoView.setVideoURI(Uri.parse(streamInfo.url))
            videoView.start()
        }
    }
}
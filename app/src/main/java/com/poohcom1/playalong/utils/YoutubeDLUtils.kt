package com.poohcom1.playalong.utils

import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import com.yausername.youtubedl_android.mapper.VideoInfo

fun getVideoInfo(url: String): Result<VideoInfo> =
    runCatching {
        val getUrlRequest = YoutubeDLRequest(url)
        getUrlRequest.addOption("-f", "best")
        getUrlRequest.addOption("--no-playlist")

        YoutubeDL.getInstance().getInfo(getUrlRequest)
    }


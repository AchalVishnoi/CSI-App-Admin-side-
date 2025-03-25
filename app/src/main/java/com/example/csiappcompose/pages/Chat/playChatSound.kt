package com.example.csiappcompose.pages.Chat

import android.content.Context
import android.media.MediaPlayer

fun playSound(context: Context, resId: Int) {
    val mediaPlayer = MediaPlayer.create(context, resId)
    mediaPlayer.start()
    mediaPlayer.setOnCompletionListener {
        it.release()
    }
}

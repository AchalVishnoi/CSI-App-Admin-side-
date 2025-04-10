package com.example.csiappcompose.dataModelsResponseTask

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Group(
    val id: Int,
    val members: List<Member>,
    val name: String
): Parcelable


//generate the rtsp video streaming app that give faciolity to record the stream and run the recorded video in it
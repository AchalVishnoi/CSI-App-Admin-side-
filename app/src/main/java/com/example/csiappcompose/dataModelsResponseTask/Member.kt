package com.example.csiappcompose.dataModelsResponseTask

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Member(
    val domain: String,
    val full_name: String,
    val id: Int
): Parcelable
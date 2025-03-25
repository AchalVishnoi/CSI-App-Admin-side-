package com.example.csiappcompose.viewModels

import android.os.Message

sealed class NetWorkResponse <out T>{
    data class Success<out T>(val data:T): NetWorkResponse<T>()
    data class  Error(val message: String): NetWorkResponse<Nothing>()
    object Loading: NetWorkResponse<Nothing>()
}
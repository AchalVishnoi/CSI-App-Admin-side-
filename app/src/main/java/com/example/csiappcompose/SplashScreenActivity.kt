package com.example.csiappcompose


import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csiappcompose.pages.LoginScreen
import com.example.csiappcompose.pages.SplashScreen
import com.example.csiappcompose.pages.SplashUI

import kotlinx.coroutines.delay
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.csiappcompose.pages.SplashUI
import com.example.csiappcompose.viewModels.AuthViewModel
import kotlinx.coroutines.delay
import androidx.compose.runtime.*

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val authViewModel: AuthViewModel = viewModel()
            val token by authViewModel.token.collectAsStateWithLifecycle(initialValue = null)

            LaunchedEffect(token) {
                delay(2000)
                val destination = if (!token.isNullOrEmpty()) {
                    Intent(this@SplashScreenActivity, MainActivity::class.java)
                } else {
                    Intent(this@SplashScreenActivity, LoginScreen::class.java)
                }
                startActivity(destination)
                finish()
            }

            SplashUI()
        }

    }
}
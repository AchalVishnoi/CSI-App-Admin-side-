package com.example.csiappcompose.pages


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.csiappcompose.AuthViewModel
import kotlinx.coroutines.delay

import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.csiappcompose.R

import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, authViewModel: AuthViewModel) {

    val token by authViewModel.token.collectAsStateWithLifecycle(initialValue = null)

    LaunchedEffect(token) {
        delay(2000) // Simulating splash delay
        val destination = if (!token.isNullOrEmpty()) "home" else "login"
        navController.navigate(destination) {
            popUpTo("splash") { inclusive = true }
        }
    }

    SplashUI()
}


@Preview
@Composable
fun SplashUI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Blue),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(0.3f),
            painter = painterResource(id = R.drawable.csi_logo),
            contentDescription = "CSI Logo"

        )

    }
}
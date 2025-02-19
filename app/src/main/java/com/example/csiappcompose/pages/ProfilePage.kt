package com.example.csiappcompose.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.csiappcompose.AuthViewModel


@Composable
fun ProfilePage() {


    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val logoutState = authViewModel.logoutState.collectAsState()



    LaunchedEffect(logoutState) {
        if (logoutState.value) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

     Column (
        modifier = Modifier.fillMaxSize()
            .background(Color(color = 0xFF3BA90B)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("Welcome!")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {  }) {
            Text("Logout")
        }
    }

}



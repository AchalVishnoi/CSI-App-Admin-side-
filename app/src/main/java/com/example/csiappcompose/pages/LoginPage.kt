package com.example.csiappcompose.pages
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.csiappcompose.viewModels.AuthViewModel
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor






@Composable
fun LoginPage(navController: NavController, authViewModel: AuthViewModel) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val loginResult by authViewModel.loginResult.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()

    val context = LocalContext.current


    LaunchedEffect(loginResult) {
        loginResult?.let { result ->
            result.onSuccess {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
            result.onFailure {
                Toast.makeText(context, "Login Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = PrimaryBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                modifier = Modifier.padding(16.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                modifier = Modifier.padding(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                Button(
                    onClick = { authViewModel.loginUser(email.value, password.value)
                              },
                    modifier = Modifier.padding(16.dp)

                ) {
                    Text("Login")
                }
            }
        }
    }
}




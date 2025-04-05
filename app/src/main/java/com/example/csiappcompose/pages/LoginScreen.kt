package com.example.csiappcompose.pages

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            LoginScreenFun()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginScreenFun() {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

//    val loginResult by authViewModel.loginResult.collectAsState()
//    val isLoading by authViewModel.isLoading.collectAsState()
    //val context = LocalContext.current

//    LaunchedEffect(loginResult) {
//        loginResult?.let { result ->
//            result.onSuccess {
//                navController.navigate("home") {
//                    popUpTo("login") { inclusive = true }
//                }
//            }
//            result.onFailure {
//                Toast.makeText(context, "Login Failed: ${it.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PrimaryBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = com.example.csiappcompose.R.drawable.csi_logo_with_background), // Replace with your logo
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 32.dp)
            )

            // Email Input
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("User Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedTextColor = Color.Black
                )
            )

            // Password Input with Eye Button
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White,
                    focusedTextColor = Color.Black
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = Color.Gray
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button with Loading Indicator
            Button(
                onClick = {
                   // authViewModel.loginUser(email.value, password.value)
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0057FF)),
                //enabled = !isLoading  // Disable button when loading
            ) {
//                if (isLoading) {
//                    CircularProgressIndicator(
//                        color = Color.White,
//                        modifier = Modifier.size(24.dp)
//                    )
//                } else {
                 Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//                }
            }

            // Forgot Password
            Text(
                text = "Forgot Password",
                fontSize = 14.sp,
                color = Color.Black,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .clickable {
                        // Handle forgot password action
                    }
            )
        }
    }
}
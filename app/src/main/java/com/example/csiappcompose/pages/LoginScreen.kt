package com.example.csiappcompose.pages

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csiappcompose.MainActivity
import com.example.csiappcompose.SplashScreenActivity
import com.example.csiappcompose.ui.theme.PrimaryBackgroundColor
import com.example.csiappcompose.ui.theme.lightSkyBlue
import com.example.csiappcompose.ui.theme.primary
import com.example.csiappcompose.viewModels.AuthViewModel


class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val authViewModel: AuthViewModel = viewModel()
            LoginScreenFun(authViewModel)

            val loginResult by authViewModel.loginResult.collectAsState()
            val isLoading by authViewModel.isLoading.collectAsState()



            LaunchedEffect(loginResult) {
                loginResult?.let { result ->
                    result.onSuccess { message ->
                        val intent = Intent(
                            this@LoginScreen,
                            if (message == "complete details") MainActivity::class.java else FillYourDetail::class.java
                        )
                        startActivity(intent)
                        finish()
                    }

                    result.onFailure {
                        Toast.makeText(this@LoginScreen, "Login Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }

            }

        }
    }
}


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenFun(authViewModel: AuthViewModel= viewModel()) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginResult by authViewModel.loginResult.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val context = LocalContext.current

    // Check if form is valid (simple email validation)
    val isFormValid = email.value.isNotBlank() &&
            password.value.isNotBlank() &&
            android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightSkyBlue)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .alpha(if (isLoading) 0.5f else 1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    ,
                contentAlignment = Alignment.Center,

            ) {
                Image(
                    painter = painterResource(id = com.example.csiappcompose.R.drawable.csi_logo_with_background),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 32.dp)
                )
            }

            Text(
                text = "Login",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            inputField(
                message = email,
                placeholder = "Email",
                modifier = Modifier.padding(bottom = 8.dp)
            )


            Card(
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(color = Color(0xFFF7F7F7))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    TextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        placeholder = { Text("Password", fontSize = 16.sp) },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            cursorColor = Color.Black,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray
                        ),
                        textStyle = TextStyle(fontSize = 16.sp),
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
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isFormValid) {
                        authViewModel.loginUser(email.value, password.value)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,
                    disabledContainerColor = primary.copy(alpha = 0.1f),
                    contentColor = Color.White,  // Enabled text color
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                ),
                enabled = isFormValid && !isLoading
            ) {
                Text("Login", fontSize = 16.sp)
            }

            // Forgot Password
            Text(
                text = "Forgot Password?",
                color = primary,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        val intent = Intent(context, ForgetScreen::class.java)
                        context.startActivity(intent)
                    }
            )
        }

        // Loading overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .zIndex(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = primary)
            }
        }
    }
}
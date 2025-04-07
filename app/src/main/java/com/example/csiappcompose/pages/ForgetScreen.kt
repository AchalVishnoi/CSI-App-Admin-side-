package com.example.csiappcompose.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csiappcompose.Backend.RetrofitInstance.apiService
import com.example.csiappcompose.MainActivity
import com.example.csiappcompose.R
import com.example.csiappcompose.dataModelsRequests.ForgotPasswordRequest
import com.example.csiappcompose.dataModelsRequests.LoginRequest

import com.example.csiappcompose.pages.LoginScreen
import com.example.csiappcompose.ui.theme.lightSkyBlue
import com.example.csiappcompose.ui.theme.primary
import com.example.csiappcompose.ui.theme.skyBlue
import com.example.csiappcompose.viewModels.AuthViewModel
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


class ForgetScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForgetPasswordFlowScreen()
        }
    }
}


@Preview
@Composable
fun ForgetPasswordFlowScreen() {
    var moveToOtpScreen by remember { mutableStateOf(false) }
    var emailShared by remember { mutableStateOf("") }

    if (moveToOtpScreen) {
        OtpAndPasswordScreen(email = emailShared)
    } else {
        ForgetPasswordScreen(
            onOtpSent = { email ->
                emailShared = email.trim()
                moveToOtpScreen = true
            }
        )
    }
}
// Reusable Text Field Components
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier
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
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp),
                placeholder = { Text(placeholder, fontSize = 16.sp) },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
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
                trailingIcon = trailingIcon
            )
        }
    }
}

@Composable
fun EmailTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    CustomTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = "Email",
        modifier = modifier,
        keyboardType = KeyboardType.Email
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isPasswordVisible: Boolean,
    onPasswordVisibilityToggle: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    CustomTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = "Password",
        modifier = modifier,
        keyboardType = KeyboardType.Password,
        isPassword = true,
        isPasswordVisible = isPasswordVisible,
        trailingIcon = if (onPasswordVisibilityToggle != null) {
            {
                IconButton(onClick = onPasswordVisibilityToggle) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        tint = Color.Gray
                    )
                }
            }
        } else null
    )
}

@Composable
fun OtpTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    CustomTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = "OTP",
        modifier = modifier,
        keyboardType = KeyboardType.Number
    )
}

// Updated ForgetPasswordScreen using reusable components
@Composable
fun ForgetPasswordScreen(
    authViewModel: AuthViewModel = viewModel(),
    onOtpSent: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var errorString by remember { mutableStateOf<String?>(null) }
    val isLoading by authViewModel.isLoading.collectAsState()
    val requestForOtp by authViewModel.requestForOtpResult.collectAsState()
    val context = LocalContext.current

    val isFormValid by remember {
        derivedStateOf {
            email.isNotEmpty() &&
                    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
    }

    LaunchedEffect(requestForOtp?.hashCode()) {
        requestForOtp?.onSuccess {
            errorString = null
            onOtpSent(email)
            authViewModel.clearOtpResult()
        }?.onFailure {
            errorString = it.message
            authViewModel.clearOtpResult()
        }
    }

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
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.csi_logo_with_background),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 32.dp)
                )
            }

            Text(
                text = "Forgot Password",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Email Input using reusable component
            EmailTextField(
                value = email,
                onValueChange = { email = it }
            )

            errorString?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { authViewModel.requestForOtp(email) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) primary else primary.copy(alpha = 0.1f)
                ),
                enabled = isFormValid && !isLoading
            ) {
                Text("Send OTP", fontSize = 16.sp)
            }

            // Login link
            Text(
                text = "Back to Login",
                color = primary,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        val intent = Intent(context, LoginScreen::class.java)
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

// Updated OtpAndPasswordScreen using reusable components
@Composable
fun OtpAndPasswordScreen(
    email: String,
    authViewModel: AuthViewModel = viewModel()
) {
    var otp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorString by remember { mutableStateOf<String?>(null) }
    val isLoading by authViewModel.isLoading.collectAsState()
    val resetPasswordResult by authViewModel.resetPasswordResult.collectAsState()
    val context = LocalContext.current

    val isFormValid by remember {
        derivedStateOf {
            otp.isNotEmpty() &&
                    password.isNotEmpty() &&
                    confirmPassword.isNotEmpty() &&
                    password == confirmPassword
        }
    }

    LaunchedEffect(resetPasswordResult?.hashCode()) {
        resetPasswordResult?.onSuccess {
            val intent = Intent(context, LoginScreen::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }?.onFailure {
            errorString = it.message
            authViewModel.clearResetResult()
        }
    }

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
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.csi_logo_with_background),
                    contentDescription = "App Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 32.dp)
                )
            }

            Text(
                text = "Reset Password",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Using reusable components
            OtpTextField(
                value = otp,
                onValueChange = { otp = it }
            )

            PasswordTextField(
                value = password,
                onValueChange = { password = it },
                isPasswordVisible = false
            )

            PasswordTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                isPasswordVisible = false
            )

            errorString?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { authViewModel.resetPassword(email, otp, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 17.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primary,  // Enabled color
                    disabledContainerColor = primary.copy(alpha = 0.1f),  // Disabled color (50% opacity of primary)
                    contentColor = Color.White,  // Enabled text color
                    disabledContentColor = Color.White.copy(alpha = 0.5f)  // Disabled text color
                ),
                enabled = isFormValid && !isLoading
            ) {
                Text("Reset Password", fontSize = 16.sp)
            }

            // Login link
            Text(
                text = "Back to Login",
                color = primary,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clickable {
                        val intent = Intent(context, LoginScreen::class.java)
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
package com.example.csiappcompose.pages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csiappcompose.Backend.RetrofitInstance.apiService
import com.example.csiappcompose.MainActivity
import com.example.csiappcompose.R
import com.example.csiappcompose.dataModelsRequests.ForgotPasswordRequest
import com.example.csiappcompose.dataModelsRequests.LoginRequest
import com.example.csiappcompose.pages.HomePage.inputField
import com.example.csiappcompose.pages.LoginScreen
import com.example.csiappcompose.ui.theme.skyBlue
import com.example.csiappcompose.viewModels.AuthViewModel
import kotlinx.coroutines.coroutineScope
class ForgetScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForgetPasswordFlowScreen()
        }
    }
}

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

@Composable
fun ForgetPasswordScreen(
    authViewModel: AuthViewModel = viewModel(),
    onOtpSent: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var errorString by remember { mutableStateOf<String?>(null) }
    val isLoading by authViewModel.isLoading.collectAsState()
    val requestForOtp by authViewModel.requestForOtpResult.collectAsState()

    val clickable by remember {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.csi_logo_with_background),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        inputField(email, { email = it }, "Email", errorString)

        errorString?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { authViewModel.requestForOtp(email) },
                enabled = clickable,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(50.dp)
            ) {
                Text("Send OTP")
            }
        }
    }
}

@Composable
fun OtpAndPasswordScreen(
    email: String,
    authViewModel: AuthViewModel = viewModel()
) {
    var otp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorString by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val isLoading by authViewModel.isLoading.collectAsState()
    val resetPasswordResult by authViewModel.resetPasswordResult.collectAsState()

    val clickable by remember {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        inputField(otp, { otp = it }, "OTP", errorString)
        inputField(password, { password = it }, "New Password", errorString)
        inputField(confirmPassword, { confirmPassword = it }, "Confirm Password", errorString)

        errorString?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { authViewModel.resetPassword(email, otp, password) },
                enabled = clickable,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(50.dp)
            ) {
                Text("Verify")
            }
        }
    }
}

@Composable
fun inputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorText: String? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = errorText != null,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (label.contains("Email", ignoreCase = true))
                KeyboardType.Email else KeyboardType.Text
        )
    )
}

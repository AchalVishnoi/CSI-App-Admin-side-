package com.example.csiappcompose.pages

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Switch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.csiappcompose.MainActivity
import com.example.csiappcompose.R
import com.example.csiappcompose.pages.Chat.ProfileImage
import com.example.csiappcompose.pages.HomePage.DatePickerField
import com.example.csiappcompose.pages.HomePage.ImageUploadScreen
import com.example.csiappcompose.ui.theme.lightSkyBlue
import com.example.csiappcompose.ui.theme.lightWaterBlue
import com.example.csiappcompose.ui.theme.primary
import com.example.csiappcompose.viewModels.HomePageViewModel
import com.example.csiappcompose.viewModels.HomePageViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.Calendar

class FillYourDetail : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProfileFormScreen()
        }
    }
}


@Preview
@Composable
fun ProfileFormScreen() {
    val context = LocalContext.current
    val viewModel: HomePageViewModel = viewModel(factory = HomePageViewModelFactory(context))

    val selectedDomains = remember { mutableStateOf("App Dev") }
    val selectedBranches = remember { mutableStateOf("CSE") }
    val selectedGender = remember { mutableStateOf("MALE") }

    val name = remember { mutableStateOf("") }
    val studentNo = remember { mutableStateOf("") }
    val dob = remember { mutableStateOf("") }
    val linkedin = remember { mutableStateOf("") }
    val github = remember { mutableStateOf("") }
    val bio = remember { mutableStateOf("") }
    var selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    var hosteller = remember { mutableStateOf(true) }

    val domainOptions = listOf( "Backend", "Frontend","App Dev", "ML", "UI/UX", "Full Stack")
    val branchOptions = listOf("CSE", "CSE-AIDS", "CSE-AI/ML", "CS", "CST", "CS-HINDI", "IT", "ECE", "EN", "ME", "CIVIL")
    val genderOptions = listOf("MALE", "FEMALE", "OTHERS")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightSkyBlue)
    ) {
        // Single scrollable Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .alpha(if (viewModel.isProfileLoading.value) 0.5f else 1f)
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
                text = "Fill Your Details",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(16.dp))

            inputField(name, "Name")
            inputField(studentNo, "Student No.")
            DobPicker(dob)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Hosteller", fontSize = 16.sp, color = Color.Black)

                Switch(
                    checked = hosteller.value,
                    onCheckedChange = { hosteller.value = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = primary,
                        checkedTrackColor = primary.copy(alpha = 0.5f),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFD1D1D6)
                    )
                )
            }

            Spacer(Modifier.height(12.dp))

            MultiCheckSection("Domain", domainOptions, selectedDomains)
            Spacer(Modifier.height(12.dp))

            MultiCheckSection("Branch", branchOptions, selectedBranches)
            Spacer(Modifier.height(12.dp))

            MultiCheckSection("Gender", genderOptions, selectedGender)
            Spacer(Modifier.height(12.dp))

            CustomTextField(linkedin.value, { linkedin.value = it }, "LinkedIn URL")
            CustomTextField(github.value, { github.value = it }, "GitHub URL")
            CustomTextField(bio.value, { bio.value = it }, "Bio")

            Spacer(Modifier.height(16.dp))

            UploadProfileImage("Profile Image", selectedImageUri = selectedImageUri) { }

            val isFormComplete = name.value.isNotBlank()
                    && studentNo.value.isNotBlank()
                    && dob.value.isNotBlank()
                    && linkedin.value.isNotBlank()
                    && github.value.isNotBlank()
                    && bio.value.isNotBlank()
                    && selectedImageUri.value != null

            Button(
                onClick = {
                    if (isFormComplete) {
                        val imagePart = selectedImageUri.value?.let { uri ->
                            try {
                                val inputStream = context.contentResolver.openInputStream(uri)
                                val requestBody = inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
                                val fileName = "upload_${System.currentTimeMillis()}.jpg"
                                requestBody?.let {
                                    MultipartBody.Part.createFormData("photo", fileName, it)
                                }
                            } catch (e: IOException) {
                                e.printStackTrace()
                                null
                            }
                        }


                        viewModel.submitProfileDetails(
                            branch = selectedBranches.value,
                            domain = domainOptions.indexOf(selectedDomains.value),
                            dob = dob.value,
                            linkedinUrl = linkedin.value,
                            bio = bio.value,
                            githubUrl = github.value,
                            hosteller = hosteller.value,
                            photo = imagePart
                        ) {
                            val intent = Intent(context, MainActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            context.startActivity(intent)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormComplete) primary else primary.copy(alpha = 0.1f)
                )
            ) {
                Text("Submit")
            }
        }

        // Loading overlay
        if (viewModel.isProfileLoading.value) {
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


@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp, max = 120.dp),
        placeholder = {
            Text(placeholder)
        },
        maxLines = 6,
        singleLine = false,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedPlaceholderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray
        )
    )
}

@Composable
fun MultiCheckSection(
    label: String,
    options: List<String>,
    selectedOptions: MutableState<String>
) {
    if (label.isNotBlank()) {
        Text(label, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, modifier = Modifier.padding(vertical = 8.dp))
    }

    options.forEach { option ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable {
                    if (selectedOptions.value!=option) selectedOptions.value=option

                }
        ) {
            Checkbox(
                checked = selectedOptions.value == option,
                onCheckedChange = {
                        _ ->
                   selectedOptions.value = option
                },
                colors = CheckboxDefaults.colors(primary)

            )
            Text(text = option)
        }
    }
}


@Composable
fun inputField(
    message: MutableState<String>,
    placeholder: String = "Enter text",
    error: String? = null,
    modifier: Modifier = Modifier
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
                value = message.value,
                onValueChange = {
                    message.value = it.trimStart() // avoid accidental leading spaces
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp, max = 120.dp),
                placeholder = { Text(text = placeholder, fontSize = 16.sp) },
                maxLines = 6,
                singleLine = false,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                textStyle = TextStyle(fontSize = 16.sp)
            )

            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }
    }


}


@Composable
fun DobPicker(date: MutableState<String>,modifier: Modifier= Modifier) {

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date.value = "${dayOfMonth}/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.datePicker.maxDate = calendar.timeInMillis




    Card(
        modifier = Modifier
            .padding(horizontal = 17.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { datePickerDialog.show() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFFF7F7F7))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (date.value.isEmpty()) "Date of birth" else date.value,
                fontSize = 16.sp,
                color = if (date.value.isEmpty()) Color.Gray else Color.Black
            )

            Icon(
                painter = painterResource(id = R.drawable.date_range),
                contentDescription = "collender",
                modifier = modifier
            )

        }
    }






}

@Composable
fun UploadProfileImage(
    placeholder: String = "Profile Image",
    error: String? = null,
    modifier: Modifier = Modifier,
    selectedImageUri: MutableState<Uri?>,
    onImageSelected: (Uri) -> Unit,
) {
    val getContent = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri.value = uri
        uri?.let { onImageSelected(it) }
    }

    Card(
        modifier = modifier
            .padding(horizontal = 17.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        onClick = {
            getContent.launch("image/*")
        },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(color = Color(0xFFF7F7F7))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Row for icon and placeholder text
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.vector__3_),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = selectedImageUri.value?.let { "Poster Selected Preview" }
                        ?: placeholder,
                    color = Color.Black,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            selectedImageUri.value?.let { imgUrl ->
                Card(
                    modifier = Modifier
                        .padding(end = 5.dp, start = 3.dp,top=5.dp)
                        .size(100.dp),
                    shape = CircleShape,
                ) {
                    CompositionLocalProvider(LocalInspectionMode provides false) {

                            ProfileImage(imgUrl.toString()) {}



                        }
                    }
                }



            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
        }
    }
}

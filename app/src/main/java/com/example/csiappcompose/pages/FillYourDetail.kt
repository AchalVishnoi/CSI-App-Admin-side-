package com.example.csiappcompose.pages

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csiappcompose.R
import com.example.csiappcompose.pages.HomePage.ImageUploadScreen

class FillYourDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fill_your_detail)
        setContent {
            ProfileFormScreen()
        }
    }
}

@Preview
@Composable
fun ProfileFormScreen() {
    val selectedYears = remember { mutableStateListOf<String>() }
    val selectedDomains = remember { mutableStateListOf<String>() }
    val selectedBranches = remember { mutableStateListOf<String>() }
    val selectedGender = remember { mutableStateListOf<String>() }

    val name = remember { mutableStateOf("") }
    val studentNo = remember { mutableStateOf("") }
    val dob = remember { mutableStateOf("") }
    val linkedin = remember { mutableStateOf("") }
    val github = remember { mutableStateOf("") }
    val bio = remember { mutableStateOf("") }

    val yearOptions = listOf("2nd year", "3rd year", "4th year")
    val domainOptions = listOf("App Dev", "Frontend", "Backend", "ML", "UI/UX", "Full Stack")
    val branchOptions = listOf("CSE", "CSE-AIDS", "CSE-AI/ML", "CS", "CST", "CS-HINDI", "IT", "ECE", "EN", "ME", "CIVIL")
    val genderOptions = listOf("MALE", "FEMALE", "TRANS", "NON-BINARY", "QUEER", "NONE OF THE ABOVE")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Logo Placeholder
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

        Text("Fill Your ________", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        inputField(name, "Name")
        inputField(studentNo, "Student No.")
        inputField(dob, "DOB (DD-MM-YYYY)")
//        ImageUploadScreen(selectedImageUri = null) {
//            // Handle image upload
//
//        }
        Spacer(Modifier.height(12.dp))
        MultiCheckSection("Domains & Year", yearOptions, selectedYears)
        MultiCheckSection("", domainOptions, selectedDomains)

        Spacer(Modifier.height(12.dp))
        MultiCheckSection("Branch", branchOptions, selectedBranches)

        Spacer(Modifier.height(12.dp))
        MultiCheckSection("Gender", genderOptions, selectedGender)

        Spacer(Modifier.height(12.dp))
        CustomTextField(value = linkedin.value, onValueChange = { linkedin.value = it }, placeholder = "LinkedIn URL")
        CustomTextField(value = github.value, onValueChange = { github.value = it }, placeholder = "GitHub URL")
        CustomTextField(value = bio.value, onValueChange = { bio.value = it }, placeholder = "Bio")

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Upload profile pic logic */ }, modifier = Modifier.fillMaxWidth()) {
            Text("Upload Profile Picture")
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
    selectedOptions: SnapshotStateList<String>
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
                    if (selectedOptions.contains(option)) selectedOptions.remove(option)
                    else selectedOptions.add(option)
                }
        ) {
            Checkbox(
                checked = selectedOptions.contains(option),
                onCheckedChange = {
                    if (it) selectedOptions.add(option)
                    else selectedOptions.remove(option)
                }
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
    modifier: Modifier= Modifier
) {
    Card(
        modifier = modifier
            .padding(horizontal = 17.dp, vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
        ,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color = Color(0xFFF7F7F7))
                .padding(2.dp)
        ) {

            TextField(
                value = message.value,
                onValueChange = { message.value = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp, max = 120.dp),

                placeholder = { Text(placeholder) },
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

                )


            if (error != null) {
                Text(
                    text = error,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

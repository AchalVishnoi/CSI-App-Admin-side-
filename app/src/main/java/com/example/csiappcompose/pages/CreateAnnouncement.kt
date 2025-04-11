package com.example.csiappcompose.pages

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csiappcompose.R
import com.example.csiappcompose.TopBar
import com.example.csiappcompose.ui.theme.lightSkyBlue

class CreateAnnouncement : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Announcement()
        }
    }
}

@Preview
@Composable
fun Announcement() {

    val yearOptions = listOf("4th Year", "3rd Year", "2nd Year")
    val taskOptions = listOf("CSI app", "Code Compiler", "Event Management")
    val domainOptions = listOf("App Dev", "Frontend", "Backend", "ML", "UI/UX")
    val memberList = listOf("Aditya", "Ashya", "Sandeep", "Harsh", "Sparsh")

    val selectedYears = remember { mutableStateListOf<String>() }
    val selectedDomains = remember { mutableStateListOf<String>() }
    val selectedTasks = remember { mutableStateListOf<String>() }
    val selectedMembers = remember { mutableStateListOf<String>() }

    val announcementText = remember { mutableStateOf("") }

    val activity = (LocalContext.current as? Activity)

    androidx.compose.material.Scaffold(
        modifier = Modifier.fillMaxSize().padding(top = 0.dp),
        topBar = {
            TopBar()
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(lightSkyBlue)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                androidx.compose.material.IconButton(onClick = { activity?.finish() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Make an Announcement",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Text("To", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(start = 16.dp))

            Spacer(modifier = Modifier.height(8.dp))
            MultiSelectDropdown("Select Years", yearOptions, selectedYears)
            Spacer(modifier = Modifier.height(16.dp))
            MultiSelectDropdown("Select Domains", domainOptions, selectedDomains)
            Spacer(modifier = Modifier.height(16.dp))
            MultiSelectDropdown("Select Tasks", taskOptions, selectedTasks)
            Spacer(modifier = Modifier.height(16.dp))
            MultiSelectDropdown("Select Members", memberList, selectedMembers)

            Spacer(modifier = Modifier.height(16.dp))


            Card(
                modifier = Modifier
                    .padding(horizontal = 17.dp, vertical = 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                TextField(
                    value = announcementText.value,
                    onValueChange = { announcementText.value = it },
                    placeholder = { Text("Write an Announcement") },
                    modifier = Modifier.fillMaxWidth(),
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
            }
            Button(
                onClick = {
                   // yha submit krna hai
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
            ) {
                Text("Submit", color = Color.White)
            }
        }
    }
}

@Composable
fun MultiSelectDropdown(
    label: String,
    options: List<String>,
    selectedOptions: SnapshotStateList<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(horizontal = 17.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedOptions.joinToString(", "),
                onValueChange = {},
                readOnly = true,
                label = { Text(label) },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = Color.Black,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedPlaceholderColor = Color.Gray
                ),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
            )

            if (expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(8.dp)
                ) {
                    options.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    if (selectedOptions.contains(option)) {
                                        selectedOptions.remove(option)
                                    } else {
                                        selectedOptions.add(option)
                                    }
                                }
                        ) {
                            if (label == "Select Members") {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .size(24.dp)
                                )
                                Text(
                                    option,
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Text(
                                    option,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Checkbox(
                                checked = selectedOptions.contains(option),
                                onCheckedChange = {
                                    if (it) selectedOptions.add(option)
                                    else selectedOptions.remove(option)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}





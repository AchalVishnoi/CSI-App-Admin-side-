package com.example.csiappcompose.pages

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.csiappcompose.R
import androidx.compose.ui.tooling.preview.Preview

class CreateAnnouncement : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_announcement)
        setContent {
            AnnouncementScreen()
        }
    }
}

@Composable
fun AnnouncementScreen() {
    var selectedYear by remember { mutableStateOf<String?>(null) }
    var selectedDomain by remember { mutableStateOf<String?>(null) }
    var selectedTask by remember { mutableStateOf("CSI app") }
    var announcementText by remember { mutableStateOf(TextFieldValue()) }
    var selectedMember by remember { mutableStateOf<String?>(null) }

    val years = listOf("4th Year", "3rd Year", "2nd Year")
    val domains = listOf("App Dev", "Frontend", "Backend", "ML")
    val tasks = listOf("CSI app", "Code Compiler", "Social Media App")

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        item {
            Text(text = "Make an Announcement", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            DropdownSelector(
                label = "Year",
                options = years,
                selectedOption = selectedYear ?: "Select Year"
            ) {
                selectedYear = it
                selectedDomain = null
            }
        }

        if (selectedYear != null) {
            item {
                DropdownSelector(
                    label = "Domain",
                    options = domains,
                    selectedOption = selectedDomain ?: "Select Domain"
                ) {
                    selectedDomain = it
                }
            }
        }

        item {
            DropdownSelector(
                label = "Task",
                options = tasks,
                selectedOption = selectedTask
            ) {
                selectedTask = it
            }
        }

        item {
            MemberSelection(onMemberSelected = { selectedMember = it })
        }

        item {
            selectedMember?.let {
                Text(
                    text = "Selected Member: $it",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }

        item {
            TextField(
                value = announcementText,
                onValueChange = { announcementText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Write an Announcement") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions.Default
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Button(
                onClick = {
                    // TODO: Replace with API call to send announcement
                    println("Year: $selectedYear, Domain: $selectedDomain, Task: $selectedTask, Member: $selectedMember, Text: ${announcementText.text}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Send Announcement")
            }
        }
    }
}

@Composable
fun DropdownSelector(label: String, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                Text(text = selectedOption)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }, text = { Text(text = option) })
                }
            }
        }
    }
}

@Composable
fun MemberSelection(onMemberSelected: (String) -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Text(text = "Member", style = MaterialTheme.typography.bodyMedium)
        var searchText by remember { mutableStateOf(TextFieldValue()) }
        val allMembers = listOf("Aditya", "Amit", "Rohan", "Sandeep", "Harsh", "Shreya", "Anjali")
        val filteredMembers = allMembers.filter { it.contains(searchText.text, ignoreCase = true) }

        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search") }
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(filteredMembers) { member ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable { onMemberSelected(member) }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.csi_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(8.dp)
                        )
                        Text(
                            text = member,
                            modifier = Modifier.padding(8.dp),
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAnnouncementScreen() {
    AnnouncementScreen()
}

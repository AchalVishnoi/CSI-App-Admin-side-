// TaskDetailsActivity.kt
package com.example.csiappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TaskDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val date = intent.getStringExtra("date") ?: "No data"
        val name = intent.getStringExtra("name") ?: "No name"
        val progress = intent.getIntExtra("progress", 0)

        setContent {
            TaskDetailsScreen(date, name, progress)
        }
    }
}

@Composable
fun TaskDetailsScreen(data: String, name: String, progress: Int) {
    val groups = remember { sampleGroups() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        item {
            Text("Task Overview", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(name, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))
            }
            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(progress = progress / 100f, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(groups) { group ->
            GroupSection(group)
        }
    }
}

@Composable
fun GroupSection(group: Group) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                group.name, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6200EE), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text("Attachments", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))

            group.domains.forEach { domain ->
                DomainSection(domain)
            }
        }
    }
}

@Composable
fun DomainSection(domain: Domain) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(domain.name, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF6200EE))
            Spacer(modifier = Modifier.height(8.dp))

            domain.members.forEach { member ->
                Text("${member.name} - ${member.role}", fontSize = 16.sp)
            }
        }
    }
}

// Sample data classes and extended dummy data for scrolling
data class Group(val name: String, val domains: List<Domain>)
data class Domain(val name: String, val members: List<Member>)
data class Member(val name: String, val role: String)

fun sampleGroups() = listOf(
    Group(
        "GROUP 1",
        listOf(
            Domain("App Dev", List(10) { Member("Aditi $it", "App Dev") }),
            Domain("Frontend", List(8) { Member("Akshay $it", "Frontend") }),
            Domain("Backend", List(12) { Member("Ankit $it", "Backend") })
        )
    ),
    Group(
        "GROUP 2",
        listOf(
            Domain("UI/UX", List(15) { Member("Aarya $it", "UI/UX") }),
            Domain("QA", List(7) { Member("Aman $it", "QA") })
        )
    ),
    Group(
        "GROUP 3",
        listOf(
            Domain("Database", List(10) { Member("Arjun $it", "Database") }),
            Domain("Cloud", List(9) { Member("Alok $it", "Cloud Engineer") })
        )
    )
)

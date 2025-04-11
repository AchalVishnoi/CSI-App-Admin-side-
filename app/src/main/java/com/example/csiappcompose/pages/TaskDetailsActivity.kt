package com.example.csiappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csiappcompose.dataModelsResponseTask.Group
import com.example.csiappcompose.dataModelsResponseTask.Member

class TaskDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val date = intent.getStringExtra("date") ?: "No data"
        val name = intent.getStringExtra("name") ?: "No name"
        val progress = intent.getFloatExtra("progress", 0f)
        val description = intent.getStringExtra("description") ?: ""
        val attachment = intent.getStringExtra("attachment") ?: "No attachment"
        val group = intent.getParcelableExtra("group") ?: Group(0, emptyList(), "")

        setContent {
            TaskDetailsScreen(date, name, progress, description, group, attachment)
        }
    }

    @Composable
    fun TaskDetailsScreen(
        date: String,
        name: String,
        progress: Float,
        description: String,
        group: Group,
        attachment: String
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize().padding(top = 0.dp),
            topBar = {
                TopBar()
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(start = 8.dp, top = 70.dp, end = 8.dp)
            ) {
                item {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )
                        Text(
                            text = "Task Overview",
                            fontSize = 24.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            name,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6200EE),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Column {
                        Row {
                            Text("Progress...")
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "$progress%",
                                fontSize = 16.sp,
                                color = Color(0xFF6200EE),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        LinearProgressIndicator(
                            progress = progress / 100f,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = description,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }

                    Text("Attachments", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = attachment,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                item {
                    GroupSection( group = listOf(group))
                }
            }
        }
    }

    @Composable
    fun GroupSection(
        group: List<Group>,
    ) {
        group.forEach { grp ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF6200EE), RoundedCornerShape(8.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = grp.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    grp.members.forEach { member ->
                        MemberCard(member)
                    }
                }
            }
        }
    }

    @Composable
    fun MemberCard(member: Member) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            elevation = cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = member.full_name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = member.domain,
                    fontSize = 16.sp
                )
            }
        }
    }
}

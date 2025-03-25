// TaskDetailsActivity.kt
package com.example.csiappcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TaskDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.getStringExtra("data") ?: "No data"
        val name = intent.getStringExtra("name") ?: "No name"
        val progress = intent.getIntExtra("progress", 0)

        setContent {
            TaskDetailsScreen(data, name, progress)
        }
    }
}

@Composable
fun TaskDetailsScreen(data: String, name: String, progress: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Task Name: $name", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Task Data: $data", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Progress: $progress%", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = progress / 100f,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

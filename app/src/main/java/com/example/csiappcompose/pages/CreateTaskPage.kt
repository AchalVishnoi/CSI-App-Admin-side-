package com.example.csiappcompose.pages

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.csiappcompose.TopBar

class CreateTaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = intent.getStringExtra("data") ?: "No data"
        val name = intent.getStringExtra("name") ?: "No name"
        val progress = intent.getIntExtra("progress", 0)

        setContent {
            CreateTaskPageFun()
        }
    }
}

@Composable
fun CreateTaskPageFun(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = Modifier.fillMaxSize().padding(top = 0.dp),
        topBar = {
            TopBar()
        },
    ) { innerPadding ->
        Text("Create Task Page",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}
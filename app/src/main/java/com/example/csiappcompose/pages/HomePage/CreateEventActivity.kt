package com.example.csiappcompose.pages.HomePage

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csiappcompose.viewModels.HomePageViewModel
import com.example.csiappcompose.viewModels.HomePageViewModelFactory

class CreateEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

   setContent{
    CreateEvent()
     }



    }
}
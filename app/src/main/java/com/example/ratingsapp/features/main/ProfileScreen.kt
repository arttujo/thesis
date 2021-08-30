package com.example.ratingsapp.features.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun ProfileScreen(mainVm: MainViewModel) {
    Column(modifier = Modifier.fillMaxSize()) {

        Text(text = "Profile")
    }
}



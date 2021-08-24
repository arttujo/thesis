package com.example.ratingsapp.features.login_register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ratingsapp.components.BackArrowTopBar
import com.example.ratingsapp.components.NoActionTopBar
import com.example.ratingsapp.ui.theme.RatingsAppTheme


@Composable
fun RegisterScreen(navController: NavController){
    Scaffold(
        topBar = { BackArrowTopBar(navController = navController) },
        modifier = Modifier
            .fillMaxSize()){
        
        Column() {
            Text(text = "dsadsadsa")
        }
    }

}




@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()

    RatingsAppTheme {
        RegisterScreen(navController)
    }
}
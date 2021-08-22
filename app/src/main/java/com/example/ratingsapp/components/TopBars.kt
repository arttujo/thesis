package com.example.ratingsapp.components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ratingsapp.R
import com.example.ratingsapp.ui.theme.RatingsAppTheme

@Composable
fun MainTopBar(onLogoutClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name),fontSize = 16.sp) },
        backgroundColor = MaterialTheme.colors.primary,
        elevation =0.dp,
        contentColor = Color.Black,
        actions = {
            IconButton(onClick = onLogoutClick) {
                Icon(painter = painterResource(id = R.drawable.ic_logout), contentDescription = stringResource(
                    id = R.string.logout
                ),tint = Color.Black )
            }
        }
    )
}

@Preview
@Composable
fun PreviewTop() {
    RatingsAppTheme {
        MainTopBar(onLogoutClick = {})
    }
}

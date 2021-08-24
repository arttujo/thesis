package com.example.ratingsapp.components

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
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



@Composable
fun NoActionTopBar() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name),fontSize = 16.sp) },
        backgroundColor = MaterialTheme.colors.primary,
        elevation =0.dp,
        contentColor = Color.Black,
    )
}

@Composable
fun BackArrowTopBar(navController: NavController) {

    /**
     * Use this elsewhere
     */
    var canPop by remember { mutableStateOf(false) }

    navController.addOnDestinationChangedListener{controller, _, _ ->
        canPop = controller.previousBackStackEntry != null
    }

    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name),fontSize = 16.sp) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.Black,
        navigationIcon = {
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon( painterResource(id = R.drawable.ic_back), contentDescription = stringResource(
                    id = R.string.navigate_back
                ))
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

@Preview
@Composable
fun NoActionPreview(){
    RatingsAppTheme {
        NoActionTopBar()
    }
}

@Preview
@Composable
fun BackButtonTopBar() {
    RatingsAppTheme {
        BackArrowTopBar(rememberNavController())
    }
}
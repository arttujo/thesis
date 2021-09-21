package com.example.ratingsapp.features.login_register

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ratingsapp.R
import com.example.ratingsapp.components.LoadingOverlay
import com.example.ratingsapp.components.NoActionTopBar
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.ui.theme.RatingsAppTheme

@ExperimentalAnimationApi
@Composable
fun LoginScreen(navController: NavController, mainVm: MainViewModel) {
    val vm: LoginViewModel = viewModel()
    if (!vm.hasInit) vm.init(mainVm)
    val usernameInput: String by vm.username.observeAsState("")

    val loading by vm.loading.observeAsState()
    val errorEvent by vm.errorEvent.observeAsState()

    Scaffold(
        topBar = { NoActionTopBar() },
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (loading == true) {
            LoadingOverlay()
        } else {
            LoginScreenContent(vm = vm, usernameInput = usernameInput, navController)
            errorEvent?.getContentIfNotHandled().let {
                if (it == true) {
                    Toast.makeText(
                        LocalContext.current,
                        stringResource(id = R.string.login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}


@ExperimentalAnimationApi
@Composable
fun LoginScreenContent(vm: LoginViewModel, usernameInput: String, navController: NavController) {
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.default_padding))) {
        Text(
            text = stringResource(id = R.string.login_title),
            style = MaterialTheme.typography.h2,
            modifier = Modifier
                .padding(bottom = 50.dp)
                .fillMaxWidth()
        )

        TextField(
            trailingIcon = {
                AnimatedVisibility(
                    visible = vm.username.value != "",
                    enter = fadeIn(0.1F),
                    exit = fadeOut(0.1F)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = stringResource(id = R.string.clear_text),
                        modifier = Modifier.clickable {
                            vm.onClearInputClick()
                        }
                    )
                }

            },
            value = usernameInput,
            onValueChange = { vm.onInputChange(it) },
            modifier = Modifier
                .padding(bottom = 50.dp)
                .fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.username_hint)) }
        )
        Button(
            onClick = {
                focusManager.clearFocus()
                vm.login(navController)
            },
            enabled = vm.username.value != "",
            modifier = Modifier
                .padding(bottom = 40.dp)
                .fillMaxWidth(),
        ) {
            Text(text = stringResource(id = R.string.login_title))
        }

        Button(
            onClick = {
                navController.navigate("register")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.register_title))
        }

    }
}

@ExperimentalAnimationApi
@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    RatingsAppTheme {
        LoginScreenContent(
            vm = LoginViewModel(),
            usernameInput = " ",
            navController = rememberNavController()
        )
    }

}

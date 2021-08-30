package com.example.ratingsapp.features.login_register

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ratingsapp.R
import com.example.ratingsapp.api.ApiHelper
import com.example.ratingsapp.api.JsonApiService
import com.example.ratingsapp.components.BackArrowTopBar
import com.example.ratingsapp.components.ColumnWithDefaultMargin
import com.example.ratingsapp.components.GenericClearableTextInput
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.repositories.MainRepository
import com.example.ratingsapp.ui.theme.RatingsAppTheme


@ExperimentalAnimationApi
@Composable
fun RegisterScreen(navController: NavController, mainVm: MainViewModel){
    val vm: RegisterViewModel = viewModel()
    vm.init(mainViewModel = mainVm)
    RegisterScreenContent(navController = navController, vm = vm)
}

@ExperimentalAnimationApi
@Composable
fun RegisterScreenContent(navController: NavController, vm: RegisterViewModel){
    Scaffold(
        topBar = { BackArrowTopBar(navController = navController) },
        modifier = Modifier
            .fillMaxSize()){
        ColumnWithDefaultMargin {
            Text(
                text = stringResource(id = R.string.register_title),
                style = MaterialTheme.typography.h2,
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .fillMaxWidth()
            )
            GenericClearableTextInput(
                hint = stringResource(id = R.string.firstname_hint),
                contentDescription = stringResource(id = R.string.firstname_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.default_padding)),
                inputValue = vm.firstname.observeAsState("").value,
                onChange = {vm.onFNInputChange(it)},
                onClear = { vm.clearFirstname() }
            )

            GenericClearableTextInput(
                hint = stringResource(id = R.string.lastname_hint),
                contentDescription = stringResource(id = R.string.lastname_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.default_padding)),
                inputValue = vm.lastname.observeAsState("").value,
                onChange = {vm.onLNInputChange(it)},
                onClear = {vm.clearLastname()}
            )
            GenericClearableTextInput(
                hint = stringResource(id = R.string.username_hint),
                contentDescription = stringResource(id = R.string.username_hint),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.default_padding_double)),
                inputValue = vm.username.observeAsState("").value,
                onChange = {vm.onUNInputChange(it)},
                onClear = {vm.clearUsername()}
            )

            Button(onClick = { /*TODO*/ }, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.register_title))
            }

        }
    }
}




@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {

    RatingsAppTheme {
        RegisterScreenContent(navController = rememberNavController(), vm = RegisterViewModel())
    }
}
package com.example.ratingsapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.ratingsapp.R
import com.example.ratingsapp.ui.theme.RatingsAppTheme


@Composable
fun ColumnWithDefaultMargin(composable: @Composable () -> Unit) {
    Column (modifier = Modifier
        .padding(dimensionResource(id = R.dimen.default_padding))
        .fillMaxSize()) {
        composable()
    }
}

@Composable
fun LoadingOverlay() {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview(){
    RatingsAppTheme {
        LoadingOverlay()
    }
}

@ExperimentalAnimationApi
@Composable
fun GenericClearableTextInput(
    hint: String,
    contentDescription: String,
    modifier: Modifier?,
    inputValue: String,
    onChange: (String) -> Unit,
    onClear: () -> Unit) {


    TextField(
        trailingIcon = {
            AnimatedVisibility(
                visible = inputValue != "",
                enter = fadeIn(0.1F),
                exit = fadeOut(0.1F)
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = contentDescription,
                    modifier = Modifier.clickable {
                        onClear()
                    }
                )
            }

        },
        value = inputValue,
        onValueChange = { onChange(it) },
        modifier = modifier ?: Modifier,
        label = { Text(text = hint) }
    )
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun ClearInput() {
    GenericClearableTextInput(inputValue = "", onChange = {}, onClear = {}, hint = "Username", contentDescription = "Content desciption", modifier = null)
}


package com.example.ratingsapp.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.ratingsapp.R
import com.example.ratingsapp.ui.theme.RatingsAppTheme


// Extension function to for making a clickable without it having the ripple effect
inline fun Modifier.noRippleClickable(crossinline onClick: () -> Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

@Composable
fun ColumnWithDefaultMargin(modifier: Modifier = Modifier, composable: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.default_padding))
            .fillMaxSize()
    ) {
        composable()
    }
}

@Composable
fun LoadingOverlay() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
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
    onClear: () -> Unit
) {
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
@Composable
fun SearchTextInput(
    hint: String,
    contentDescription: String,
    modifier: Modifier?,
    inputValue: String,
    onChange: (String) -> Unit,
    onClick: () -> Unit
) {
    TextField(
        trailingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = contentDescription,
                modifier = Modifier.clickable {
                    onClick()
                }
            )
        },
        value = inputValue,
        onValueChange = { onChange(it) },
        modifier = modifier ?: Modifier,
        label = { Text(text = hint) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Change soft keyboard to have have search icon
        keyboardActions = KeyboardActions(onSearch = { onClick() })        // onSearch can be used to handle the click event
    )
}

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun ClearInput() {
    GenericClearableTextInput(
        inputValue = "",
        onChange = {},
        onClear = {},
        hint = "Username",
        contentDescription = "Content desciption",
        modifier = null
    )
}


@Composable
fun AlertDlg(confirmAction: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = stringResource(id = R.string.logout_confirmation)) },
        confirmButton = {
            Button(
                onClick = { confirmAction() },
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() },
            ) {
                Text(text = stringResource(id = R.string.cancel))
            }
        }

    )
}

@Composable
fun LimitedTextInput(
    charLimit: Int,
    hint: String,
    modifier: Modifier,
    inputValue: String,
    onChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        TextField(
            value = inputValue,
            onValueChange = {
                if (it.length <= charLimit) {
                    onChange(it)
                }
            },
            label = { Text(text = hint) },
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = "${inputValue.length} / $charLimit",
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = dimensionResource(id = R.dimen.default_padding))
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLimitedInput() {
    RatingsAppTheme() {
        LimitedTextInput(
            charLimit = 50,
            hint = "Leave a Comment",
            inputValue = "",
            modifier = Modifier,
            onChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLimitedInput2() {
    RatingsAppTheme() {
        LimitedTextInput(
            charLimit = 50,
            hint = "Leave a Comment",
            inputValue = "jgfkdjskgjfdksgjkdflsjgkdfsjgkldfs",
            modifier = Modifier,
            onChange = {}
        )
    }
}


@Composable
fun DoneScreen(navController: NavController, closeDest: String, infoText: String? = null) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success_icon))
    val progress by animateLottieCompositionAsState(composition)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp, start = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
        ) {
            LottieAnimation(
                composition,
                progress,
            )
        }
        if (infoText != null) {
            Text(text = infoText, style = MaterialTheme.typography.h4)
        }

        /**
         * PopBackStack with the destination wouldn't work so we need do this
         * horrible thing to get the desired effect...
         */
        Button(
            onClick = {
                navController.popBackStack("main", false).also {
                    if (it && closeDest != "null") {
                        navController.navigate(closeDest)
                    }
                }

            },
            Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.close))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DonePreview() {
    RatingsAppTheme {
        DoneScreen(
            rememberNavController(),
            "dsadas",
            infoText = stringResource(id = R.string.review_thanks)
        )
    }
}

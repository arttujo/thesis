package com.example.ratingsapp.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ratingsapp.R
import com.example.ratingsapp.ui.theme.RatingsAppTheme

@Composable
fun ReloadSnackBar(reloadAction: () -> Unit) {
    Snackbar(backgroundColor = MaterialTheme.colors.error) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (icon, info, button) = createRefs()

            Icon(
                painter = painterResource(id = R.drawable.ic_info),
                contentDescription = "",
                Modifier
                    .padding(4.dp)
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )
            Text(
                text = stringResource(id = R.string.data_fetch_error),
                color = Color.Black,
                style = MaterialTheme.typography.body2,
                modifier = Modifier.constrainAs(info) {
                    start.linkTo(icon.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
            Button(onClick = reloadAction, modifier = Modifier.constrainAs(button) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }) {
                Text(text = stringResource(id = R.string.reload))
            }
        }
    }
}

@Preview
@Composable
fun SnackPreview() {
    RatingsAppTheme {
        ReloadSnackBar({})
    }
}
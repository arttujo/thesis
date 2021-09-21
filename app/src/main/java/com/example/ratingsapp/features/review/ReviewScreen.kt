package com.example.ratingsapp.features.review

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ratingsapp.R
import com.example.ratingsapp.components.*
import com.example.ratingsapp.features.details.InfoRow
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.models.Comment
import com.example.ratingsapp.models.Review
import com.example.ratingsapp.ui.theme.RatingsAppTheme


@Composable
fun ReviewScreen(mainViewModel: MainViewModel, navController: NavController, reviewId: Int?) {
    val vm:ReviewViewModel = viewModel()
    if(!vm.hasInit) {
        if(reviewId != null) {
            vm.init(mainViewModel, reviewId)
        }
    }
    val review by vm.review.observeAsState()
    val loading by vm.loading.observeAsState()

    Scaffold(topBar = { BackArrowTopBar(navController = navController , showLike = false, likeAction = {}, hasLiked = false )
    }) {

        if(loading==true) {
            LoadingOverlay()
        } else {
            if (review != null) {
                ReviewScreenContent(review = review!!, vm)
            }
        }
    }

}


@Composable
fun ReviewScreenContent(review: Review, vm: ReviewViewModel) {


    ColumnWithDefaultMargin {
        LazyColumn() {
            item {
                Column() {
                    Text(text = review.title, style = MaterialTheme.typography.h4,maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(
                        text = stringResource(id = R.string.likes_num,review.likes.toString()),
                        textAlign = TextAlign.Right)
                }
            }
            
            item {
                Text(text = review.body, style = MaterialTheme.typography.body1, modifier = Modifier.padding(top = 16.dp))
            }
            item {
                StaticRating(score = review.reviewScore, )
                Text(text = stringResource(id = R.string.review_by, review.authorUsername), style = MaterialTheme.typography.caption)
            }

            item {
                Text(text = stringResource(id = R.string.comments_title), style = MaterialTheme.typography.h4, modifier = Modifier.padding(top = 16.dp))
                LimitedTextInput(
                    charLimit = 25,
                    hint = stringResource(id = R.string.leave_comment),
                    modifier = Modifier.padding(top = 8.dp),
                    inputValue = vm.commentInput.observeAsState().value!!,
                    onChange = { vm.onInputChanged(it) }
                )
                Button(onClick = { vm.newComment() }, modifier = Modifier.padding(bottom = 8.dp), enabled = vm.commentInput.value?.length!! >= 1) {
                    Text(text = stringResource(id = R.string.send))
                }
            }
            if (!review.comments.isNullOrEmpty()) {
                items(review.comments) { comment ->
                    CommentRow(comment = comment)
                }
            }
        }
    }
}


@Composable
fun CommentRow(comment: Comment) {
    Card(
        shape = RoundedCornerShape(10),
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.padding(8.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp))  {
            Row {
                Text(text = stringResource(id = R.string.review_by, comment.author), style = MaterialTheme.typography.h5)
            }
            Row(modifier = Modifier.padding(top = 4.dp)) {
                Text(text = comment.comment,style = MaterialTheme.typography.body1)
            }
            Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(id = R.string.likes_num, comment.likes.toString()), style = MaterialTheme.typography.body1)
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Outlined.ThumbUp, contentDescription = stringResource(id = R.string.like), tint = Color.Black)
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CommentPreview() {
    RatingsAppTheme() {
        CommentRow(comment = Comment(1,1,"tester",1, "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati ", 342))
    }
}



@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    RatingsAppTheme {
        ReviewScreenContent(Review(1,1,1,"TEST", "TEST", "\"At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga.", 5,20), ReviewViewModel())
    }
}
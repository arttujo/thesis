package com.example.ratingsapp.features.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.ratingsapp.R

typealias ComposableFun = @Composable () -> Unit

sealed class TabItem(var icon:Int, var title: Int, var screen: ComposableFun) {
    object Home: TabItem(R.drawable.ic_home, R.string.home, { HomeScreen()} )
    object Search: TabItem(R.drawable.ic_search, R.string.search, { SearchScreen()})
    object Profile: TabItem(R.drawable.ic_profile, R.string.profile, { ProfileScreen()})
}




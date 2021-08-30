package com.example.ratingsapp.features.main

import androidx.compose.runtime.Composable
import com.example.ratingsapp.R

typealias ComposableFun = @Composable () -> Unit

data class TabItem(var icon:Int, var title: Int, var vm: MainViewModel, var screen: ComposableFun)



package com.example.ratingsapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.TabRowDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ratingsapp.components.MainTopBar
import com.example.ratingsapp.features.login_register.LoginScreen
import com.example.ratingsapp.features.login_register.LoginViewModel
import com.example.ratingsapp.features.login_register.RegisterScreen
import com.example.ratingsapp.features.main.MainViewModel
import com.example.ratingsapp.features.main.TabItem
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")



@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ApplicationRoot() {
    val navController = rememberNavController()
    NavHost(navController = navController , startDestination = "login" ) {
        composable("login") { LoginScreen(navController)}
        composable("register") { RegisterScreen() }
        composable("main") { MainScreen()}
    }
}




class MainActivity : ComponentActivity() {


    @ExperimentalAnimationApi
    @ExperimentalPagerApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            RatingsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ApplicationRoot()
                }
            }
        }
    }
    private fun logOut(){}

}


@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun MainScreen() {
    val tabs = listOf(
        TabItem.Home,
        TabItem.Search,
        TabItem.Profile
    )
    val pagerState = rememberPagerState(pageCount = tabs.size)
    Scaffold(
        topBar = { MainTopBar {
            //TODO logout click
        }}
    ) {
        Column {
            Tabs(tabs = tabs, pagerState = pagerState)
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }
}


@ExperimentalPagerApi
@Composable
fun TabsContent(tabs:List<TabItem>, pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        tabs[page].screen()
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun Tabs(tabs: List<TabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.Black,
        indicator = { tabPos ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState,tabPos)
            )
        }
    ) {
        tabs.forEachIndexed { index, tabItem ->
            LeadingIconTab(
                selected = pagerState.currentPage == index,
                text = { stringResource(id = tabItem.title) },
                icon = {
                    Icon(
                        painter = painterResource(id = tabItem.icon),
                        contentDescription = stringResource(
                            id = tabItem.title
                        )
                    )
                },
                onClick = {
                          scope.launch {
                              pagerState.animateScrollToPage(index)
                          }
                },
            )
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun Main() {
    RatingsAppTheme {
        MainScreen()
    }
}


@Composable
fun RatingsApp(){
    val navController = rememberNavController()
    RatingsAppTheme {
        NavHost(navController = navController, startDestination = "test") {

        }
    }
}





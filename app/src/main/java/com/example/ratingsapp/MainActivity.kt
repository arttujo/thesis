package com.example.ratingsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material.TabRowDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.ratingsapp.api.ApiHelper
import com.example.ratingsapp.api.RetrofitBuilder
import com.example.ratingsapp.components.AlertDlg
import com.example.ratingsapp.components.MainTopBar
import com.example.ratingsapp.features.login_register.LoginScreen
import com.example.ratingsapp.features.login_register.RegisterScreen
import com.example.ratingsapp.features.main.*
import com.example.ratingsapp.repositories.MainRepository
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException



@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ApplicationRoot(mainVm: MainViewModel) {
    val navController = rememberNavController()
    RatingsAppTheme() {
        NavHost(navController = navController , startDestination = "login" ) {
            composable("login") { LoginScreen(navController, mainVm)}
            composable("register") { RegisterScreen(navController, mainVm) }
            composable("main") { MainScreen(navController,mainVm)}
        }
    }
}




class MainActivity : ComponentActivity() {

    lateinit var viewModel: MainViewModel
    private fun setupVm(){
        viewModel = ViewModelProvider(this,ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(MainViewModel::class.java)
    }


    @ExperimentalCoilApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupVm()
        setContent {
            RatingsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ApplicationRoot(viewModel)
                }
            }
        }
    }

}

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown Class Name")
    }
}



@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun MainScreen(navController: NavHostController, mainVm: MainViewModel) {
    val home = TabItem(R.drawable.ic_home,R.string.home, mainVm) { HomeScreen(mainVm) }
    val search = TabItem(R.drawable.ic_search, R.string.search,mainVm) { SearchScreen(mainVm)}
    val profile = TabItem(R.drawable.ic_profile, R.string.profile, mainVm) { ProfileScreen(mainVm)}
    val tabs = listOf(home,search,profile)
    val pagerState = rememberPagerState(pageCount = tabs.size)

    val logoutEvent by mainVm.logoutEvent.observeAsState()
    var showDialog by remember { mutableStateOf(false) }

    logoutEvent?.getContentIfNotHandled().let {
        if (it != null) {
            showDialog = true
        }
    }
    BackHandler(onBack = {
        if (navController.previousBackStackEntry == null) {
            showDialog = true
        }
    })

    Scaffold(
        topBar = { MainTopBar {
            showDialog = true
        }}
    ) {

        if (showDialog) {
            AlertDlg(confirmAction = { mainVm.logout(navController) }, onDismiss = {showDialog = false})
        }

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

@Composable
fun RatingsApp(){
    val navController = rememberNavController()
    RatingsAppTheme {
        NavHost(navController = navController, startDestination = "test") {

        }
    }
}





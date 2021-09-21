package com.example.ratingsapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.ratingsapp.api.ApiHelper
import com.example.ratingsapp.api.RawgApiHelper
import com.example.ratingsapp.api.RawgBuilder
import com.example.ratingsapp.api.RetrofitBuilder
import com.example.ratingsapp.components.AlertDlg
import com.example.ratingsapp.components.DoneScreen
import com.example.ratingsapp.components.MainTopBar
import com.example.ratingsapp.features.details.GameDetailsScreen
import com.example.ratingsapp.features.details.RawgGameDetailsScreen
import com.example.ratingsapp.features.login_register.LoginScreen
import com.example.ratingsapp.features.login_register.RegisterScreen
import com.example.ratingsapp.features.main.*
import com.example.ratingsapp.features.review.NewReviewScreen
import com.example.ratingsapp.features.review.ReviewScreen
import com.example.ratingsapp.repositories.MainRepository
import com.example.ratingsapp.repositories.RawgRepository
import com.example.ratingsapp.ui.theme.RatingsAppTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch


const val GAME_ID = "GAME_ID"
const val REVIEW_ID = "REVIEW_ID"
const val NAV_DEST = "NAV_DEST"
const val FORCE_RELOAD = "FORCE_RELOAD"
const val INFO_TEXT = "INFO_TEXT"

@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun ApplicationRoot(mainVm: MainViewModel) {
    val navController = rememberAnimatedNavController()

    val login = mainVm.loggedInAs.observeAsState()

    val startDestination = remember { mutableStateOf("login") }
    Log.d("DBGL", "LOGGED IN: ${login.value}")

    RatingsAppTheme() {
        if (login.value != null) {
            startDestination.value = "main"
        }
        AnimatedNavHost(navController = navController, startDestination = startDestination.value) {
            composable("login") { LoginScreen(navController, mainVm) }
            composable("register") { RegisterScreen(navController, mainVm) }
            composable("main") { MainScreen(navController, mainVm) }


            composable(
                enterTransition = { _, _ ->
                    slideInVertically(initialOffsetY = { 3000 }, animationSpec = tween(200))
                },
                popEnterTransition = { _, _ ->
                    slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(200))
                },

                popExitTransition = { _, _ ->
                    slideOutVertically(targetOffsetY = { 3000 }, animationSpec = tween(200))
                },
                route = "gameDetails/{$GAME_ID}",
                arguments = listOf(navArgument(GAME_ID) { type = NavType.IntType })
            ) {
                GameDetailsScreen(
                    mainViewModel = mainVm,
                    navController = navController,
                    gameId = it.arguments?.getInt(GAME_ID)
                )
            }

            composable(
                enterTransition = { _, _ ->
                    slideInVertically(initialOffsetY = { 1000 }, animationSpec = tween(200))
                },

                exitTransition = { _, _ ->
                    slideOutVertically(targetOffsetY = { 5000 }, animationSpec = tween(200))
                },
                route = "rawgGameDetails/{$GAME_ID}",
                arguments = listOf(navArgument(GAME_ID) { type = NavType.IntType })
            ) {
                RawgGameDetailsScreen(
                    mainViewModel = mainVm,
                    navController = navController,
                    gameId = it.arguments?.getInt(GAME_ID)
                )
            }

            composable(
                enterTransition = { _, _ ->
                    slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(200))
                },
                route = "reviewDetails/{$REVIEW_ID}",
                arguments = listOf(navArgument(REVIEW_ID) { type = NavType.IntType })
            ) {
                ReviewScreen(
                    mainViewModel = mainVm,
                    navController = navController,
                    reviewId = it.arguments?.getInt(REVIEW_ID)
                )
            }
            composable(
                enterTransition = { _, _ ->
                    slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(200))
                },
                route = "newReview/{$GAME_ID}",
                arguments = listOf(navArgument(GAME_ID) { type = NavType.IntType })
            ) {
                NewReviewScreen(
                    mainViewModel = mainVm,
                    navController = navController,
                    gameId = it.arguments?.getInt(GAME_ID)
                )
            }
            composable("done?navDest={$NAV_DEST}&infoText={$INFO_TEXT}", arguments = listOf(
                navArgument(NAV_DEST) { type = NavType.StringType },
                navArgument(INFO_TEXT) { type = NavType.StringType }

            )) {
                DoneScreen(
                    navController = navController,
                    closeDest = it.arguments?.getString(NAV_DEST)!!,
                    infoText = it.arguments?.getString(INFO_TEXT)!!,
                )
            }

        }
    }
}

// return correctly formatted argument string for the done screen
fun createDoneScreen(navDest: String, infoText: String) = "done?navDest=$navDest&infoText=$infoText"

const val ARG_DATA_STORE = "ARG_DATA_STORE"
val Context.prefsDataStore by preferencesDataStore(name = ARG_DATA_STORE) //preference datastore

object PreferenceKeys {
    val LOGGED_IN_AUTHOR = stringPreferencesKey("LOGGED_IN_AUTHOR")
}

class MainActivity : ComponentActivity() {

    @ExperimentalCoilApi
    @ExperimentalFoundationApi
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalPagerApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                applicationContext.prefsDataStore,
                RawgApiHelper(RawgBuilder.apiService)
            )
        )
            .get(MainViewModel::class.java)

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

class ViewModelFactory(
    private val apiHelper: ApiHelper,
    private val dataStore: DataStore<Preferences>,
    private val rawgApiHelper: RawgApiHelper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(
                MainRepository(apiHelper),
                dataStore,
                RawgRepository(rawgApiHelper)
            ) as T
        }
        throw IllegalArgumentException("Unknown Class Name")
    }
}


@ExperimentalAnimationApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun MainScreen(navController: NavHostController, mainVm: MainViewModel) {
    val home =
        TabItem(R.drawable.ic_home, R.string.home, mainVm) { HomeScreen(mainVm, navController) }
    val search = TabItem(R.drawable.ic_search, R.string.search, mainVm) {
        SearchScreen(
            mainVm,
            navController
        )
    }
    val profile = TabItem(R.drawable.ic_profile, R.string.profile, mainVm) {
        ProfileScreen(
            mainVm,
            navController
        )
    }
    val tabs = listOf(home, search, profile)
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
        topBar = {
            MainTopBar {
                showDialog = true
            }
        }
    ) {

        if (showDialog) {
            AlertDlg(
                confirmAction = { mainVm.logout(navController) },
                onDismiss = { showDialog = false })
        }

        Column {
            Tabs(tabs = tabs, pagerState = pagerState)
            TabsContent(tabs = tabs, pagerState = pagerState)
        }
    }
}


@ExperimentalPagerApi
@Composable
fun TabsContent(tabs: List<TabItem>, pagerState: PagerState) {
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
                Modifier.pagerTabIndicatorOffset(pagerState, tabPos)
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
fun RatingsApp() {
    val navController = rememberNavController()
    RatingsAppTheme {
        NavHost(navController = navController, startDestination = "test") {

        }
    }
}





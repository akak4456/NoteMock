package com.jo.notemock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jo.notemock.screen.CalendarScreen
import com.jo.notemock.screen.MyScreen
import com.jo.notemock.screen.NoteListScreen
import com.jo.notemock.screen.SearchScreen
import com.jo.notemock.ui.theme.NoteMockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteMockTheme {
                // A surface container using the 'background' color from the theme
                MainScreenView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenView() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavigationGraph(navController = navController)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.NoteList,
        BottomNavItem.Calendar,
        BottomNavItem.Search,
        BottomNavItem.My,
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(selected =
            currentRoute == item.screenRoute, onClick = {
                navController.navigate(item.screenRoute) {
                    popUpTo(navController.graph.startDestinationId)
                    launchSingleTop = true
                }
            }, icon = {
                Image(
                    painter = painterResource(
                        id =
                        if (currentRoute == item.screenRoute) {
                            item.iconWhenOn
                        } else {
                            item.iconWhenOff
                        }
                    ), contentDescription = ""
                )
            })
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavItem.NoteList.screenRoute) {
        composable(BottomNavItem.NoteList.screenRoute) {
            NoteListScreen()
        }
        composable(BottomNavItem.Calendar.screenRoute) {
            CalendarScreen()
        }
        composable(BottomNavItem.Search.screenRoute) {
            SearchScreen()
        }
        composable(BottomNavItem.My.screenRoute) {
            MyScreen()
        }
    }
}

const val NOTE_LIST = "NOTE_LIST"
const val CALENDAR = "CALENDAR"
const val SEARCH = "SEARCH"
const val MY = "My"

sealed class BottomNavItem(
    val iconWhenOff: Int,
    val iconWhenOn: Int,
    val screenRoute: String
) {
    object NoteList : BottomNavItem(
        R.drawable.ic_note_list_bottom_navigation_off,
        R.drawable.ic_note_list_bottom_navigation_on,
        NOTE_LIST
    )

    object Calendar : BottomNavItem(
        R.drawable.ic_calendar_bottom_navigation_off,
        R.drawable.ic_calendar_bottom_navigation_on,
        CALENDAR
    )

    object Search : BottomNavItem(
        R.drawable.ic_search_bottom_navigation_off,
        R.drawable.ic_search_bottom_navigation_on,
        SEARCH
    )

    object My : BottomNavItem(
        R.drawable.ic_my_bottom_navigation_off,
        R.drawable.ic_my_bottom_navigation_on,
        MY
    )
}
package com.example.caseapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.caseapp.ui.detail.DetailScreen
import com.example.caseapp.ui.home.HomeScreen
import com.example.caseapp.utils.ScreenRoutes
import com.example.caseapp.utils.theme.CaseAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaseAppTheme {
                val navController = rememberNavController()

                // navigation yapısında okunabilirliği arttırmak adına yapılmış bir value, bütün actionları daha düzenli yönetmek için yapıldı.
                val navigation: (String) -> Unit = { route ->
                    if (route == "-1") {
                        navController.popBackStack()
                    } else {
                        navController.navigate(route)
                    }
                }
                NavHost(navController = navController,
                    startDestination = ScreenRoutes.List)
                {
                    composable(ScreenRoutes.List) {
                        HomeScreen(openDetail = {
                            navigation(it)
                        })
                    }

                    composable(
                        route = ScreenRoutes.Detail,
                        arguments =  listOf(
                            navArgument("id") {
                                type = NavType.StringType
                            },
                        )
                    ) { entry ->
                        val id = entry.arguments?.getString("id")
                        if (id != null) {
                            DetailScreen(onBackPressed = {
                                navigation(it)
                            })
                        }

                    }
                }
            }
        }
    }
}
package com.example.caseapp.ui

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CaseAppTheme {


                val navController = rememberNavController()

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
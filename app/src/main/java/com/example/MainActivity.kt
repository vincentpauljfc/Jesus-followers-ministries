package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.data.local.AppDatabase
import com.example.data.repository.JfcRepository
import com.example.ui.screens.JfcLoginScreen
import com.example.ui.screens.JfcMainScaffold
import com.example.ui.screens.JfcSplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.JfcViewModel
import com.example.ui.viewmodel.JfcViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enforce mandated Edge To Edge safe drawing
        enableEdgeToEdge()

        // Initialize local Room Database and Repository
        val database = AppDatabase.getDatabase(this)
        val repository = JfcRepository(database.jfcDao())
        
        // Formulate view model factory
        val factory = JfcViewModelFactory(application, repository)

        setContent {
            val viewModel: JfcViewModel by viewModels { factory }
            
            // Orchestrate application launch cycles (Splash -> Login / Guest skip -> Dashboard)
            var showSplash by remember { mutableStateOf(true) }

            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    when {
                        showSplash -> {
                            JfcSplashScreen(onSplashComplete = { showSplash = false })
                        }
                        !viewModel.isUserLoggedIn -> {
                            JfcLoginScreen(viewModel = viewModel)
                        }
                        else -> {
                            JfcMainScaffold(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

package com.example.todolistb
/*
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolistb.ui.theme.TodoListBTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListBTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoListBTheme {
        Greeting("Android")
    }
}

*/

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistb.settings.SettingsViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolistb.AddTaskScreen
import com.example.todolistb.Routes
import com.example.todolistb.TaskListScreen
import com.example.todolistb.TaskViewModel
import com.example.todolistb.ui.theme.TodoListBTheme // Usa el theme que creó tu plantilla; ajusta el import

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsVm: SettingsViewModel = viewModel()
            val settingsState = settingsVm.state.collectAsState()
            TodoListBTheme(darkTheme = settingsState.value.darkTheme) {
                val nav = rememberNavController()
                val vm: TaskViewModel = viewModel()
                NavHost(navController = nav, startDestination = if (settingsState.value.hasUsername) Routes.LIST else Routes.WELCOME) {
                    composable(Routes.WELCOME) {
                        com.example.todolistb.ui.welcome.WelcomeScreen(
                            settingsState = settingsState.value,
                            onSave = { name, dark ->
                                settingsVm.updateUsername(name)
                                settingsVm.updateTheme(dark)
                                nav.navigate(Routes.LIST) {
                                    popUpTo(Routes.WELCOME) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Routes.LIST) {
                        TaskListScreen(
                            tasks = vm.tasks,
                            onToggle = { id, checked -> vm.toggleTaskDone(id, checked) },
                            onAddClick = { nav.navigate(Routes.ADD) }
                        )
                    }
                    composable(Routes.ADD) {
                        AddTaskScreen(
                            onSave = { title, desc ->
                                vm.addTask(title, desc)
                                nav.popBackStack()
                            },
                            onCancel = { nav.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
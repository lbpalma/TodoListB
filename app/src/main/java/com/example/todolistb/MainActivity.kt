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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolistb.settings.SettingsViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolistb.Routes
import com.example.todolistb.notes.NoteListScreen
import com.example.todolistb.notes.AddOrEditNoteScreen
import com.example.todolistb.notes.NotaViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.todolistb.ui.theme.TodoListBTheme // Usa el theme que creó tu plantilla; ajusta el import

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val settingsVm: SettingsViewModel = viewModel()
            val settingsState = settingsVm.state.collectAsState()
            TodoListBTheme(darkTheme = settingsState.value.darkTheme) {
                val nav = rememberNavController()
                val notaVm: NotaViewModel = viewModel()
                NavHost(navController = nav, startDestination = if (settingsState.value.hasUsername) Routes.NOTES_LIST else Routes.WELCOME) {
                    composable(Routes.WELCOME) {
                        com.example.todolistb.ui.welcome.WelcomeScreen(
                            settingsState = settingsState.value,
                            onSave = { name, dark ->
                                settingsVm.updateUsername(name)
                                settingsVm.updateTheme(dark)
                                nav.navigate(Routes.NOTES_LIST) {
                                    popUpTo(Routes.WELCOME) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Routes.NOTES_LIST) {
                        val notas by notaVm.notas.collectAsState()
                        val contador by notaVm.contador.collectAsState()
                        NoteListScreen(
                            notas = notas,
                            contador = contador,
                            onAddClick = { nav.navigate(Routes.NOTE_ADD) },
                            onOpen = { nota -> nav.navigate(Routes.NOTE_EDIT + "/${nota.id}") },
                            onDelete = { notaVm.eliminar(it) },
                            onFilterCategoria = { cat -> notaVm.setCategoriaFiltro(cat) },
                            onSearch = { q -> notaVm.setQuery(q) }
                        )
                    }
                    composable(Routes.NOTE_ADD) {
                        AddOrEditNoteScreen(
                            onSave = { t, c, cat, col ->
                                notaVm.crear(t, c, cat, col)
                                nav.popBackStack()
                            },
                            onCancel = { nav.popBackStack() }
                        )
                    }
                    composable(
                        route = Routes.NOTE_EDIT + "/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getInt("id") ?: -1
                        val notaState = notaVm.getNota(id).collectAsState(initial = null)
                        val nota = notaState.value
                        if (nota != null) {
                            AddOrEditNoteScreen(
                                nota = nota,
                                onSave = { t, c, cat, col ->
                                    notaVm.actualizar(nota, t, c, cat, col)
                                    nav.popBackStack()
                                },
                                onCancel = { nav.popBackStack() },
                                onDelete = {
                                    notaVm.eliminar(nota)
                                    nav.popBackStack()
                                }
                            )
                        } else {
                            // Podrías mostrar un indicador de carga o mensaje de error
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
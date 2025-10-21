package com.example.todolistb.ui.welcome

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.todolistb.settings.SettingsState

@Composable
fun WelcomeScreen(
    settingsState: SettingsState,
    onSave: (name: String, darkTheme: Boolean) -> Unit
) {
    var name by remember(settingsState.username) { mutableStateOf(settingsState.username ?: "") }
    var dark by remember(settingsState.darkTheme) { mutableStateOf(settingsState.darkTheme) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (settingsState.hasUsername) {
            Text(text = "Hola, ${settingsState.username}!", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { onSave(settingsState.username ?: "", dark) }) { Text("Continuar") }
        } else {
            Text(text = "Bienvenido", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("¿Cuál es tu nombre?") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Tema:")
                Spacer(modifier = Modifier.width(8.dp))
                SegmentedButton(
                    options = listOf("Claro", "Oscuro"),
                    selected = if (dark) 1 else 0,
                    onOptionSelected = { idx -> dark = idx == 1 }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onSave(name, dark) },
                enabled = name.isNotBlank()
            ) { Text("Guardar y continuar") }
        }
    }
}

@Composable
fun SegmentedButton(options: List<String>, selected: Int, onOptionSelected: (Int) -> Unit) {
    Row {
        options.forEachIndexed { idx, text ->
            Button(
                onClick = { onOptionSelected(idx) },
                colors = if (selected == idx) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors(),
                modifier = Modifier.padding(horizontal = 2.dp)
            ) { Text(text) }
        }
    }
}

package com.example.todolistb.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.todolistb.data.Nota

private val coloresPredef = listOf(
    "#FFCDD2", "#BBDEFB", "#C8E6C9", "#FFF9C4", "#D1C4E9"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrEditNoteScreen(
    nota: Nota? = null,
    onSave: (titulo: String, contenido: String, categoria: String, color: String) -> Unit,
    onCancel: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    var titulo by remember(nota) { mutableStateOf(TextFieldValue(nota?.titulo ?: "")) }
    var contenido by remember(nota) { mutableStateOf(TextFieldValue(nota?.contenido ?: "")) }
    var categoria by remember(nota) { mutableStateOf(nota?.categoria ?: "Personal") }
    var colorSel by remember(nota) { mutableStateOf(nota?.color ?: coloresPredef.first()) }
    var titleError by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (nota == null) "Nueva nota" else "Editar nota") },
                navigationIcon = {
                    IconButton(onClick = onCancel) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                },
                actions = {
                    if (nota != null && onDelete != null) {
                        TextButton(onClick = onDelete) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
                    }
                }
            )
        }
    ) { inner ->
        Column(Modifier.padding(inner).padding(16.dp)) {
            OutlinedTextField(
                value = titulo,
                onValueChange = {
                    titulo = it
                    if (titleError != null && it.text.isNotBlank()) titleError = null
                },
                label = { Text("Título") },
                isError = titleError != null,
                supportingText = { if (titleError != null) Text(titleError!!) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = contenido,
                onValueChange = { contenido = it },
                label = { Text("Contenido") },
                modifier = Modifier.fillMaxWidth().heightIn(min = 160.dp)
            )
            Spacer(Modifier.height(12.dp))
            // Categoría
            var expandedCat by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expandedCat, onExpandedChange = { expandedCat = !expandedCat }) {
                TextField(
                    readOnly = true,
                    value = categoria,
                    onValueChange = {},
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCat) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expandedCat, onDismissRequest = { expandedCat = false }) {
                    listOf("Personal", "Trabajo", "Estudio").forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = { categoria = cat; expandedCat = false }
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Color")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                coloresPredef.forEach { c ->
                    val col = try { Color(android.graphics.Color.parseColor(c)) } catch (e: Exception) { Color.LightGray }
                    Box(
                        modifier = Modifier
                            .size(if (c == colorSel) 40.dp else 32.dp)
                            .clip(CircleShape)
                            .background(col)
                            .clickable { colorSel = c }
                            .border(width = if (c == colorSel) 3.dp else 1.dp, color = if (c == colorSel) MaterialTheme.colorScheme.primary else Color.Gray, shape = CircleShape)
                    )
                }
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    val t = titulo.text.trim()
                    if (t.isBlank()) {
                        titleError = "El título es obligatorio"
                        return@Button
                    }
                    onSave(t, contenido.text.trim(), categoria, colorSel)
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Guardar") }
        }
    }
}

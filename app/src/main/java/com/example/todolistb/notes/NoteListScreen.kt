package com.example.todolistb.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.todolistb.data.Nota

private val coloresPredef = listOf(
    "#FFCDD2", "#BBDEFB", "#C8E6C9", "#FFF9C4", "#D1C4E9"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    notas: List<Nota>,
    contador: Int,
    onAddClick: () -> Unit,
    onOpen: (Nota) -> Unit,
    onDelete: (Nota) -> Unit,
    onFilterCategoria: (String?) -> Unit,
    onSearch: (String) -> Unit
) {
    var search by remember { mutableStateOf("") }
    var expandedFiltro by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember { mutableStateOf<String?>(null) }

    var notaAEliminar by remember { mutableStateOf<Nota?>(null) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text("Notas ($contador)") })
                OutlinedTextField(
                    value = search,
                    onValueChange = {
                        search = it
                        onSearch(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    placeholder = { Text("Buscar por título") },
                    singleLine = true
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expandedFiltro,
                        onExpandedChange = { expandedFiltro = !expandedFiltro }
                    ) {
                        TextField(
                            readOnly = true,
                            value = categoriaSeleccionada ?: "Todas las categorías",
                            onValueChange = {},
                            label = { Text("Categoría") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFiltro) },
                            modifier = Modifier.menuAnchor().weight(1f)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedFiltro,
                            onDismissRequest = { expandedFiltro = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Todas") },
                                onClick = {
                                    categoriaSeleccionada = null
                                    onFilterCategoria(null)
                                    expandedFiltro = false
                                }
                            )
                            listOf("Personal", "Trabajo", "Estudio").forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat) },
                                    onClick = {
                                        categoriaSeleccionada = cat
                                        onFilterCategoria(cat)
                                        expandedFiltro = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar nota")
            }
        }
    ) { inner ->
        if (notas.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(inner), contentAlignment = Alignment.Center) {
                Text("No hay notas. Pulsa + para agregar.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(notas, key = { it.id }) { nota ->
                    NotaCard(nota = nota, onClick = { onOpen(nota) }, onDelete = { notaAEliminar = nota })
                }
            }
        }
    }

    if (notaAEliminar != null) {
        AlertDialog(
            onDismissRequest = { notaAEliminar = null },
            title = { Text("Eliminar nota") },
            text = { Text("¿Seguro que deseas eliminar esta nota? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    notaAEliminar?.let { onDelete(it) }
                    notaAEliminar = null
                }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { notaAEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun NotaCard(nota: Nota, onClick: () -> Unit, onDelete: () -> Unit) {
    val bg = try { Color(android.graphics.Color.parseColor(nota.color)) } catch (e: Exception) { Color.LightGray }
    Surface(shadowElevation = 1.dp, shape = MaterialTheme.shapes.medium, modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
        Column(Modifier.background(bg).padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(nota.titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Eliminar") }
            }
            Text(nota.contenido.take(80) + if (nota.contenido.length > 80) "..." else "", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(6.dp))
            Text(nota.categoria + " • " + nota.fechaCreacion + (nota.fechaModificacion?.let { " • Mod: $it" } ?: ""), style = MaterialTheme.typography.labelSmall)
        }
    }
}

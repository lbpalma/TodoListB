package com.example.todolistb.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistb.data.AppDatabase
import com.example.todolistb.data.Nota
import com.example.todolistb.data.NotaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotaViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = NotaRepository(AppDatabase.getInstance(app).notaDao())

    private val categoriaFiltro = MutableStateFlow<String?>(null)
    private val queryBusqueda = MutableStateFlow("")

    private val baseFlow: Flow<List<Nota>> = repo.notasTodas()

    val notas: StateFlow<List<Nota>> = combine(baseFlow, categoriaFiltro, queryBusqueda) { lista, cat, q ->
        lista.filter { nota ->
            (cat == null || nota.categoria == cat) &&
            (q.isBlank() || nota.titulo.contains(q, ignoreCase = true))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val contador: StateFlow<Int> = repo.contar().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun getNota(id: Int): Flow<Nota?> = repo.notaPorId(id)

    fun setCategoriaFiltro(cat: String?) { categoriaFiltro.value = cat }
    fun setQuery(q: String) { queryBusqueda.value = q }

    fun crear(titulo: String, contenido: String, categoria: String, color: String) {
        viewModelScope.launch { repo.crear(titulo, contenido, categoria, color) }
    }

    fun actualizar(nota: Nota, titulo: String, contenido: String, categoria: String, color: String) {
        viewModelScope.launch { repo.actualizar(nota, titulo, contenido, categoria, color) }
    }

    fun eliminar(nota: Nota) {
        viewModelScope.launch { repo.eliminar(nota) }
    }
}

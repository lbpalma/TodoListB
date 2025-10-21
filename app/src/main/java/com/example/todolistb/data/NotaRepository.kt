package com.example.todolistb.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NotaRepository(private val dao: NotaDao) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    fun notasTodas(): Flow<List<Nota>> = dao.obtenerTodas()
    fun notasPorCategoria(cat: String): Flow<List<Nota>> = dao.obtenerPorCategoria(cat)
    fun buscarPorTitulo(query: String): Flow<List<Nota>> = dao.buscarPorTitulo(query)
    fun notaPorId(id: Int): Flow<Nota?> = dao.obtenerPorId(id)
    fun contar(): Flow<Int> = dao.contar()

    suspend fun crear(
        titulo: String,
        contenido: String,
        categoria: String,
        color: String
    ) {
        val ahora = LocalDateTime.now().format(dateFormatter)
        dao.insertar(
            Nota(
                titulo = titulo.trim(),
                contenido = contenido.trim(),
                categoria = categoria,
                fechaCreacion = ahora,
                fechaModificacion = null,
                color = color
            )
        )
    }

    suspend fun actualizar(nota: Nota, nuevoTitulo: String, nuevoContenido: String, nuevaCategoria: String, nuevoColor: String) {
        val ahora = LocalDateTime.now().format(dateFormatter)
        dao.actualizar(
            nota.copy(
                titulo = nuevoTitulo.trim(),
                contenido = nuevoContenido.trim(),
                categoria = nuevaCategoria,
                color = nuevoColor,
                fechaModificacion = ahora
            )
        )
    }

    suspend fun eliminar(nota: Nota) = dao.eliminar(nota)
}

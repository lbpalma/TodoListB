package com.example.todolistb.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDao {
    @Query("SELECT * FROM notas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<Nota>>

    @Query("SELECT * FROM notas WHERE categoria = :cat ORDER BY id DESC")
    fun obtenerPorCategoria(cat: String): Flow<List<Nota>>

    @Query("SELECT * FROM notas WHERE titulo LIKE '%' || :query || '%' ORDER BY id DESC")
    fun buscarPorTitulo(query: String): Flow<List<Nota>>

    @Query("SELECT * FROM notas WHERE id = :id")
    fun obtenerPorId(id: Int): Flow<Nota?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(nota: Nota): Long

    @Update
    suspend fun actualizar(nota: Nota)

    @Delete
    suspend fun eliminar(nota: Nota)

    @Query("SELECT COUNT(*) FROM notas")
    fun contar(): Flow<Int>
}

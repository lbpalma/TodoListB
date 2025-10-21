package com.example.todolistb.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notas")
data class Nota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val contenido: String,
    val categoria: String, // Personal, Trabajo, Estudio
    val fechaCreacion: String,
    val fechaModificacion: String? = null,
    val color: String // puede ser un hex (#FFAA00) o nombre
)

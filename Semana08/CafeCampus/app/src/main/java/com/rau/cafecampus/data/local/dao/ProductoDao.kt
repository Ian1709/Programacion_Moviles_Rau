package com.rau.cafecampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rau.cafecampus.data.local.entity.Producto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos WHERE disponible = 1")
    fun obtenerTodosDisponibles(): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE categoria = :categoria AND disponible = 1")
    fun obtenerPorCategoria(categoria: String): Flow<List<Producto>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Producto?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(productos: List<Producto>)
}
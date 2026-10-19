package com.rau.cafecampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pedidos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["clienteId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("clienteId")]
)
data class Pedido(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clienteId: Int,
    val estado: String,
    val horaRecojo: String,
    val pago: String,
    val nota: String = "",
    val total: Double,
    val fecha: Long = System.currentTimeMillis()
)
package com.rau.cafecampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "detalle_pedido",
    foreignKeys = [
        ForeignKey(
            entity = Pedido::class,
            parentColumns = ["id"],
            childColumns = ["pedidoId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index("pedidoId")]
)
data class DetallePedido(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pedidoId: Long,
    val productoId: Int,
    val nombreProducto: String,
    val cantidad: Int,
    val precioUnitario: Double
)
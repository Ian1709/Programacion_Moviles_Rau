package com.rau.cafecampus.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rau.cafecampus.data.local.relation.PedidoConDetalle
import com.rau.cafecampus.ui.theme.NaranjaEstudiante
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PedidoCard(
    pedidoConDetalle: PedidoConDetalle,
    onClick: () -> Unit,
    onRepetirPedido: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val pedido = pedidoConDetalle.pedido
    val detalles = pedidoConDetalle.detalles

    val formatoFecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val fechaFormateada = formatoFecha.format(Date(pedido.fecha))

    val resumenDetalle = if (detalles.isNotEmpty()) {
        val primerDetalle = "${detalles.first().nombreProducto} x${detalles.first().cantidad}"
        if (detalles.size > 1) "$primerDetalle + ${detalles.size - 1} más" else primerDetalle
    } else ""

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #%04d".format(pedido.id),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text("›", fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = resumenDetalle,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = fechaFormateada,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EstadoChip(estado = pedido.estado)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (onRepetirPedido != null) {
                        TextButton(
                            onClick = onRepetirPedido,
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                        ) {
                            Text("🔄 Repetir", color = NaranjaEstudiante, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    Text(
                        text = "S/ %.2f".format(pedido.total),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = NaranjaEstudiante
                    )
                }
            }
        }
    }
}

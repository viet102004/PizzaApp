package com.example.pizza_app.ui.order

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizza_app.data.model.Order


@Composable
fun OrderList(orders: List<Order>) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        orders.forEach { order ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(order.storeName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(order.status, color = Color.Red, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(order.orderTime, fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(8.dp))
                    order.items.forEach {
                        Text("• $it", fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Tổng tiền: ${order.totalPrice}", fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = { /* TODO: Go to detail */ }) {
                        Text("Xem chi tiết", fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

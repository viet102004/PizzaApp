package com.example.pizza_app.ui.profile

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.*


@Composable
fun UpdateDOBScreen(navController: NavController) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("19 tháng 10, 2004") }
    val showDatePicker = remember { mutableStateOf(false) }

    if (showDatePicker.value) {
        val calendar = Calendar.getInstance()
        val dateParts = selectedDate.split(" ", ",")
        val day = dateParts.getOrNull(0)?.toIntOrNull() ?: calendar.get(Calendar.DAY_OF_MONTH)
        val month = (dateParts.getOrNull(2)?.toIntOrNull()?.minus(1)) ?: calendar.get(Calendar.MONTH)
        val year = dateParts.getOrNull(3)?.toIntOrNull() ?: calendar.get(Calendar.YEAR)

        DatePickerDialog(
            context,
            { _, y, m, d ->
                selectedDate = "$d tháng ${m + 1}, $y"
                showDatePicker.value = false
            },
            year, month, day
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Ngày sinh",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(36.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Ngày sinh có thể nhấn
        OutlinedTextField(
            value = selectedDate,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker.value = true },
            readOnly = true,
            label = { Text("Ngày sinh") }
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = {
                // TODO: Lưu ngày sinh
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFB700)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Lưu", fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

package com.example.praktikum2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.praktikum2.ui.theme.ShoppingListTheme

@Composable
fun HelpCenterScreen() {
    val faqItems = listOf(
        "Bagaimana cara menambahkan item baru?" to "Tekan kolom 'Barang baru...', ketik nama item, lalu tekan tombol '+'.",
        "Bagaimana cara menghapus item?" to "Geser item ke kiri untuk menghapusnya.",
        "Bagaimana cara mengubah urutan item?" to "Tekan dan tahan pada item yang ingin dipindahkan, lalu seret ke posisi yang baru.",
        "Apakah data saya aman?" to "Ya, semua data Anda disimpan di perangkat lokal dan tidak dikirim ke mana pun."
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Pusat Bantuan", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Berikut adalah beberapa pertanyaan yang sering diajukan:", style = MaterialTheme.typography.bodyLarge)
        }
        items(faqItems) { (question, answer) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(question, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(answer, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HelpCenterScreenPreview() {
    ShoppingListTheme {
        HelpCenterScreen()
    }
}
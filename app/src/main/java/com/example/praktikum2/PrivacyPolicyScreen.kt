package com.example.praktikum2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.praktikum2.ui.theme.ShoppingListTheme

@Composable
fun PrivacyPolicyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Kebijakan Privasi", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Terakhir diperbarui: 06 Oktober 2025\n\n" +
                    "Aplikasi Shopping List ('kami', 'kita', atau 'milik kami') mengoperasikan aplikasi mobile Shopping List (selanjutnya disebut 'Layanan').\n\n" +
                    "Halaman ini memberitahu Anda tentang kebijakan kami mengenai pengumpulan, penggunaan, dan pengungkapan data pribadi saat Anda menggunakan Layanan kami.",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Pengumpulan dan Penggunaan Informasi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Kami tidak mengumpulkan informasi identitas pribadi apa pun dari pengguna Layanan kami. Semua data daftar belanja disimpan secara lokal di perangkat Anda dan tidak dikirim ke server mana pun.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrivacyPolicyScreenPreview() {
    ShoppingListTheme {
        PrivacyPolicyScreen()
    }
}
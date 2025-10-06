package com.example.praktikum2

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.praktikum2.navigation.Screen
import com.example.praktikum2.ui.theme.ShoppingListTheme
import kotlinx.coroutines.delay
import androidx.compose.animation.fadeOut

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    navController: NavController
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Profile Header ---
        AnimatedSettingsSection(delayMillis = 0) {
            SettingsProfileHeader(navController)
        }

        // --- Bagian Pengaturan Umum ---
        AnimatedSettingsSection(delayMillis = 100) {
            SettingsCard(title = "Umum") {
                SettingsToggleItem(
                    icon = Icons.Default.DarkMode,
                    title = "Mode Gelap",
                    checked = isDarkTheme,
                    onCheckedChange = onThemeChange
                )
                SettingsClickableItem(
                    icon = Icons.Default.Translate,
                    title = "Bahasa",
                    subtitle = "Indonesia",
                    onClick = {
                        Toast.makeText(context, "Fitur ganti bahasa belum tersedia", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }

        // --- Bagian Notifikasi ---
        AnimatedSettingsSection(delayMillis = 200) {
            SettingsCard(title = "Notifikasi") {
                SettingsToggleItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifikasi Aplikasi",
                    checked = remember { mutableStateOf(true) }.value,
                    onCheckedChange = { /* Handle notifikasi utama */ }
                )
                SettingsToggleItem(
                    icon = Icons.Default.ChatBubble,
                    title = "Pesan & Update",
                    checked = remember { mutableStateOf(false) }.value,
                    onCheckedChange = { /* Handle pesan */ }
                )
            }
        }

        // --- Bagian Privasi & Keamanan ---
        AnimatedSettingsSection(delayMillis = 300) {
            SettingsCard(title = "Privasi & Keamanan") {
                SettingsClickableItem(
                    icon = Icons.Default.Lock,
                    title = "Ganti Kata Sandi",
                    onClick = { navController.navigate(Screen.ChangePassword.route) }
                )
                SettingsClickableItem(
                    icon = Icons.Default.PrivacyTip,
                    title = "Kebijakan Privasi",
                    onClick = { navController.navigate(Screen.PrivacyPolicy.route) }
                )
            }
        }

        // --- Bagian Bantuan & Tentang ---
        AnimatedSettingsSection(delayMillis = 400) {
            SettingsCard(title = "Bantuan & Tentang") {
                SettingsClickableItem(
                    icon = Icons.Default.Help,
                    title = "Pusat Bantuan",
                    onClick = { navController.navigate(Screen.HelpCenter.route) }
                )
                SettingsClickableItem(
                    icon = Icons.Default.Info,
                    title = "Tentang Aplikasi",
                    subtitle = "Versi 1.0.0",
                    onClick = { navController.navigate(Screen.About.route) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Tombol Logout ---
        AnimatedSettingsSection(delayMillis = 500) {
            OutlinedButton(
                onClick = { /*TODO: Implement Logout logic*/ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun AnimatedSettingsSection(delayMillis: Long, content: @Composable () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delayMillis)
        isVisible = true
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(300)) + slideInVertically(tween(300), initialOffsetY = { it / 4 }),
        exit = fadeOut(tween(150))
    ) {
        content()
    }
}

@Composable
fun SettingsProfileHeader(navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { navController.navigate(Screen.Profile.route) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.profile_pic),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Ahmad Rasha Radya Aufa Lubis",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Lihat Profil Lengkap",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go to Profile",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun SettingsCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
            )
            HorizontalDivider()
            Column(content = content)
        }
    }
}

@Composable
fun SettingsToggleItem(icon: ImageVector, title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
fun SettingsClickableItem(icon: ImageVector, title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = title, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            subtitle?.let {
                Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Detail",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    ShoppingListTheme {
        SettingsScreen(isDarkTheme = false, onThemeChange = {}, navController = rememberNavController())
    }
}
package com.example.praktikum2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.praktikum2.ui.theme.ShoppingListTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val scrollState = rememberLazyListState()
    val headerHeightPx = with(LocalDensity.current) { 240.dp.toPx() }
    val toolbarHeightPx = with(LocalDensity.current) { 64.dp.toPx() }

    val toolbarAlpha by animateFloatAsState(
        targetValue = if (scrollState.firstVisibleItemIndex > 0 || scrollState.firstVisibleItemScrollOffset > headerHeightPx - toolbarHeightPx) 1f else 0f,
        label = "Toolbar Alpha"
    )

    val profileImageSize by animateDpAsState(
        targetValue = if (scrollState.firstVisibleItemScrollOffset > 0 || scrollState.firstVisibleItemIndex > 0) 80.dp else 120.dp,
        label = "Image Size"
    )

    // State untuk mengontrol visibilitas dialog foto
    var showProfilePicDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ProfileHeader(
                    profileImageSize = profileImageSize,
                    onProfilePicClick = { showProfilePicDialog = true } // Tambahkan onClick di sini
                )
            }
            item {
                ProfileSectionCard(title = "Tentang Saya", delayMillis = 0) {
                    InfoRow(Icons.Default.Cake, "TTL", "Denpasar, 17 Oktober 2005")
                    InfoRow(Icons.Default.School, "Program Studi", "Sistem Informasi")
                    InfoRow(Icons.Default.Business, "Universitas", "Universitas Andalas")
                }
            }
            item {
                ProfileSectionCard(title = "Peminatan & Hobi", delayMillis = 100) {
                    InfoRow(Icons.Default.Code, "Peminatan", "UI/UX Designer")
                    InfoRow(Icons.Default.Favorite, "Hobi", "Berenang dan Bermain Game")
                }
            }
            item {
                ProfileSectionCard(title = "Kontak", delayMillis = 200) {
                    InfoRow(Icons.Default.Email, "Email", "aufalubis17@gmail.com")
                    InfoRow(Icons.Default.Phone, "Telepon", "+62 812-7615-3991")
                }
            }
        }

        // TopAppBar transparan yang muncul saat scroll
        TopAppBar(
            title = {
                Text(
                    "Ahmad Rasha Lubis",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = toolbarAlpha)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = toolbarAlpha),
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }

    // Tampilkan dialog foto profil jika showProfilePicDialog true
    if (showProfilePicDialog) {
        ProfilePictureDialog(
            onDismissRequest = { showProfilePicDialog = false }
        )
    }
}

@Composable
fun ProfileHeader(profileImageSize: Dp, onProfilePicClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
            .padding(top = 72.dp, bottom = 16.dp), // Beri ruang untuk TopAppBar transparan
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_pic),
            contentDescription = "Foto Profil",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(profileImageSize)
                .clip(CircleShape)
                .clickable { onProfilePicClick() } // Membuat gambar bisa diklik
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ahmad Rasha Radya Aufa Lubis",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "2311522008",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun ProfileSectionCard(title: String, delayMillis: Long, content: @Composable ColumnScope.() -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(delayMillis)
        isVisible = true
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(500)) + slideInVertically(tween(500), initialOffsetY = { it / 2 }),
        exit = fadeOut()
    ) {
        Card(
            modifier = Modifier.padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
                content()
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun ProfilePictureDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = "Foto Profil Lengkap",
                    contentScale = ContentScale.Fit, // Gunakan Fit agar gambar tidak terpotong
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp) // Batasi tinggi agar tidak terlalu besar
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismissRequest) {
                    Text("Tutup")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ShoppingListTheme {
        ProfileScreen()
    }
}
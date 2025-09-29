package com.example.praktikum2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktikum2.components.ShoppingListItem
import com.example.praktikum2.model.ShoppingItem
import com.example.praktikum2.ui.theme.ShoppingListTheme
import com.example.praktikum2.util.DragTarget
import com.example.praktikum2.util.DragTargetInfo
import com.example.praktikum2.util.DropTarget
import com.example.praktikum2.util.LocalDragTargetInfo
import com.example.praktikum2.util.LongPressDraggable
import kotlinx.coroutines.delay
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun ShoppingListScreen() {
    var items by remember {
        mutableStateOf(
            listOf(
                ShoppingItem(UUID.randomUUID().toString(), "Kopi Bubuk", false),
                ShoppingItem(UUID.randomUUID().toString(), "Gula Pasir", false),
                ShoppingItem(UUID.randomUUID().toString(), "Susu Krimer", false),
                ShoppingItem(UUID.randomUUID().toString(), "Roti Tawar", false),
            )
        )
    }
    var newItemName by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") } // <-- PERBAIKAN SALAH KETIK DI SINI
    var showAddedConfirmation by remember { mutableStateOf(false) }

    val filteredItems = items.filter { it.name.contains(searchQuery, ignoreCase = true) }
    val showClearButton = items.any { it.isChecked }
    val isTyping = newItemName.isNotBlank()

    LaunchedEffect(showAddedConfirmation) {
        if (showAddedConfirmation) {
            delay(1500)
            showAddedConfirmation = false
        }
    }

    CompositionLocalProvider(LocalDragTargetInfo provides remember { DragTargetInfo() }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Shopping List",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = showClearButton,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    Button(
                        onClick = { items = items.filter { !it.isChecked } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Hapus Selesai",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Hapus yang Selesai")
                    }
                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp)
                ) {
                    AddItemSection(
                        itemName = newItemName,
                        onItemNameChange = { newItemName = it },
                        onAddItem = {
                            if (newItemName.isNotBlank()) {
                                val newItem = ShoppingItem(UUID.randomUUID().toString(), newItemName)
                                items = items + newItem
                                newItemName = ""
                                showAddedConfirmation = true
                            }
                        },
                        isTyping = isTyping
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Cari barang...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (filteredItems.isEmpty() && searchQuery.isNotBlank()) {
                        AnimatedEmptyState(message = "Barang \"$searchQuery\" tidak ditemukan.")
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            itemsIndexed(items = filteredItems, key = { _, item -> item.id }) { index, item ->
                                LongPressDraggable(
                                    dataToDrop = item,
                                    modifier = Modifier.animateItemPlacement()
                                ) {
                                    DropTarget<ShoppingItem>(
                                        onDrop = { droppedItem ->
                                            val fromIndex = items.indexOf(droppedItem)
                                            val toIndex = items.indexOf(item)
                                            if (fromIndex != -1 && toIndex != -1) {
                                                items = items.toMutableList().apply {
                                                    add(toIndex, removeAt(fromIndex))
                                                }
                                            }
                                        }
                                    ) {
                                        ShoppingListItem(
                                            item = item,
                                            onCheckChange = { changedItem ->
                                                items = items.map {
                                                    if (it.id == changedItem.id) it.copy(isChecked = !it.isChecked) else it
                                                }
                                            },
                                            onDeleteItem = { deletedItem ->
                                                items = items.filter { it.id != deletedItem.id }
                                            },
                                            staggerDelay = (index * 50L).coerceAtMost(500L)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(
                    visible = showAddedConfirmation,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Row(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Added", tint = MaterialTheme.colorScheme.onTertiaryContainer)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Item Berhasil Ditambahkan!",
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }
                }
            }
        }
        DragTarget()
    }
}

@Composable
fun AddItemSection(
    itemName: String,
    onItemNameChange: (String) -> Unit,
    onAddItem: () -> Unit,
    isTyping: Boolean
) {
    val buttonScale by animateFloatAsState(
        targetValue = if (isTyping) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "Button Scale"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = itemName,
            onValueChange = onItemNameChange,
            label = { Text("Barang baru...") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onAddItem,
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.graphicsLayer {
                scaleX = buttonScale
                scaleY = buttonScale
            }
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Item")
        }
    }
}

@Composable
fun AnimatedEmptyState(message: String) {
    val infiniteTransition = rememberInfiniteTransition(label = "Empty State Animation")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Icon Offset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(bottom = 50.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Inbox,
                contentDescription = "Empty State",
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer {
                        translationY = offsetY
                    },
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun ShoppingListPreview() {
    ShoppingListTheme(darkTheme = false) {
        ShoppingListScreen()
    }
}
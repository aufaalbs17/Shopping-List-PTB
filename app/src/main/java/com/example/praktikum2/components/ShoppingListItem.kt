package com.example.praktikum2.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktikum2.model.ShoppingItem
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onCheckChange: (ShoppingItem) -> Unit,
    onDeleteItem: (ShoppingItem) -> Unit,
    modifier: Modifier = Modifier,
    staggerDelay: Long
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        isVisible = true
    }

    val density = LocalDensity.current
    val swipeState = rememberSwipeableState(initialValue = 0)
    val sizePx = with(density) { 80.dp.toPx() }
    val scope = rememberCoroutineScope()

    val cardColor by animateColorAsState(
        targetValue = if (item.isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant,
        animationSpec = tween(400), label = "cardColor"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(300, staggerDelay.toInt())) + slideInHorizontally(tween(300, staggerDelay.toInt()), initialOffsetX = { -it/2 })
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.errorContainer)
        ) {
            // ... (Backdrop untuk hapus tetap sama)

            Card(
                modifier = Modifier
                    .offset { IntOffset(swipeState.offset.value.roundToInt(), 0) }
                    .swipeable(
                        state = swipeState,
                        anchors = mapOf(0f to 0, -sizePx to 1),
                        thresholds = { _, _ -> FractionalThreshold(0.3f) },
                        orientation = Orientation.Horizontal,
                        enabled = !item.isChecked
                    )
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Elevasi 0 agar lebih flat
                colors = CardDefaults.cardColors(containerColor = cardColor),
                onClick = { onCheckChange(item) }
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .animateContentSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = if (item.isChecked) 1f else 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.name.firstOrNull()?.toString()?.uppercase() ?: "",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = item.name,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge,
                        textDecoration = if (item.isChecked) TextDecoration.LineThrough else null,
                        color = if (item.isChecked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                    )

                    AnimatedVisibility(
                        visible = item.isChecked,
                        enter = scaleIn(animationSpec = tween(200)) + fadeIn(),
                        exit = scaleOut(animationSpec = tween(200)) + fadeOut()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Checked",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(swipeState.currentValue) {
        if (swipeState.currentValue == 1) {
            onDeleteItem(item)
            scope.launch { swipeState.animateTo(0) }
        }
    }
}
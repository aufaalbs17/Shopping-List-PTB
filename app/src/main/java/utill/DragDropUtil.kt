package com.example.praktikum2.util

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

internal class DragTargetInfo {
    var isDragging: Boolean by mutableStateOf(false)
    var dragPosition by mutableStateOf(Offset.Zero)
    var dragOffset by mutableStateOf(Offset.Zero)
    var draggableComposable by mutableStateOf<(@Composable () -> Unit)?>(null)
    var dataToDrop by mutableStateOf<Any?>(null)
}

internal val LocalDragTargetInfo = compositionLocalOf { DragTargetInfo() }

@Composable
fun <T> LongPressDraggable(
    modifier: Modifier = Modifier,
    dataToDrop: T,
    onDragStart: () -> Unit = {},
    onDragEnd: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val dragTargetInfo = LocalDragTargetInfo.current
    var currentPosition by remember { mutableStateOf(Offset.Zero) }
    var currentSize by remember { mutableStateOf(IntSize.Zero) }

    Box(modifier = modifier
        .onGloballyPositioned {
            currentPosition = it.localToWindow(Offset.Zero)
            currentSize = it.size
        }
        .pointerInput(Unit) {
            detectDragGesturesAfterLongPress(
                onDragStart = { offset ->
                    dragTargetInfo.dataToDrop = dataToDrop
                    dragTargetInfo.isDragging = true
                    dragTargetInfo.dragPosition = currentPosition + offset
                    dragTargetInfo.draggableComposable = {
                        Box(modifier = Modifier) {
                            content()
                        }
                    }
                    onDragStart()
                },
                onDrag = { change, dragAmount ->
                    change.consume()
                    dragTargetInfo.dragOffset += dragAmount
                },
                onDragEnd = {
                    dragTargetInfo.isDragging = false
                    dragTargetInfo.dragOffset = Offset.Zero
                    onDragEnd()
                },
                onDragCancel = {
                    dragTargetInfo.isDragging = false
                    dragTargetInfo.dragOffset = Offset.Zero
                }
            )
        }) {
        content()
    }
}

@Composable
fun DragTarget(modifier: Modifier = Modifier) { // <-- PERBAIKAN: Hapus parameter 'content'
    val dragTargetInfo = LocalDragTargetInfo.current
    val isDragging = dragTargetInfo.isDragging

    if (isDragging) {
        Box(
            modifier = modifier
                .graphicsLayer {
                    val offset = dragTargetInfo.dragPosition + dragTargetInfo.dragOffset
                    translationX = offset.x
                    translationY = offset.y
                    alpha = if (isDragging) 0.8f else 0f
                }
        ) {
            dragTargetInfo.draggableComposable?.invoke()
        }
    }
}

@Composable
fun <T> DropTarget(
    modifier: Modifier = Modifier,
    onDrop: (T) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val dragTargetInfo = LocalDragTargetInfo.current
    var isHovered by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .onGloballyPositioned {
                if (dragTargetInfo.isDragging) {
                    val area = it.size
                    val position = it.localToWindow(Offset.Zero)
                    val dropPosition = dragTargetInfo.dragPosition + dragTargetInfo.dragOffset
                    isHovered = dropPosition.x in position.x..(position.x + area.width) &&
                            dropPosition.y in position.y..(position.y + area.height)
                } else {
                    isHovered = false
                }
            }
            .pointerInput(dragTargetInfo.isDragging) {
                if (dragTargetInfo.isDragging) {
                    detectDragGesturesAfterLongPress(
                        onDrag = { change, _ -> change.consume() },
                        onDragEnd = {
                            if (isHovered) {
                                (dragTargetInfo.dataToDrop as? T)?.let(onDrop)
                            }
                        }
                    )
                }
            }
    ) {
        content()
    }
}
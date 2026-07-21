@file:Suppress("MatchingDeclarationName")

package com.practicum.shoppinglist.presentation.ui.products

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

class DragDropState(
    val lazyListState: LazyListState,
    private val onMove: (Int, Int) -> Unit,
    private val onDragEnd: () -> Unit = {}
) {
    var draggedIndex by mutableStateOf<Int?>(null)
        private set

    private var initialItemOffset = 0f
    private var totalDragOffset by mutableFloatStateOf(0f)

    private val draggedItemInfo
        get() = lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { it.index == draggedIndex }

    fun onDragStart(index: Int) {
        draggedIndex = index
        totalDragOffset = 0f
        initialItemOffset = lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { it.index == index }
            ?.offset
            ?.toFloat() ?: 0f
    }

    fun onDrag(offset: Offset) {
        totalDragOffset += offset.y
        val currentItemInfo = draggedItemInfo ?: return
        val currentItemIndex = currentItemInfo.index

        val desiredTop = initialItemOffset + totalDragOffset
        val desiredCenter = desiredTop + currentItemInfo.size / 2

        val targetItem = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
            item.index != currentItemIndex &&
                desiredCenter.roundToInt() in item.offset..item.offset + item.size
        }

        if (targetItem != null) {
            onMove(currentItemIndex, targetItem.index)
            draggedIndex = targetItem.index
        }
    }

    fun onDragInterrupted() {
        draggedIndex = null
        totalDragOffset = 0f
        onDragEnd()
    }

    fun overlayOffset(): IntOffset =
        IntOffset(0, (initialItemOffset + totalDragOffset).roundToInt())
}

fun Modifier.dragDropGesture(state: DragDropState): Modifier = this.pointerInput(state) {
    detectDragGesturesAfterLongPress(
        onDragStart = { offset ->
            val layoutInfo = state.lazyListState.layoutInfo
            val item = layoutInfo.visibleItemsInfo.firstOrNull {
                offset.y.toInt() in it.offset..it.offset + it.size
            }
            if (item != null) {
                state.onDragStart(item.index)
            }
        },
        onDrag = { change, dragAmount ->
            change.consume()
            state.onDrag(dragAmount)
        },
        onDragEnd = { state.onDragInterrupted() },
        onDragCancel = { state.onDragInterrupted() }
    )
}

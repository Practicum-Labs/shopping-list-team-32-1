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
    private val onMove: (Int, Int) -> Unit
) {
    var draggedIndex by mutableStateOf<Int?>(null)
        private set

    private var dragOffset by mutableFloatStateOf(0f)

    private val draggedItemInfo
        get() = lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { it.index == draggedIndex }

    fun onDragStart(index: Int) {
        draggedIndex = index
    }

    fun onDrag(offset: Offset) {
        dragOffset += offset.y
        val currentItemInfo = draggedItemInfo ?: return
        val currentItemIndex = currentItemInfo.index
        val currentItemOffset = currentItemInfo.offset

        val targetItem = lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { item ->
            val relativeOffset = currentItemOffset + dragOffset
            relativeOffset.roundToInt() in item.offset..(item.offset + item.size) &&
                item.index != currentItemIndex
        }

        if (targetItem != null) {
            onMove(currentItemIndex, targetItem.index)
            draggedIndex = targetItem.index
            dragOffset = 0f
        }
    }

    fun onDragInterrupted() {
        draggedIndex = null
        dragOffset = 0f
    }

    fun getItemOffset(index: Int): IntOffset {
        return if (index == draggedIndex) {
            IntOffset(0, dragOffset.roundToInt())
        } else {
            IntOffset.Zero
        }
    }
}

fun Modifier.dragDropGesture(state: DragDropState): Modifier = this.pointerInput(state) {
    detectDragGesturesAfterLongPress(
        onDragStart = { offset ->
            val layoutInfo = state.lazyListState.layoutInfo
            val item = layoutInfo.visibleItemsInfo.firstOrNull {
                offset.y.toInt() in it.offset..(it.offset + it.size)
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

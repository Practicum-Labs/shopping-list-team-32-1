package com.practicum.shoppinglist.presentation.ui.common

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import com.practicum.shoppinglist.presentation.theme.Dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private const val LongSwipeThresholdMultiplier = 1.5f
private const val OpenThresholdDivisor = 2f

@Stable
class SwipeableItemState(
    val itemKey: Any,
    val actionsWidthPx: Float,
    private val coroutineScope: CoroutineScope,
) {
    var offsetX by mutableFloatStateOf(0f)
        private set

    val isLongSwipe: Boolean
        get() = offsetX < actionsWidthPx * LongSwipeThresholdMultiplier

    fun onDrag(dragAmount: Float) {
        offsetX = (offsetX + dragAmount).coerceAtMost(0f)
    }

    fun animateOffsetTo(target: Float) {
        coroutineScope.launch {
            animate(
                initialValue = offsetX,
                targetValue = target,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
            ) { value, _ -> offsetX = value }
        }
    }

    fun handleDragEnd(openedItemKey: Any?, onOpenedChange: (Any?) -> Unit, onDelete: () -> Unit) {
        when (resolveSwipeEndResult(offsetX, actionsWidthPx)) {
            SwipeEndResult.DELETE -> {
                onDelete()
                if (openedItemKey == itemKey) onOpenedChange(null)
                animateOffsetTo(0f)
            }
            SwipeEndResult.OPEN -> {
                onOpenedChange(itemKey)
                animateOffsetTo(actionsWidthPx)
            }
            SwipeEndResult.CLOSE -> {
                if (openedItemKey == itemKey) onOpenedChange(null)
                animateOffsetTo(0f)
            }
        }
    }
}

private enum class SwipeEndResult { DELETE, OPEN, CLOSE }

private fun resolveSwipeEndResult(offsetX: Float, actionsWidthPx: Float): SwipeEndResult = when {
    offsetX < actionsWidthPx * LongSwipeThresholdMultiplier -> SwipeEndResult.DELETE
    offsetX < actionsWidthPx / OpenThresholdDivisor -> SwipeEndResult.OPEN
    else -> SwipeEndResult.CLOSE
}

@Composable
private fun rememberSwipeableItemState(
    itemKey: Any,
    openedItemKey: Any?,
    actionsWidth: Dp,
): SwipeableItemState {
    val density = LocalDensity.current
    val actionsWidthPx = with(density) { -actionsWidth.toPx() }
    val coroutineScope = rememberCoroutineScope()

    val state = remember(itemKey, actionsWidthPx) {
        SwipeableItemState(itemKey = itemKey, actionsWidthPx = actionsWidthPx, coroutineScope = coroutineScope)
    }

    LaunchedEffect(openedItemKey) {
        if (openedItemKey != itemKey && state.offsetX != 0f) {
            state.animateOffsetTo(0f)
        }
    }

    return state
}

private fun Modifier.closeOthersOnTap(
    itemKey: Any,
    openedItemKey: Any?,
    onOpenedChange: (Any?) -> Unit,
): Modifier = pointerInput(itemKey, openedItemKey) {
    awaitEachGesture {
        awaitFirstDown(pass = PointerEventPass.Initial)
        if (openedItemKey != null && openedItemKey != itemKey) {
            onOpenedChange(null)
        }
    }
}

private fun Modifier.swipeToRevealGesture(
    state: SwipeableItemState,
    openedItemKey: Any?,
    onOpenedChange: (Any?) -> Unit,
    onDelete: () -> Unit,
): Modifier = pointerInput(Unit) {
    detectHorizontalDragGestures(
        onDragEnd = { state.handleDragEnd(openedItemKey, onOpenedChange, onDelete) },
        onHorizontalDrag = { change, dragAmount ->
            change.consume()
            state.onDrag(dragAmount)
        }
    )
}

@Composable
@Suppress("LongParameterList")
fun SwipeableListItem(
    onDelete: () -> Unit,
    backgroundContent: @Composable (isLongSwipe: Boolean, closeItem: () -> Unit) -> Unit,
    content: @Composable () -> Unit,
    itemKey: Any,
    openedItemKey: Any?,
    onOpenedChange: (Any?) -> Unit,
    modifier: Modifier = Modifier,
    actionsWidth: Dp = Dimens.Main.swipeActionsWidth,
) {
    val state = rememberSwipeableItemState(itemKey, openedItemKey, actionsWidth)

    val closeItem: () -> Unit = {
        if (openedItemKey == itemKey) onOpenedChange(null)
        state.animateOffsetTo(0f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .closeOthersOnTap(itemKey, openedItemKey, onOpenedChange)
    ) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(Dimens.Main.listItemCornerRadius))
        ) {
            backgroundContent(state.isLongSwipe, closeItem)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(state.offsetX.roundToInt(), 0) }
                .swipeToRevealGesture(state, openedItemKey, onOpenedChange, onDelete)
        ) {
            content()
        }
    }
}

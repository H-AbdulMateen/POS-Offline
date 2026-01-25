package com.abdulmateen.pos_offline.feature.main.inventory.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeableItemWithActions(
    state: SwipeableItemState,
    isRevealed: Boolean,
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()


    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .onSizeChanged {
                    state.contextMenuWidth = it.width.toFloat()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(state.offset.value.roundToInt(), 0) }
//                .pointerInput(contextMenuWidth) {
                .pointerInput(state.contextMenuWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
//                                val newOffset = (offset.value + dragAmount)
//                                    .coerceIn(0f, contextMenuWidth)
//                                offset.snapTo(newOffset)
                                    state.snapTo(state.offset.value + dragAmount)
                            }
                        },
                        onDragEnd = {
                            if (state.offset.value >= state.contextMenuWidth / 2f) {
                                state.expand(onExpanded)
                            } else {
                                state.collapse(onCollapsed)
                            }
//                            when {
//                                offset.value >= contextMenuWidth / 2f -> {
//                                        scope.launch {
//                                                offset.animateTo(contextMenuWidth)
//                                                onExpanded()
//
//                                        }
//                                }
//
//                                else -> {
//                                    scope.launch {
//                                        offset.animateTo(0f)
//                                        onCollapsed()
//                                    }
//                                }
//                            }
                        }
                    )
                }
        ) {
            content()
        }
    }
}
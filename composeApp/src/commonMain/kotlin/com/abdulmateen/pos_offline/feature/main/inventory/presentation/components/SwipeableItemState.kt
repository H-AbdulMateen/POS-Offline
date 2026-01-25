package com.abdulmateen.pos_offline.feature.main.inventory.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SwipeableItemState(
    initialValue: Boolean,
    private val scope: CoroutineScope
) {
    val offset = Animatable(if (initialValue) 1f else 0f) // We'll use 0f to width
    
    // Internal width reference
    var contextMenuWidth = 0f

    fun expand(onExpanded: () -> Unit = {}) {
        scope.launch {
            offset.animateTo(contextMenuWidth)
            onExpanded()
        }
    }

    fun collapse(onCollapsed: () -> Unit = {}) {
        scope.launch {
            offset.animateTo(0f)
            onCollapsed()
        }
    }
    
    suspend fun snapTo(value: Float) {
        offset.snapTo(value.coerceIn(0f, contextMenuWidth))
    }
}

// Helper to remember the state
@Composable
fun rememberSwipeableItemState(
    initialValue: Boolean = false,
    scope: CoroutineScope = rememberCoroutineScope()
): SwipeableItemState {
    return remember { SwipeableItemState(initialValue, scope) }
}
package com.example.bottomsheet_component.ui

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun BoxScope.BottomSheet(
    bottomSheetState: BottomSheetState,
    onDefault: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    onExpanded: () -> Unit = {},
    lineSize: Dp = 50.dp,
    lineDistanceFromTop: Dp = 13.dp,
    lineWidth: Float = 10f,
    content: @Composable ColumnScope.() -> Unit,
) {
    val height = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    val coroutineScope = rememberCoroutineScope()

    Column(
        Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .fillMaxHeight(bottomSheetState.screenPercentage.value)
            .clip(RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp))
            .background(Color.White)
            .snapDrag(
                bottomSheetState,
                coroutineScope,
                onDefault = { onDefault() },
                onCollapsed = { onCollapsed },
                onExpanded = { onExpanded })
            .drawBehind {
                drawLine(
                    color = Color.LightGray,
                    start = Offset(
                        size.width / 2 - lineSize.toPx() / 2,
                        lineDistanceFromTop.toPx()
                    ),
                    end = Offset(
                        size.width / 2 + lineSize.toPx() / 2,
                        lineDistanceFromTop.toPx()
                    ),
                    strokeWidth = lineWidth
                )
            }
            .padding(25.dp)
    ) {
        content()
    }
}

@Composable
fun rememberBottomSheetState(
    startStage: BottomSheetStage,
    bottomSheetStagePercentage: BottomSheetStagePercentage
): BottomSheetState {
    return remember {
        BottomSheetState(
            startStage = startStage,
            bottomSheetStagePercentage = bottomSheetStagePercentage
        )
    }
}

data class BottomSheetStagePercentage(
    val DEFAULT: Float,
    val EXPANDED: Float,
    val COLLAPSED: Float
)

enum class BottomSheetStage {
    DEFAULT,
    EXPANDED,
    COLLAPSED
}

@Stable
class BottomSheetState(
    startStage: BottomSheetStage,
    val bottomSheetStagePercentage: BottomSheetStagePercentage
) {
    var isDragging by mutableStateOf(false)
    var stage by mutableStateOf(startStage)
    val screenPercentage = Animatable(
        when (startStage) {
            BottomSheetStage.DEFAULT -> {
                bottomSheetStagePercentage.DEFAULT
            }

            BottomSheetStage.EXPANDED -> {
                bottomSheetStagePercentage.EXPANDED
            }

            else -> {
                bottomSheetStagePercentage.COLLAPSED
            }
        }
    )

}

fun Modifier.smoothDrag(
    bottomSheetState: BottomSheetState,
    coroutineScope: CoroutineScope,
    screenHeight: Float,
    onDefault: () -> Unit = {},
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {}
): Modifier {
    return this.then(pointerInput(true) {
        detectVerticalDragGestures(
            onDragEnd = {
                bottomSheetState.isDragging = false
                Log.d(
                    "Drag",
                    "Animation Running: ${bottomSheetState.screenPercentage.isRunning}"
                )
                if (!bottomSheetState.screenPercentage.isRunning) {
                    if (
                        with(bottomSheetState) {
                            // 1.5f
                            screenPercentage.value > (bottomSheetStagePercentage.DEFAULT + bottomSheetStagePercentage.EXPANDED) / 2
                        }) {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.animateTo(bottomSheetState.bottomSheetStagePercentage.EXPANDED)
                            bottomSheetState.stage = BottomSheetStage.EXPANDED
                            onExpanded()
                        }
                    } else if (
                        with(bottomSheetState) {
                            // 0.53f
                            screenPercentage.value < (bottomSheetStagePercentage.DEFAULT + bottomSheetStagePercentage.COLLAPSED) / 2
                        })
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.animateTo(bottomSheetState.bottomSheetStagePercentage.COLLAPSED)
                            bottomSheetState.stage = BottomSheetStage.COLLAPSED
                            onCollapsed()
                        }
                    else {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.animateTo(bottomSheetState.bottomSheetStagePercentage.DEFAULT)
                            bottomSheetState.stage = BottomSheetStage.DEFAULT
                            onDefault()
                        }
                    }
                }
            }
        ) { change, dragAmount ->

            bottomSheetState.isDragging = true

            if (bottomSheetState.screenPercentage.value - (dragAmount / screenHeight) < bottomSheetState.bottomSheetStagePercentage.COLLAPSED) {
                coroutineScope.launch {
                    bottomSheetState.screenPercentage.snapTo(bottomSheetState.bottomSheetStagePercentage.COLLAPSED)
                }
            } else if (bottomSheetState.screenPercentage.value - (dragAmount / screenHeight) > bottomSheetState.bottomSheetStagePercentage.EXPANDED) {
                coroutineScope.launch {
                    bottomSheetState.screenPercentage.snapTo(bottomSheetState.bottomSheetStagePercentage.EXPANDED)
                }
            } else {
                coroutineScope.launch {
                    bottomSheetState.screenPercentage.snapTo(bottomSheetState.screenPercentage.value - dragAmount / screenHeight)
                }
            }
        }
    })
}

fun Modifier.snapDrag(
    bottomSheetState: BottomSheetState,
    coroutineScope: CoroutineScope,
    onDefault: () -> Unit = {},
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {}
): Modifier {
    return this.then(pointerInput(true) {
        detectVerticalDragGestures { change, _ ->
            /*
            Start of Swipe
            ----------------------------------------------------------------------------------------------------
            Getting the speed of the drag if drag positive it means a swipe down, if negative a swipe up.
             Swipe down goes from small + to large +, Swipe down goes from small+- to large -:
             Calculate distance between swipes to determine if long or short swipe (usually 50 long 100 short)
             Does not work that simple:
             - If goes from low to high, means swipe down
             - If goes from height to low means swipe up.
             */

            // Swipe Down = curr > prev
            if (abs(change.position.y - change.previousPosition.y) > 40 && change.position.y > change.previousPosition.y) {
                if (abs(change.position.y - change.previousPosition.y) > 100) {
                    if (bottomSheetState.stage != BottomSheetStage.COLLAPSED) {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.animateTo(bottomSheetState.bottomSheetStagePercentage.COLLAPSED)
                            bottomSheetState.stage = BottomSheetStage.COLLAPSED
                            onCollapsed()
                        }
                    }
                } else {
                    if (bottomSheetState.stage == BottomSheetStage.EXPANDED) {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.animateTo(bottomSheetState.bottomSheetStagePercentage.DEFAULT)
                            bottomSheetState.stage = BottomSheetStage.DEFAULT
                            onDefault()
                        }
                    } else if (bottomSheetState.stage == BottomSheetStage.DEFAULT) {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.animateTo(bottomSheetState.bottomSheetStagePercentage.COLLAPSED)
                            bottomSheetState.stage = BottomSheetStage.COLLAPSED
                            onCollapsed()
                        }
                    }
                }
            }

            // Swipe Up = prev > curr
            else if (abs(change.position.y - change.previousPosition.y) > 40 && change.previousPosition.y > change.position.y) {
                if (abs(change.position.y - change.previousPosition.y) > 100) {
                    if (bottomSheetState.stage != BottomSheetStage.EXPANDED) {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.animateTo(bottomSheetState.bottomSheetStagePercentage.EXPANDED)
                            bottomSheetState.stage = BottomSheetStage.EXPANDED
                            onExpanded()
                        }
                    }
                } else {
                    if (bottomSheetState.stage == BottomSheetStage.DEFAULT) {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.animateTo(bottomSheetState.bottomSheetStagePercentage.EXPANDED)
                            bottomSheetState.stage = BottomSheetStage.EXPANDED
                            onExpanded()
                        }
                    } else if (bottomSheetState.stage == BottomSheetStage.COLLAPSED) {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.animateTo(bottomSheetState.bottomSheetStagePercentage.DEFAULT)
                            bottomSheetState.stage = BottomSheetStage.DEFAULT
                            onDefault()
                        }
                    }
                }
            }
        }
    })
}
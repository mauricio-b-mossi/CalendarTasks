package com.example.bottomsheet_component.ui

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
import kotlinx.coroutines.launch

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
            .pointerInput(true) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        bottomSheetState.isDragging = false
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
                ) { _, dragAmount ->
                    bottomSheetState.isDragging = true
                    if (bottomSheetState.screenPercentage.value - (dragAmount / height) < bottomSheetState.bottomSheetStagePercentage.COLLAPSED) {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.snapTo(bottomSheetState.bottomSheetStagePercentage.COLLAPSED)
                        }
                    } else if (bottomSheetState.screenPercentage.value - (dragAmount / height) > bottomSheetState.bottomSheetStagePercentage.EXPANDED) {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.snapTo(bottomSheetState.bottomSheetStagePercentage.EXPANDED)
                        }
                    } else {
                        coroutineScope.launch {
                            bottomSheetState.screenPercentage.snapTo(bottomSheetState.screenPercentage.value - dragAmount / height)
                        }
                    }
                }
            }
            .drawBehind {
                drawLine(
                    color = Color.LightGray,
                    start = Offset(
                        size.width / 2 - lineSize.toPx() / 2,
                        lineDistanceFromTop.toPx()
                    ),
                    end = Offset(size.width / 2 + lineSize.toPx() / 2, lineDistanceFromTop.toPx()),
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

data class BottomSheetStagePercentage(val DEFAULT: Float, val EXPANDED: Float, val COLLAPSED: Float)

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


package com.example.canvasexperimentation.ui.homeScreen.components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
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
import androidx.compose.ui.unit.dp
import kotlin.math.abs

interface BottomSheetStageSnapPoints {
    val DEFAULT: Float
    val COLLAPSED: Float
    val EXPANDED: Float
}

val snapPoints = object : BottomSheetStageSnapPoints {
    override val DEFAULT: Float
        get() = 0.5f
    override val COLLAPSED: Float
        get() = 0.02f
    override val EXPANDED: Float
        get() = 1f

}

@Stable
enum class BottomSheetStage {
    DEFAULT, COLLAPSED, EXPANDED
}

@Stable
class BottomSheetState(
    stage: BottomSheetStage = BottomSheetStage.DEFAULT,
    isDragging: Boolean = false,
    val snapPoint: BottomSheetStageSnapPoints,
) {
    var stage by mutableStateOf(stage)
    var isDragging by mutableStateOf(false)
}


@Composable
fun rememberBottomSheetState(
    stage: BottomSheetStage = BottomSheetStage.DEFAULT,
    snapPoints: BottomSheetStageSnapPoints = object : BottomSheetStageSnapPoints {
        override val DEFAULT: Float
            get() = 0.5f
        override val COLLAPSED: Float
            get() = 0.03f
        override val EXPANDED: Float
            get() = 1f

    }
): BottomSheetState {
    return remember {
        BottomSheetState(stage = stage, snapPoint = snapPoints)
    }
}

@Composable
fun BoxScope.BottomSheet(
    bottomSheetState: BottomSheetState = rememberBottomSheetState(
        stage = BottomSheetStage.DEFAULT, snapPoints = snapPoints
    ),
    onDefault: () -> Unit,
    onExpanded: () -> Unit,
    onCollapsed: () -> Unit,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit,
    dragSurfaceHeight: Float,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    var currentPercentageOffset by remember {
        mutableStateOf(0.5f)
    }

    val animateFloat: Float by animateFloatAsState(
        targetValue = when (bottomSheetState.stage) {
            BottomSheetStage.DEFAULT -> bottomSheetState.snapPoint.DEFAULT
            BottomSheetStage.COLLAPSED -> bottomSheetState.snapPoint.COLLAPSED
            BottomSheetStage.EXPANDED -> bottomSheetState.snapPoint.EXPANDED
        },
        animationSpec = tween(2000)
    )

    LaunchedEffect(key1 = bottomSheetState.stage, key2 = bottomSheetState.isDragging) {
        Log.d("Drag", "${bottomSheetState.isDragging}")
        Log.d("Drag", "${bottomSheetState.stage}")
        Log.d("Drag", "Animate Float ${animateFloat}")
        currentPercentageOffset = animateFloat
    }

    Column(modifier = modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .fillMaxHeight(currentPercentageOffset)
        .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
        .background(Color.White)
        .drawBehind {
            val lineSize = 50.dp.toPx()
            val startX = size.width / 2 - lineSize / 2
            drawLine(
                Color.LightGray,
                Offset(startX, 10.dp.toPx()),
                Offset(startX + lineSize, 10.dp.toPx()),
                10f
            )
        }
        .bottomSheet(
            bottomSheetState = bottomSheetState,
            dragSurfaceHeight = dragSurfaceHeight,
            currentPercentageOffset = { currentPercentageOffset },
            onPercentageOffsetChange = { currentPercentageOffset += it },
            onDefault = { onDefault() },
            onExpanded = { onExpanded() },
            onCollapsed = { onCollapsed() },
            onDragStart = { onDragStart() },
            onDragEnd = { onDragEnd() }
        )
    ) {
        content()
    }
}

fun Modifier.bottomSheet(
    bottomSheetState: BottomSheetState,
    dragSurfaceHeight: Float,
    currentPercentageOffset: () -> Float,
    onPercentageOffsetChange: (Float) -> Unit,
    onDefault: () -> Unit,
    onExpanded: () -> Unit,
    onCollapsed: () -> Unit,
    onDragStart: () -> Unit,
    onDragEnd: () -> Unit
): Modifier {
    return this.then(
        pointerInput(true) {
            detectVerticalDragGestures(onDragEnd = {
                Log.d("Drag", "${currentPercentageOffset()}")
                //Curr > 0.5
                if (currentPercentageOffset() > bottomSheetState.snapPoint.DEFAULT) {
                    // Curr > 0.75
                    Log.d(
                        "Drag",
                        "${currentPercentageOffset() > with(bottomSheetState.snapPoint) { (DEFAULT + EXPANDED) / 2 }}"
                    )
                    if (currentPercentageOffset() > with(bottomSheetState.snapPoint) { (DEFAULT + EXPANDED) / 2 }) {
                        Log.d("Drag", "EXPANDED")
                        onExpanded()
                    } else {
                        Log.d("Drag", "DEFAULT")
                        onDefault()
                    }
                } else {
                    if (currentPercentageOffset() > with(bottomSheetState.snapPoint) { (DEFAULT + COLLAPSED) / 2 }) {
                        Log.d("Drag", "DEFAULT")
                        onDefault()
                    } else {
                        Log.d("Drag", "COLLAPSED")
                        onCollapsed()
                    }
                }
                onDragEnd()
            }) { _, dragAmount ->
                onDragStart()
                val dragAmountPercentage = dragAmount / dragSurfaceHeight
                onPercentageOffsetChange(-dragAmountPercentage)
            }
        }
    )
}

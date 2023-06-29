package com.example.canvasexperimentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bottomsheet_component.ui.BottomSheet
import com.example.bottomsheet_component.ui.BottomSheetStage
import com.example.bottomsheet_component.ui.BottomSheetStagePercentage
import com.example.bottomsheet_component.ui.rememberBottomSheetState
import com.example.canvasexperimentation.ui.theme.activeColor
import com.example.canvasexperimentation.ui.theme.calendarBackgroundColor
import com.example.canvasexperimentation.ui.theme.inactiveColor
import com.example.canvasexperimentation.ui.theme.secondaryColor
import com.example.canvasexperimentation.ui.theme.selectedColor
import com.example.date_components.ui.components.Calendar
import com.example.date_components.ui.components.CalendarOrientation
import com.example.date_components.ui.components.rememberCalendarState
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTextApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var date by remember {
                mutableStateOf(LocalDate.now())
            }

            val bottomSheetState = rememberBottomSheetState(
                startStage = BottomSheetStage.DEFAULT,
                bottomSheetStagePercentage = BottomSheetStagePercentage(
                    DEFAULT = 0.5F,
                    EXPANDED = 1F,
                    COLLAPSED = 0.1F
                )
            )

            val calendarState =
                rememberCalendarState(date = LocalDate.now(), range = 3, CalendarOrientation.ROW)

            val textMeasurer = rememberTextMeasurer()

            val coroutineScope = rememberCoroutineScope()

            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawRect(calendarBackgroundColor)
                    }
            ) {
                Calendar(
                    calendarState = calendarState,
                    date = date,
                    onDateChange = {
                        date = it
                        coroutineScope.launch {
                            calendarState.animateToSelectedDate(date)
                            bottomSheetState.screenPercentage.animateTo(bottomSheetState.bottomSheetStagePercentage.DEFAULT)
                            bottomSheetState.stage = BottomSheetStage.DEFAULT
                            calendarState.calendarOrientation = CalendarOrientation.ROW
                        }
                    },
                    textMeasurer = textMeasurer,
                    activeColor = activeColor,
                    inactiveColor = inactiveColor,
                    selectedColor = selectedColor,
                    itemPadding = 35.dp,
                )
                BottomSheet(
                    bottomSheetState = bottomSheetState,
                    onDefault = {
                        coroutineScope.launch {
                            calendarState.animateToBound(200)
                            calendarState.calendarOrientation = CalendarOrientation.ROW
                        }
                    },
                    onExpanded = {},
                    onCollapsed = {
                        calendarState.calendarOrientation = CalendarOrientation.COLUMN
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                coroutineScope.launch {
                                    calendarState.lazyListState.animateScrollToItem(calendarState.startingIndex)
                                }
                            },
                            containerColor = secondaryColor,
                            shape = CircleShape,
                            modifier = Modifier.size(40.dp)
                        ) {
                            AnimatedContent(calendarState.beforeStartingIndex) {
                                if (it) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    },
                    showFloatingActionButton = bottomSheetState.isStaticAtCollapsed && !calendarState.inStartingIndex
                ) {
                    TodoBottomSheet(
                        date = date,
                        onClickSelectedDate = {
                            coroutineScope.launch {
                                calendarState.animateToSelectedDate(it)
                            }
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                onClick = {
                                    coroutineScope.launch {
                                        calendarState.lazyListState.animateScrollToItem(
                                            calendarState.startingIndex
                                        )
                                    }
                                },
                                containerColor = secondaryColor
                            ) {
                                AnimatedContent(calendarState.beforeStartingIndex) {
                                    if (it) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowRight,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowLeft,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        },
                        showBottomFloatingActionButton = bottomSheetState.isStaticAtDefault && !calendarState.inStartingIndex,
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TodoBottomSheet(
    date: LocalDate,
    onClickSelectedDate: (LocalDate) -> Unit = {},
    floatingActionButton: @Composable () -> Unit,
    showBottomFloatingActionButton: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize()) {

        Column {
            if (date == LocalDate.now()) {
                Text(
                    text = "TODAY", style = TextStyle(
                        color = selectedColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Default,
                        letterSpacing = 0.5.sp
                    ), modifier = Modifier.clickable { onClickSelectedDate(date) }
                )
            } else {
                //Day of Week, Month dayOfMonth, year
                Text(
                    text = "${date.dayOfWeek}, ${date.month} ${date.dayOfMonth}, ${date.year}",
                    style = TextStyle(
                        color = selectedColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Default,
                        letterSpacing = 0.5.sp
                    ), modifier = Modifier.clickable { onClickSelectedDate(date) }
                )
            }

            Spacer(Modifier.height(16.dp))
        }

        Box(modifier = Modifier.align(Alignment.BottomStart)) {
            AnimatedVisibility(
                showBottomFloatingActionButton,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally(animationSpec = tween(200)) + scaleOut(
                    animationSpec = tween(200)
                )
            ) {
                floatingActionButton()
            }
        }
    }

}



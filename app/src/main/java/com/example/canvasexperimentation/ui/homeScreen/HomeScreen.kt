package com.example.canvasexperimentation.ui.homeScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import com.example.canvasexperimentation.ui.homeScreen.components.Calendar
import java.time.LocalDate

@OptIn(ExperimentalTextApi::class)
@Composable
fun HomeScreen(
    date: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    textMeasurer: TextMeasurer,
    modifier: Modifier = Modifier
) {
    Calendar(date =date, onDateSelect = {onDateSelect(it)}, textMeasurer = textMeasurer, modifier = modifier)
}
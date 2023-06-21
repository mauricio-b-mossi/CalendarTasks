package com.example.canvasexperimentation.data

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime

data class Task(
    val title: String,
    val color: Color,
    val status: Status,
    val taskDateTime: Pair<LocalDateTime?, LocalDateTime?>? = null
)

enum class Status {
    DONE,
    IN_PROGRESS,
    POSTPONED,
    PAST_DUE,
}

package com.example.planningtime

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Task(
    val title: String,
    val description: String,
    val dateTime: Date,
    var progress: Int = 0,
    var isSelected: Boolean = false,
    var type: TaskType = TaskType.WORK,
    var impact: TaskImpact = TaskImpact.MEDIUM
) : Comparable<Task> {
    override fun compareTo(other: Task): Int {
        return this.dateTime.compareTo(other.dateTime)
    }

    fun getFormattedDateTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return formatter.format(dateTime)
    }
}

enum class TaskImpact(val displayName: String) {
    VERY_LOW("Muy Bajo"),
    LOW("Bajo"),
    MEDIUM("Medio"),
    HIGH("Alto"),
    VERY_HIGH("Muy Alto")
}

enum class TaskType(val displayName: String) {
    WORK("Trabajo"),
    STUDY("Estudio"),
    ENTERTAINMENT("Entretenimiento"),
    CHORES("Quehaceres"),
    SPORTS("Deportes"),
    HEALTH("Salud"),
    FINANCE("Finanzas"),
    SOCIAL("Social"),
    OTHER("Otros")
}








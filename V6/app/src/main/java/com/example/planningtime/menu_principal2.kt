package com.example.planningtime

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.*

class menu_principal2 : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewProgress: TextView
    private lateinit var taskDatabaseHelper: TaskDatabaseHelper
    private lateinit var calendarView: CalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        taskDatabaseHelper = TaskDatabaseHelper(this)

        progressBar = findViewById(R.id.progressBar)
        textViewProgress = findViewById(R.id.textViewProgress)
        calendarView = findViewById(R.id.calendarView)

        val movetonotas = findViewById<ImageButton>(R.id.imageButtonNotes)
        movetonotas.setOnClickListener {
            val intent = Intent(this@menu_principal2, pantalla_notas::class.java)
            startActivity(intent)
        }

        val moveToTasks = findViewById<ImageButton>(R.id.imageButtonTasks)
        moveToTasks.setOnClickListener {
            val intent = Intent(this@menu_principal2, TaskActivity::class.java)
            startActivity(intent)
        }

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            showTasksForDate(selectedDate)
        }
    }

    override fun onResume() {
        super.onResume()
        updateTaskCompletionPercentage()
    }

    private fun updateTaskCompletionPercentage() {
        val tasks = taskDatabaseHelper.getAllTasks()
        var sumaProg = 0
        var elementos = 0
        for(tarea in tasks){
            val currentTime = System.currentTimeMillis()
            val timeRemaining = tarea.dateTime.time - currentTime
            if(timeRemaining>0){
                elementos++
                sumaProg+=tarea.progress
            }
        }

        //val totalTasks = tasks.size
        //val totalProgress = tasks.sumBy { it.progress }

        val completionPercentage = if (elementos > 0) {
            (sumaProg / elementos)
        } else {
            0
        }

        progressBar.progress = completionPercentage
        textViewProgress.text = "Has completado este porcentaje de tus tareas: $completionPercentage%"
    }

    private fun showTasksForDate(date: String) {
        val tasks = taskDatabaseHelper.getTasksByDate(date)
        if (tasks.isNotEmpty()) {
            val task = tasks[0] // Suponemos que mostramos la primera tarea si hay múltiples
            val intent = Intent(this, TaskDetailActivity::class.java).apply {
                putExtra("id", task.id)
                putExtra("title", task.title)
                putExtra("description", task.description)
                putExtra("dateTime", SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(task.dateTime))
                putExtra("progress", task.progress)
                putExtra("impact", task.impact.name)
            }
            startActivity(intent)
        } else {
            // Aquí puedes mostrar un mensaje diciendo que no hay tareas para esta fecha
        }
    }
}

package com.example.planningtime

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class menu_principal2 : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewProgress: TextView
    private lateinit var taskDatabaseHelper: TaskDatabaseHelper

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
    }

    override fun onResume() {
        super.onResume()
        updateTaskCompletionPercentage()
    }

    private fun updateTaskCompletionPercentage() {
        val tasks = taskDatabaseHelper.getAllTasks()
        val totalTasks = tasks.size
        val totalProgress = tasks.sumBy { it.progress }

        val completionPercentage = if (totalTasks > 0) {
            (totalProgress / totalTasks)
        } else {
            0
        }

        progressBar.progress = completionPercentage
        textViewProgress.text = "Has completado este porcentaje de tus tareas: $completionPercentage%"
    }
}

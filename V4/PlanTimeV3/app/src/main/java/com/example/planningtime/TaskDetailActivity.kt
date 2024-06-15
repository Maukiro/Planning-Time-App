package com.example.planningtime

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResult
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date

class TaskDetailActivity : AppCompatActivity() {
    private lateinit var titleText: TextView
    private lateinit var descriptionText: TextView
    private lateinit var timeText: TextView
    private lateinit var timerText: TextView
    private lateinit var progressText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var startButton: Button
    private lateinit var cancelButton: Button
    private lateinit var editButton: Button
    private var timer: CountDownTimer? = null
    private var pomodoroSessionCount = 0
    private var task: Task? = null
    private lateinit var impactText: TextView
    private lateinit var taskDatabaseHelper: TaskDatabaseHelper

    private val editTaskLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let {
                val updatedTask = it.getSerializableExtra("updatedTask") as Task
                task = updatedTask
                updateTaskDetails()
                taskDatabaseHelper.updateTask(updatedTask)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        taskDatabaseHelper = TaskDatabaseHelper(this)

        titleText = findViewById(R.id.detailTitle)
        descriptionText = findViewById(R.id.detailDescription)
        timeText = findViewById(R.id.detailTime)
        timerText = findViewById(R.id.timerText)
        progressText = findViewById(R.id.progressText)
        progressBar = findViewById(R.id.progressBar)
        startButton = findViewById(R.id.startButton)
        cancelButton = findViewById(R.id.cancelButton)
        editButton = findViewById(R.id.editButton)
        impactText = findViewById(R.id.detailImpact)

        val id = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val dateTimeString = intent.getStringExtra("dateTime")
        val progress = intent.getIntExtra("progress", 0)
        val impactString = intent.getStringExtra("impact")

        titleText.text = title
        descriptionText.text = description
        timeText.text = dateTimeString
        progressText.text = "$progress%"
        progressBar.progress = progress
        impactText.text = getImpactDisplayName(impactString)

        if (id != -1 && title != null && description != null && dateTimeString != null && impactString != null) {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val dateTime = formatter.parse(dateTimeString)
            val impact = TaskImpact.valueOf(impactString)
            task = Task(id, title, description, dateTime, progress, impact = impact)
        }

        startButton.setOnClickListener {
            startPomodoroTimer()
        }

        cancelButton.setOnClickListener {
            showProgressDialog()
        }

        editButton.setOnClickListener {
            task?.let {
                val intent = Intent(this, EditTaskActivity::class.java).apply {
                    putExtra("task", it)
                }
                editTaskLauncher.launch(intent)
            }
        }
    }

    private fun startPomodoroTimer() {
        pomodoroSessionCount = 0
        startButton.visibility = Button.GONE
        cancelButton.visibility = Button.VISIBLE
        timerText.visibility = TextView.VISIBLE
        startNextPomodoroSession()
    }

    private fun getImpactDisplayName(impactString: String?): String {
        return when (impactString) {
            TaskImpact.VERY_LOW.name -> TaskImpact.VERY_LOW.displayName
            TaskImpact.LOW.name -> TaskImpact.LOW.displayName
            TaskImpact.MEDIUM.name -> TaskImpact.MEDIUM.displayName
            TaskImpact.HIGH.name -> TaskImpact.HIGH.displayName
            TaskImpact.VERY_HIGH.name -> TaskImpact.VERY_HIGH.displayName
            else -> "Impacto Desconocido"
        }
    }


    private fun startNextPomodoroSession() {
        val workDuration = 25 * 60 * 1000L
        val shortBreakDuration = 5 * 60 * 1000L
        val longBreakDuration = 20 * 60 * 1000L

        val isWorkSession = pomodoroSessionCount % 2 == 0
        val isLongBreak = pomodoroSessionCount == 7

        val duration = when {
            isLongBreak -> longBreakDuration
            isWorkSession -> workDuration
            else -> shortBreakDuration
        }

        timer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerText.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                pomodoroSessionCount++
                if (pomodoroSessionCount < 8) {
                    startNextPomodoroSession()
                } else {
                    pomodoroSessionCount = 0
                    showProgressDialog()
                }
            }
        }.start()
    }

    private fun showProgressDialog() {
        val progressOptions = arrayOf("0%", "25%", "50%", "75%", "100%")
        AlertDialog.Builder(this)
            .setTitle("Progreso de la Tarea")
            .setItems(progressOptions) { dialog, which ->
                val progress = when (which) {
                    0 -> 0
                    1 -> 25
                    2 -> 50
                    3 -> 75
                    4 -> 100
                    else -> 0
                }
                task?.let {
                    it.progress = progress
                    progressText.text = "$progress%"
                    progressBar.progress = progress
                    taskDatabaseHelper.updateTask(it) // Guardar el progreso actualizado en la base de datos
                }
                cancelPomodoroTimer()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
                cancelPomodoroTimer()
            }
            .show()
    }

    private fun cancelPomodoroTimer() {
        timer?.cancel()
        timerText.text = "25:00"
        timerText.visibility = TextView.GONE
        startButton.visibility = Button.VISIBLE
        cancelButton.visibility = Button.GONE
    }

    private fun updateTaskDetails() {
        task?.let {
            titleText.text = it.title
            descriptionText.text = it.description
            timeText.text = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(it.dateTime)
            progressText.text = "${it.progress}%"
            progressBar.progress = it.progress
            impactText.text = it.impact.displayName
        }
    }
}

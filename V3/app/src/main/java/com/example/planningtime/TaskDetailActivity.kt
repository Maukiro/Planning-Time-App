package com.example.planningtime

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
    private var timer: CountDownTimer? = null
    private var pomodoroSessionCount = 0
    private var task: Task? = null
    private lateinit var impactText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        titleText = findViewById(R.id.detailTitle)
        descriptionText = findViewById(R.id.detailDescription)
        timeText = findViewById(R.id.detailTime)
        timerText = findViewById(R.id.timerText)
        progressText = findViewById(R.id.progressText)
        progressBar = findViewById(R.id.progressBar)
        startButton = findViewById(R.id.startButton)
        cancelButton = findViewById(R.id.cancelButton)
        impactText = findViewById(R.id.detailImpact)

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
        impactText.text = impactString

        if (title != null && description != null && dateTimeString != null && impactString != null) {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val dateTime = formatter.parse(dateTimeString)
            val impact = TaskImpact.valueOf(impactString)
            task = Task(title, description, dateTime, progress, impact = impact)
        }

        startButton.setOnClickListener {
            startPomodoroTimer()
        }

        cancelButton.setOnClickListener {
            showProgressDialog()
        }
    }

    private fun startPomodoroTimer() {
        pomodoroSessionCount = 0
        startButton.visibility = Button.GONE
        cancelButton.visibility = Button.VISIBLE
        timerText.visibility = TextView.VISIBLE
        startNextPomodoroSession()
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
                task?.progress = progress
                progressText.text = "$progress%"
                progressBar.progress = progress
                cancelPomodoroTimer()
                // Aquí podrías guardar el progreso actualizado en una base de datos o donde corresponda
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
}

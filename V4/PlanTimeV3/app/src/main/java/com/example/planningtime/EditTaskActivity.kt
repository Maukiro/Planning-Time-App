package com.example.planningtime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class EditTaskActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateTimeButton: Button
    private lateinit var taskTypeSpinner: Spinner
    private lateinit var impactSpinner: Spinner
    private var selectedDateTime: Date? = null
    private var task: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        titleEditText = findViewById(R.id.editTextTitle)
        descriptionEditText = findViewById(R.id.editTextDescription)
        dateTimeButton = findViewById(R.id.buttonDateTime)
        taskTypeSpinner = findViewById(R.id.spinnerTaskType)
        impactSpinner = findViewById(R.id.spinnerTaskImpact)
        val saveButton = findViewById<Button>(R.id.buttonSave)

        // Configurar el Spinner de tipos de tarea con nombres en español
        val taskTypes = TaskType.values().map { it.displayName }.toTypedArray()
        val taskTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, taskTypes)
        taskTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        taskTypeSpinner.adapter = taskTypeAdapter

        // Configurar el Spinner de impacto de tarea con nombres en español
        val taskImpacts = TaskImpact.values().map { it.displayName }.toTypedArray()
        val impactAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, taskImpacts)
        impactAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        impactSpinner.adapter = impactAdapter

        task = intent.getSerializableExtra("task") as Task?
        task?.let {
            titleEditText.setText(it.title)
            descriptionEditText.setText(it.description)
            selectedDateTime = it.dateTime
            dateTimeButton.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(it.dateTime)
            taskTypeSpinner.setSelection(it.type.ordinal)
            impactSpinner.setSelection(it.impact.ordinal)
        }

        dateTimeButton.setOnClickListener {
            showDateTimePicker { dateTime ->
                selectedDateTime = dateTime
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                dateTimeButton.text = formatter.format(dateTime)
            }
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val selectedTaskTypeIndex = taskTypeSpinner.selectedItemPosition
            val selectedTaskType = TaskType.values()[selectedTaskTypeIndex]
            val selectedImpactIndex = impactSpinner.selectedItemPosition
            val selectedImpact = TaskImpact.values()[selectedImpactIndex]

            if (title.isNotEmpty() && description.isNotEmpty() && selectedDateTime != null) {
                val updatedTask = task?.copy(
                    title = title,
                    description = description,
                    dateTime = selectedDateTime!!,
                    type = selectedTaskType,
                    impact = selectedImpact
                )

                val resultIntent = Intent().apply {
                    putExtra("updatedTask", updatedTask)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private fun showDateTimePicker(onDateTimeSelected: (Date) -> Unit) {
        val currentDateTime = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val selectedDateTime = Calendar.getInstance()
                selectedDateTime.set(year, month, dayOfMonth, hourOfDay, minute)
                onDateTimeSelected(selectedDateTime.time)
            }

            TimePickerDialog(
                this, timeSetListener,
                currentDateTime.get(Calendar.HOUR_OF_DAY), currentDateTime.get(Calendar.MINUTE), true
            ).show()
        }

        DatePickerDialog(
            this, dateSetListener,
            currentDateTime.get(Calendar.YEAR), currentDateTime.get(Calendar.MONTH), currentDateTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}

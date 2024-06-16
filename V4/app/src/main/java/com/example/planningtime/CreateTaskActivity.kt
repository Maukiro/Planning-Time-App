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

class CreateTaskActivity : AppCompatActivity() {
    private var selectedDateTime: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)

        val titleEditText = findViewById<EditText>(R.id.editTextTitle)
        val descriptionEditText = findViewById<EditText>(R.id.editTextDescription)
        val saveButton = findViewById<Button>(R.id.buttonSave)
        val dateTimeButton = findViewById<Button>(R.id.buttonDateTime)
        val taskTypeSpinner = findViewById<Spinner>(R.id.spinnerTaskType)
        val impactSpinner = findViewById<Spinner>(R.id.spinnerTaskImpact)

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
                val resultIntent = Intent().apply {
                    putExtra("title", title)
                    putExtra("description", description)
                    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    putExtra("dateTime", formatter.format(selectedDateTime))
                    putExtra("taskType", selectedTaskType.name)
                    putExtra("taskImpact", selectedImpact.name)
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

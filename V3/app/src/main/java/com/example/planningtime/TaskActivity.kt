package com.example.planningtime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class TaskActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tasksAdapter: TasksAdapter
    private val tasksList = mutableListOf<Task>()
    private var allSelected = false
    private var currentFilter: TaskType? = null

    private val createTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let {
                val title = it.getStringExtra("title")
                val description = it.getStringExtra("description")
                val dateTimeString = it.getStringExtra("dateTime")
                val taskTypeString = it.getStringExtra("taskType")
                val taskImpactString = it.getStringExtra("taskImpact")
                if (title != null && description != null && dateTimeString != null && taskTypeString != null && taskImpactString != null) {
                    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val dateTime = formatter.parse(dateTimeString)
                    val taskType = TaskType.valueOf(taskTypeString)
                    val taskImpact = TaskImpact.valueOf(taskImpactString)
                    tasksList.add(Task(title, description, dateTime, type = taskType, impact = taskImpact))
                    tasksList.sort()  // Ordenar la lista de tareas por fecha y hora
                    filterTasks()  // Filtrar las tareas después de agregar una nueva
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tasksAdapter = TasksAdapter(tasksList) { task ->
            // Acción al hacer clic en la tarea (opcional)
        }
        recyclerView.adapter = tasksAdapter

        val createTaskButton = findViewById<Button>(R.id.buttonCreateTask)
        createTaskButton.setOnClickListener {
            val intent = Intent(this@TaskActivity, CreateTaskActivity::class.java)
            createTaskLauncher.launch(intent)
        }

        val selectDeselectAllButton = findViewById<Button>(R.id.buttonSelectDeselectAll)
        selectDeselectAllButton.setOnClickListener {
            allSelected = !allSelected
            tasksList.forEach { it.isSelected = allSelected }
            tasksAdapter.notifyDataSetChanged()
            selectDeselectAllButton.text = if (allSelected) "Deseleccionar Todas" else "Seleccionar Todas"
        }

        findViewById<Button>(R.id.buttonFilterWork).setOnClickListener { filterTasks(TaskType.WORK) }
        findViewById<Button>(R.id.buttonFilterStudy).setOnClickListener { filterTasks(TaskType.STUDY) }
        findViewById<Button>(R.id.buttonFilterEntertainment).setOnClickListener { filterTasks(TaskType.ENTERTAINMENT) }
        findViewById<Button>(R.id.buttonFilterChores).setOnClickListener { filterTasks(TaskType.CHORES) }
        findViewById<Button>(R.id.buttonFilterSports).setOnClickListener { filterTasks(TaskType.SPORTS) }
        findViewById<Button>(R.id.buttonFilterHealth).setOnClickListener { filterTasks(TaskType.HEALTH) }
        findViewById<Button>(R.id.buttonFilterFinance).setOnClickListener { filterTasks(TaskType.FINANCE) }
        findViewById<Button>(R.id.buttonFilterSocial).setOnClickListener { filterTasks(TaskType.SOCIAL) }
        findViewById<Button>(R.id.buttonFilterOther).setOnClickListener { filterTasks(TaskType.OTHER) }
    }

    private fun filterTasks(filter: TaskType? = null) {
        currentFilter = filter
        val filteredList = if (filter == null) {
            tasksList
        } else {
            tasksList.filter { it.type == filter }
        }
        tasksAdapter = TasksAdapter(filteredList) { task ->
            // Aquí manejas la acción de abrir los detalles de la tarea
            val intent = Intent(this, TaskDetailActivity::class.java).apply {
                putExtra("title", task.title)
                putExtra("description", task.description)
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                putExtra("dateTime", formatter.format(task.dateTime))
                putExtra("progress", task.progress)
            }
            startActivity(intent)
        }
        recyclerView.adapter = tasksAdapter
    }

}

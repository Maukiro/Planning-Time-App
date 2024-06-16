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
    private lateinit var taskDatabaseHelper: TaskDatabaseHelper

    private val createTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            loadTasksFromDatabase() // Recargar tareas desde la base de datos
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        taskDatabaseHelper = TaskDatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tasksAdapter = TasksAdapter(tasksList) { task ->
            // Acción al hacer clic en la tarea (opcional)
            val intent = Intent(this, TaskDetailActivity::class.java).apply {
                putExtra("id", task.id)
                putExtra("title", task.title)
                putExtra("description", task.description)
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                putExtra("dateTime", formatter.format(task.dateTime))
                putExtra("progress", task.progress)
                putExtra("impact", task.impact.name)
            }
            startActivity(intent)
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

        val deleteSelectedButton = findViewById<Button>(R.id.buttonDeleteSelected)
        deleteSelectedButton.setOnClickListener {
            deleteSelectedTasks()
        }

        findViewById<Button>(R.id.buttonFilterAll).setOnClickListener { filterTasks(null) }
        findViewById<Button>(R.id.buttonFilterSelected).setOnClickListener { filterTasks(selectedOnly = true) }
        findViewById<Button>(R.id.buttonFilterWork).setOnClickListener { filterTasks(TaskType.WORK) }
        findViewById<Button>(R.id.buttonFilterStudy).setOnClickListener { filterTasks(TaskType.STUDY) }
        findViewById<Button>(R.id.buttonFilterEntertainment).setOnClickListener { filterTasks(TaskType.ENTERTAINMENT) }
        findViewById<Button>(R.id.buttonFilterChores).setOnClickListener { filterTasks(TaskType.CHORES) }
        findViewById<Button>(R.id.buttonFilterSports).setOnClickListener { filterTasks(TaskType.SPORTS) }
        findViewById<Button>(R.id.buttonFilterHealth).setOnClickListener { filterTasks(TaskType.HEALTH) }
        findViewById<Button>(R.id.buttonFilterFinance).setOnClickListener { filterTasks(TaskType.FINANCE) }
        findViewById<Button>(R.id.buttonFilterSocial).setOnClickListener { filterTasks(TaskType.SOCIAL) }
        findViewById<Button>(R.id.buttonFilterOther).setOnClickListener { filterTasks(TaskType.OTHER) }

        loadTasksFromDatabase() // Cargar tareas desde la base de datos al iniciar
    }

    override fun onResume() {
        super.onResume()
        loadTasksFromDatabase() // Recargar tareas desde la base de datos cada vez que la actividad se reanuda
    }

    private fun loadTasksFromDatabase() {
        tasksList.clear()
        tasksList.addAll(taskDatabaseHelper.getAllTasks())
        tasksList.sortByDescending { calculatePriority(it) } // Ordenar tareas por prioridad
        tasksAdapter.notifyDataSetChanged()
    }

    private fun deleteSelectedTasks() {
        val selectedTasks = tasksList.filter { it.isSelected }
        selectedTasks.forEach { taskDatabaseHelper.deleteTask(it) }
        loadTasksFromDatabase() // Recargar la lista de tareas desde la base de datos
    }

    private fun filterTasks(filter: TaskType? = null, selectedOnly: Boolean = false) {
        currentFilter = filter
        val filteredList = when {
            selectedOnly -> tasksList.filter { it.isSelected }
            filter == null -> tasksList
            else -> tasksList.filter { it.type == filter }
        }
        tasksAdapter = TasksAdapter(filteredList) { task ->
            // Aquí manejas la acción de abrir los detalles de la tarea
            val intent = Intent(this, TaskDetailActivity::class.java).apply {
                putExtra("id", task.id)
                putExtra("title", task.title)
                putExtra("description", task.description)
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                putExtra("dateTime", formatter.format(task.dateTime))
                putExtra("progress", task.progress)
                putExtra("impact", task.impact.name)
            }
            startActivity(intent)
        }
        recyclerView.adapter = tasksAdapter
    }

    private fun calculatePriority(task: Task): Double {
        if (task.progress == 100) {
            return Double.MIN_VALUE // Las tareas completadas tienen la prioridad más baja
        }

        val currentTime = System.currentTimeMillis()
        val timeRemaining = task.dateTime.time - currentTime
        if(timeRemaining <=0){
            return Double.MIN_VALUE
        }
        val progress = task.progress
        var impact = when (task.impact) {
            TaskImpact.VERY_LOW -> 1
            TaskImpact.LOW -> 2
            TaskImpact.MEDIUM -> 3
            TaskImpact.HIGH -> 4
            TaskImpact.VERY_HIGH -> 5
        }
        val normalizedTime = timeRemaining / (1000.0 * 60 * 60 * 24) // días restantes
        val normalizedProgress = (100-progress) / 100.0 // progreso en porcentaje
        return (impact* (normalizedProgress)) / (normalizedTime + 1)
    }
}

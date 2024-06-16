package com.example.planningtime

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TasksAdapter(private val tasksList: List<Task>, private val itemClick: (Task) -> Unit) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        private val dateTimeTextView: TextView = view.findViewById(R.id.dateTimeTextView)
        private val progressText: TextView = view.findViewById(R.id.progressText)
        private val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        private val checkBox: CheckBox = view.findViewById(R.id.checkBox)
        private val typeColorView: View = view.findViewById(R.id.typeColorView)
        private val impactTextView: TextView = view.findViewById(R.id.impactTextView)

        fun bind(task: Task, clickListener: (Task) -> Unit) {
            titleTextView.text = task.title
            descriptionTextView.text = task.description
            dateTimeTextView.text = task.getFormattedDateTime()
            progressText.text = "${task.progress}%"
            progressBar.progress = task.progress
            checkBox.isChecked = task.isSelected
            typeColorView.setBackgroundColor(getColorForTaskType(task.type))
            impactTextView.text = task.impact.displayName

            val currentTime = System.currentTimeMillis()
            val timeRemaining = task.dateTime.time - currentTime
            var auxiliarText = ""
            if(task.progress==100){
                println("Existe una rara")
                auxiliarText = "[Terminada] "+task.title
                titleTextView.text = auxiliarText
            }else if(timeRemaining<=0){
                auxiliarText= "[Agotada] "+task.title
                titleTextView.text = auxiliarText
            }

            // Manejar la selección solo en el CheckBox
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                task.isSelected = isChecked
            }

            // Manejar el clic en todo el item para abrir los detalles
            itemView.setOnClickListener {
                if (!checkBox.isPressed) {
                    clickListener(task)
                }
            }
        }

        private fun getColorForTaskType(taskType: TaskType): Int {
            return when (taskType) {
                TaskType.WORK -> Color.RED
                TaskType.STUDY -> Color.BLUE
                TaskType.ENTERTAINMENT -> Color.GREEN
                TaskType.CHORES -> Color.YELLOW
                TaskType.SPORTS -> Color.MAGENTA
                TaskType.HEALTH -> Color.CYAN
                TaskType.FINANCE -> Color.LTGRAY
                TaskType.SOCIAL -> Color.DKGRAY
                TaskType.OTHER -> Color.GRAY
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasksList[position], itemClick)
    }

    override fun getItemCount() = tasksList.size
}

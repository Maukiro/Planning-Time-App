package com.example.planningtime

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Locale

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "sharedDatabase", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS tareas (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT, fecha_hora TEXT, progreso INTEGER, tipo TEXT, impacto TEXT)")
        // Crear las otras tablas si no existen
        db?.execSQL("CREATE TABLE IF NOT EXISTS usuarios (correo TEXT PRIMARY KEY, contraseña TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS notas (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,titulo TEXT , descripcion TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS tareas")
        onCreate(db)
    }

    fun getAllTasks(): List<Task> {
        val db = this.readableDatabase
        val cursor = db.query("tareas", null, null, null, null, null, null)
        val tasks = mutableListOf<Task>()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val title = cursor.getString(cursor.getColumnIndexOrThrow("titulo"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
                val dateTimeString = cursor.getString(cursor.getColumnIndexOrThrow("fecha_hora"))
                val dateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(dateTimeString)
                val progress = cursor.getInt(cursor.getColumnIndexOrThrow("progreso"))
                val type = TaskType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("tipo")))
                val impact = TaskImpact.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("impacto")))
                tasks.add(Task(id, title, description, dateTime, progress, type = type, impact = impact))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tasks
    }

    fun addTask(task: Task): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("titulo", task.title)
            put("descripcion", task.description)
            put("fecha_hora", SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(task.dateTime))
            put("progreso", task.progress)
            put("tipo", task.type.name)
            put("impacto", task.impact.name)
        }
        return db.insert("tareas", null, values)
    }

    fun updateTask(task: Task) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("titulo", task.title)
            put("descripcion", task.description)
            put("fecha_hora", SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(task.dateTime))
            put("progreso", task.progress)
            put("tipo", task.type.name)
            put("impacto", task.impact.name)
        }
        db.update("tareas", values, "id = ?", arrayOf(task.id.toString()))
    }

    fun updateTaskProgress(task: Task) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("progreso", task.progress)
        }
        db.update("tareas", values, "id = ?", arrayOf(task.id.toString()))
    }

    fun deleteTask(task: Task) {
        val db = this.writableDatabase
        db.delete("tareas", "id = ?", arrayOf(task.id.toString()))
    }
}
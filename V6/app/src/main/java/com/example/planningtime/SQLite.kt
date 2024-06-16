package com.example.planningtime

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Locale

class SQLite(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, "sharedDatabase", factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE IF NOT EXISTS usuarios (correo TEXT PRIMARY KEY, contraseña TEXT)")
        db?.execSQL("CREATE TABLE IF NOT EXISTS notas (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,titulo TEXT , descripcion TEXT)")
        // Crear las otras tablas si no existen
        db?.execSQL("CREATE TABLE IF NOT EXISTS tareas (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT, descripcion TEXT, fecha_hora TEXT, progreso INTEGER, tipo TEXT, impacto TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Implementar si es necesario
    }

    fun getAllNotes(): MutableList<Note> {
        val notesList = mutableListOf<Note>()
        val db = readableDatabase
        val cursor: Cursor? = db?.query("notas", arrayOf("titulo", "descripcion"), null, null, null, null, null)

        cursor?.use {
            while (it.moveToNext()) {
                val titulo = it.getString(it.getColumnIndex("titulo"))
                val descripcion = it.getString(it.getColumnIndex("descripcion"))
                notesList.add(Note(titulo, descripcion))
            }
        }

        cursor?.close()
        db?.close()

        return notesList
    }
}
package com.example.planningtime

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class SQLite(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE uduarios (correo TEXT PRIMARY KEY, contraseña TEXT)")
        db?.execSQL("CREATE TABLE notas (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,titulo TEXT , descripcion TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

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
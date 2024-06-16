package com.example.planningtime

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditarNotaActivity : AppCompatActivity() {
    private lateinit var tituloEditText: EditText
    private lateinit var descripcionEditText: EditText
    private lateinit var dbHelper: SQLite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_nota)

        dbHelper = SQLite(this, "sharedDatabase", null, 1)

        tituloEditText = findViewById(R.id.editTextTitulo)
        descripcionEditText = findViewById(R.id.editTextDescripcion)

        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")

        tituloEditText.setText(titulo)
        descripcionEditText.setText(descripcion)

        val guardarButton = findViewById<Button>(R.id.buttonGuardar)
        guardarButton.setOnClickListener {
            guardarNota()
        }
    }

    private fun guardarNota() {
        val newTitulo = tituloEditText.text.toString()
        val newDescripcion = descripcionEditText.text.toString()

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("titulo", newTitulo)
            put("descripcion", newDescripcion)
        }
        db.update("notas", values, "titulo = ? AND descripcion = ?", arrayOf(intent.getStringExtra("titulo"), intent.getStringExtra("descripcion")))

        val resultIntent = Intent().apply {
            putExtra("newTitulo", newTitulo)
            putExtra("newDescripcion", newDescripcion)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}

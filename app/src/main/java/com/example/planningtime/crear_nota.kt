package com.example.planningtime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class crear_nota : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_nota)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tituloEditText = findViewById<EditText>(R.id.editTextTitulo)
        val descripcionEditText = findViewById<EditText>(R.id.editTextDescripcion)
        val guardarButton = findViewById<Button>(R.id.buttonGuardar)

        guardarButton.setOnClickListener {
            val titulo = tituloEditText.text.toString()
            val descripcion = descripcionEditText.text.toString()

            if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {
                val resultIntent = Intent()
                resultIntent.putExtra("titulo", titulo)
                resultIntent.putExtra("descripcion", descripcion)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }
}

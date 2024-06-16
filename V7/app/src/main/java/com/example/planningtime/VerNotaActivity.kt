package com.example.planningtime

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class VerNotaActivity : AppCompatActivity() {
    private lateinit var tituloTextView: TextView
    private lateinit var descripcionTextView: TextView

    private val editNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                val newTitulo = it.getStringExtra("newTitulo")
                val newDescripcion = it.getStringExtra("newDescripcion")

                if (newTitulo != null && newDescripcion != null) {
                    tituloTextView.text = newTitulo
                    descripcionTextView.text = newDescripcion
                    // Update the result to send back to pantalla_notas
                    val resultIntent = Intent().apply {
                        putExtra("updatedTitulo", newTitulo)
                        putExtra("updatedDescripcion", newDescripcion)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_nota)

        val titulo = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")

        tituloTextView = findViewById(R.id.textViewTitulo)
        descripcionTextView = findViewById(R.id.textViewDescripcion)

        tituloTextView.text = titulo
        descripcionTextView.text = descripcion

        val editarButton = findViewById<Button>(R.id.buttonEditar)
        editarButton.setOnClickListener {
            val intent = Intent(this, EditarNotaActivity::class.java).apply {
                putExtra("titulo", titulo)
                putExtra("descripcion", descripcion)
            }
            editNoteLauncher.launch(intent)
        }
    }
}

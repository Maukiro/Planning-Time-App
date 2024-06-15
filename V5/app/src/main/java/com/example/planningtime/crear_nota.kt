package com.example.planningtime

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

//        guardarButton.setOnClickListener {
//            val titulo = tituloEditText.text.toString()
//            val descripcion = descripcionEditText.text.toString()
//
//            if (titulo.isNotEmpty() && descripcion.isNotEmpty()) {
//                val resultIntent = Intent()
//                resultIntent.putExtra("titulo", titulo)
//                resultIntent.putExtra("descripcion", descripcion)
//                setResult(Activity.RESULT_OK, resultIntent)
//                finish()
//            }
//        }

    }

    fun guardarNota(view: View){
        // Obtener las referencias a los EditText
        val tituloEditText = findViewById<EditText>(R.id.editTextTitulo)
        val descripcionEditText = findViewById<EditText>(R.id.editTextDescripcion)
        //Definir base de datos
        var con=SQLite(this,"datos",null,1)
        var baseDatos =con.writableDatabase
        // Obtener los textos de email y password
        var tituloText = tituloEditText.text.toString()
        var descripcionText = descripcionEditText.text.toString()
        // Mostrar un mensaje Toast con los datos obtenidos
        //var message = "TITULO DE NOTA: $tituloText\n Descripcion NOta: $descripcionText"
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        if(tituloText.isEmpty()==false && descripcionText.isEmpty()==false){
            var registro= ContentValues()
            registro.put("titulo",tituloText)
            registro.put("descripcion",descripcionText)
            baseDatos.insert("notas",null,registro)
            val resultIntent = Intent()
            resultIntent.putExtra("titulo", tituloText)
            resultIntent.putExtra("descripcion", descripcionText)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
            tituloEditText?.setText("")
            descripcionEditText?.setText("")
            Toast.makeText(this, "Se ha creado la nota correctamente", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
        }
    }
}
package com.example.planningtime

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
import kotlin.coroutines.Continuation

class registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun registrar(view: View) {
        // Obtener las referencias a los EditText
        var editTextText = findViewById<EditText>(R.id.editTextText)
        var editTextEmail = findViewById<EditText>(R.id.editTextTextEmailAddress)
        var editTextPassword = findViewById<EditText>(R.id.editTextTextPassword2)
        var con=SQLite(this,"sharedDatabase",null,1)
        var baseDatos =con.writableDatabase
        // Obtener los textos de email y password
        var nombreUsuarioText = editTextText.text.toString()
        var emailText = editTextEmail.text.toString()
        var passwordText = editTextPassword.text.toString()
        //Mostrar un mensaje Toast con los datos obtenidos
        //var message = "Nombre de Usuario:$nombreUsuarioText Email: $emailText\nPassword: $passwordText"
        //Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        if(nombreUsuarioText.isEmpty()==false && emailText.isEmpty()==false && passwordText.isEmpty()==false){
            var registro= ContentValues()
            registro.put("nombreUsuario",nombreUsuarioText)
            registro.put("correo",emailText)
            registro.put("contraseña",passwordText)
            baseDatos.insert("usuarios",null,registro)
            editTextText?.setText("")
            editTextEmail?.setText("")
            editTextPassword?.setText("")
            Toast.makeText(this, "Se ha insertado correctamente", Toast.LENGTH_LONG).show()
            val movetomenu = findViewById<Button>(R.id.buttonRegistrar)
            movetomenu.setOnClickListener {
                val intent = Intent(this@registro, menu_principal2::class.java)
                startActivity(intent)
            }
        }else{
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
        }
    }
}
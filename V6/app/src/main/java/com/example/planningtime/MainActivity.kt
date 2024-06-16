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
import android.database.Cursor

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }






    }
    fun insertar(view: View){
        var editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        var editTextPassword = findViewById<EditText>(R.id.editTextTextPassword)
        var con = SQLite(this, "sharedDatabase", null, 1)
        var baseDatos = con.writableDatabase
        var emailText = editTextEmail.text.toString()
        var passwordText = editTextPassword.text.toString()

        if(emailText.isNotEmpty() && passwordText.isNotEmpty()){
            var registro = ContentValues().apply {
                put("correo", emailText)
                put("contraseña", passwordText)
            }
            baseDatos.insert("usuarios", null, registro)
            editTextEmail.setText("")
            editTextPassword.setText("")
            Toast.makeText(this, "Se ha insertado correctamente", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
        }
    }

    fun buscar(view: View){
        var editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        var editTextPassword = findViewById<EditText>(R.id.editTextTextPassword)
        var con = SQLite(this, "sharedDatabase", null, 1)
        var baseDatos = con.readableDatabase
        var emailText = editTextEmail.text.toString()
        var passwordText = editTextPassword.text.toString()

        if(emailText.isNotEmpty() && passwordText.isNotEmpty()){
            val cursor: Cursor = baseDatos.rawQuery("SELECT * FROM usuarios WHERE correo=? AND contraseña=?", arrayOf(emailText, passwordText))
            if (cursor.moveToFirst()) {
                Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_LONG).show()
                val movetomenu = findViewById<Button>(R.id.button_iniciar)
                movetomenu.setOnClickListener {
                    val intent = Intent(this@MainActivity, menu_principal2::class.java)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_LONG).show()
            }
            cursor.close()
        } else {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
        }
    }

}
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
        var baseDatosConsulta = con.readableDatabase
        var emailText = editTextEmail.text.toString()
        var passwordText = editTextPassword.text.toString()


        if(emailText.isNotEmpty() && passwordText.isNotEmpty()){
            val cursor: Cursor = baseDatosConsulta.rawQuery("SELECT * FROM usuarios WHERE correo=? ", arrayOf(emailText))
            if (!cursor.moveToFirst()) {
                var registro = ContentValues().apply {
                    put("correo", emailText)
                    put("contraseña", passwordText)
                }
                baseDatos.insert("usuarios", null, registro)
                editTextEmail.setText("")
                editTextPassword.setText("")
                Toast.makeText(this, "Se ha insertado correctamente", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "El usuario ya existía", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_LONG).show()
        }
        /*println("Borrando cosas")
        var con = SQLite(this, "sharedDatabase", null, 1)
        var baseDatos = con.writableDatabase
        baseDatos.execSQL("delete from usuarios")
        var baseDatosConsulta = con.readableDatabase
        val cursor: Cursor = baseDatosConsulta.rawQuery("SELECT * FROM usuarios", arrayOf())
        if (cursor.moveToFirst()){
            Toast.makeText(this, "No se pudo :(", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, "Se borraron los datos ;)", Toast.LENGTH_LONG).show()
            for(i in 1..430){
                var registro = ContentValues().apply {
                    put("correo", i.toString())
                    put("contraseña", i.toString())
                }
                baseDatos.insert("usuarios", null, registro)
            }
        }*/
    }

    fun buscar(view: View){
        println("Invocado en buscar")
        var editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        var editTextPassword = findViewById<EditText>(R.id.editTextTextPassword)
        var con = SQLite(this, "sharedDatabase", null, 1)
        var baseDatos = con.readableDatabase
        var emailText = editTextEmail.text.toString()
        var passwordText = editTextPassword.text.toString()

        println(emailText)
        println(passwordText)
        if(emailText.isNotEmpty() && passwordText.isNotEmpty()){
            val cursor: Cursor = baseDatos.rawQuery("SELECT * FROM usuarios WHERE correo=?", arrayOf(emailText))
            if (cursor.moveToFirst()) {
                val cursor: Cursor = baseDatos.rawQuery("SELECT * FROM usuarios WHERE correo=? AND contraseña=?", arrayOf(emailText, passwordText))
                if (cursor.moveToFirst()) {
                    Toast.makeText(this, "Usuario encontrado", Toast.LENGTH_LONG).show()
                    val movetomenu = findViewById<Button>(R.id.button_iniciar)
                    movetomenu.setOnClickListener {
                        val intent = Intent(this@MainActivity, menu_principal2::class.java)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_LONG).show()
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
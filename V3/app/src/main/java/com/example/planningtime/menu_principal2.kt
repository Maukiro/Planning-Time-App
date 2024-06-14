package com.example.planningtime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class menu_principal2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val movetonotas = findViewById<ImageButton>(R.id.imageButtonNotes)
        movetonotas.setOnClickListener {
            val intent = Intent(this@menu_principal2, pantalla_notas::class.java)
            startActivity(intent)
        }

        val moveToTasks = findViewById<ImageButton>(R.id.imageButtonTasks)
        moveToTasks.setOnClickListener {
            val intent = Intent(this@menu_principal2, TaskActivity::class.java)
            startActivity(intent)
        }
    }
}
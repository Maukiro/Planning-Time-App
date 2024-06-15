package com.example.planningtime

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class pantalla_notas : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private val notesList = mutableListOf<Note>()
    private lateinit var dbHelper: SQLite

    private val createNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let {

                val titulo = it.getStringExtra("titulo")
                val descripcion = it.getStringExtra("descripcion")
                if (titulo != null && descripcion != null) {
                    notesList.add(Note(titulo, descripcion))
                    notesAdapter.notifyItemInserted(notesList.size - 1)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_notas)

        dbHelper = SQLite(this,"datos",null,1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerViewNotas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(notesList)
        recyclerView.adapter = notesAdapter

        // Cargar datos desde la base de datos
        loadNotesFromDatabase()

        val crearNotasButton = findViewById<Button>(R.id.buttonCrearNota)
        crearNotasButton.setOnClickListener {
            val intent = Intent(this@pantalla_notas, crear_nota::class.java)
            createNoteLauncher.launch(intent)
        }

    }

    private fun loadNotesFromDatabase() {
        notesList.clear()
        notesList.addAll(dbHelper.getAllNotes())
        notesAdapter.notifyDataSetChanged()
    }
}
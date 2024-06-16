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
    private val notesList = mutableListOf<NoteItem>()
    private lateinit var dbHelper: SQLite
    private lateinit var selectAllButton: Button
    private lateinit var eliminarSeleccionadasButton: Button

    private val createNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let {
                val titulo = it.getStringExtra("titulo")
                val descripcion = it.getStringExtra("descripcion")
                if (titulo != null && descripcion != null) {
                    notesList.add(NoteItem(titulo, descripcion))
                    notesAdapter.notifyItemInserted(notesList.size - 1)
                }
            }
        }
    }

    private val viewNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let {
                val updatedTitulo = it.getStringExtra("updatedTitulo")
                val updatedDescripcion = it.getStringExtra("updatedDescripcion")
                if (updatedTitulo != null && updatedDescripcion != null) {
                    val position = notesList.indexOfFirst { note -> note.titulo == updatedTitulo && note.descripcion == updatedDescripcion }
                    if (position != -1) {
                        notesList[position].titulo = updatedTitulo
                        notesList[position].descripcion = updatedDescripcion
                        notesAdapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pantalla_notas)

        dbHelper = SQLite(this, "sharedDatabase", null, 1)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerViewNotas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        notesAdapter = NotesAdapter(notesList) { note ->
            val intent = Intent(this, VerNotaActivity::class.java).apply {
                putExtra("titulo", note.titulo)
                putExtra("descripcion", note.descripcion)
            }
            viewNoteLauncher.launch(intent)
        }
        recyclerView.adapter = notesAdapter

        // Cargar datos desde la base de datos
        loadNotesFromDatabase()

        val crearNotasButton = findViewById<Button>(R.id.buttonCrearNota)
        crearNotasButton.setOnClickListener {
            val intent = Intent(this@pantalla_notas, crear_nota::class.java)
            createNoteLauncher.launch(intent)
        }

        selectAllButton = findViewById<Button>(R.id.buttonSelectAll)
        selectAllButton.setOnClickListener {
            toggleSelectAll()
        }

        eliminarSeleccionadasButton = findViewById<Button>(R.id.buttonEliminarSeleccionadas)
        eliminarSeleccionadasButton.setOnClickListener {
            eliminarNotasSeleccionadas()
        }
    }

    override fun onResume() {
        super.onResume()
        loadNotesFromDatabase()
    }

    private fun loadNotesFromDatabase() {
        notesList.clear()
        val notesFromDB = dbHelper.getAllNotes().map { NoteItem(it.titulo, it.descripcion) }
        notesList.addAll(notesFromDB)
        notesAdapter.notifyDataSetChanged()
    }

    private fun toggleSelectAll() {
        val allSelected = notesList.all { it.isChecked }
        notesList.forEach { it.isChecked = !allSelected }
        notesAdapter.notifyDataSetChanged()
    }

    private fun eliminarNotasSeleccionadas() {
        val selectedNotes = notesList.filter { it.isChecked }
        selectedNotes.forEach { note ->
            dbHelper.writableDatabase.delete("notas", "titulo = ? AND descripcion = ?", arrayOf(note.titulo, note.descripcion))
        }
        notesList.removeAll(selectedNotes)
        notesAdapter.notifyDataSetChanged()
    }
}

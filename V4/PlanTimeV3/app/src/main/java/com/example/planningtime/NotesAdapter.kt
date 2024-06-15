package com.example.planningtime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class NoteItem(val titulo: String, val descripcion: String)

class NotesAdapter(private val notesList: List<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloTextView: TextView = view.findViewById(R.id.note_title)
        val descripcionTextView: TextView = view.findViewById(R.id.note_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.tituloTextView.text = note.titulo
        holder.descripcionTextView.text = note.descripcion
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}
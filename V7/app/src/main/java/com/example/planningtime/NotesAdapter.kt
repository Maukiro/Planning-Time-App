package com.example.planningtime

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class NoteItem(var titulo: String, var descripcion: String, var isChecked: Boolean = false)

class NotesAdapter(private val notesList: List<NoteItem>, private val onNoteClick: (NoteItem) -> Unit) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tituloTextView: TextView = view.findViewById(R.id.note_title)
        val descripcionTextView: TextView = view.findViewById(R.id.note_description)
        val checkBox: CheckBox = view.findViewById(R.id.note_checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notesList[position]
        holder.tituloTextView.text = note.titulo
        holder.descripcionTextView.text = note.descripcion
        holder.checkBox.isChecked = note.isChecked

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            note.isChecked = isChecked
        }

        holder.itemView.setOnClickListener {
            if (!holder.checkBox.isChecked) {
                onNoteClick(note)
            }
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}

package com.example.planningtime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.planningtime.R
import com.example.planningtime.metodos

class metodosAdapter(private val metodosList: List<metodos>) : RecyclerView.Adapter<metodosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): metodosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return metodosViewHolder(layoutInflater.inflate(R.layout.item_metodos,parent,false))

    }

    override fun getItemCount(): Int =  metodosList.size


    override fun onBindViewHolder(holder: metodosViewHolder, position: Int) {
        val item = metodosList[position]//position en la posiciona acceder, se me ocurre iterarla y asi mostrar uno por uno
        holder.render(item)
    }

}
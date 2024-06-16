package com.example.planningtime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.planningtime.R
import com.example.planningtime.consejos

class ConsejosAdapter(private val consejosList: List<consejos>
) :RecyclerView.Adapter<ConsejosViewHolder> (){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsejosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ConsejosViewHolder(layoutInflater.inflate(R.layout.item_consejos,parent,false))
    }

    override fun getItemCount(): Int = consejosList.size

    override fun onBindViewHolder(holder: ConsejosViewHolder, position: Int) {
        val item = consejosList[position]
        holder.render(item)
    }


}
package com.example.planningtime.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.planningtime.R
import com.example.planningtime.databinding.ItemConsejosBinding
import com.example.planningtime.databinding.ItemMetodosBinding
import com.example.planningtime.metodos

class metodosViewHolder(view: View):ViewHolder(view) {

    val binding = ItemMetodosBinding.bind(view)


    fun render(metodosModel:metodos){
        binding.textView5.text = metodosModel.nombreMetodo
        binding.textView4.text = metodosModel.explicacionMetodo
    }
}
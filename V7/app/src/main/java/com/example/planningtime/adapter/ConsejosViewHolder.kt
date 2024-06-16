package com.example.planningtime.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.planningtime.consejos
import com.example.planningtime.databinding.ItemConsejosBinding

class ConsejosViewHolder(view: View):RecyclerView.ViewHolder(view) {

    val binding = ItemConsejosBinding.bind(view)

    fun render(
        consejosModel: consejos,
    ){
        binding.textView3.text = consejosModel.consejo
    }
}
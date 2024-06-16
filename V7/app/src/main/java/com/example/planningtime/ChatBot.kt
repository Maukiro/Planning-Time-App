package com.example.planningtime

import android.os.Binder
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planningtime.adapter.ConsejosAdapter
import com.example.planningtime.adapter.metodosAdapter
import com.example.planningtime.databinding.ActivityChatBotBinding

class ChatBot : AppCompatActivity() {

    private  lateinit var binding: ActivityChatBotBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        consejosProvider.consejosList

        enableEdgeToEdge()
        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }
    fun verConsejos(view: View){
        initRecyclerView()
    }

    fun verMetodos(view: View){
        initRecyclerView2()
    }
    private fun initRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter =  ConsejosAdapter(consejosProvider.consejosList)
    }

    private fun initRecyclerView2(){
        binding.recyclerView2.layoutManager = LinearLayoutManager(this)
        binding.recyclerView2.adapter = metodosAdapter(metodosProvider.metodosLista)
    }

}
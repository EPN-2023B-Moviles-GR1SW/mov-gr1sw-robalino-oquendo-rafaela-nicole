package com.example.myapplication.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Entity.Chat
import com.example.myapplication.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

// Definición del adaptador, que extiende RecyclerView.Adapter y toma una lista de elementos de Chat
class ChatAdapter(private val chatList: List<Chat>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // Definición de la clase interna ViewHolder que contiene las vistas de cada elemento de chat
    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textUserName: TextView = itemView.findViewById(R.id.textUserName)
        val textMessage: TextView = itemView.findViewById(R.id.textMessage)
        val imageChat: ImageView = itemView.findViewById(R.id.imageChat)
    }

    // Método llamado cuando se necesita crear un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        // Inflar el diseño del elemento de chat y devolver un ViewHolder que lo contiene
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(itemView)
    }

    // Método llamado cuando se debe asociar un ViewHolder existente con datos
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        // Obtener el objeto Chat en la posición específica de la lista
        val currentChat = chatList[position]

        // Establecer los datos del chat en las vistas del ViewHolder
        holder.textUserName.text = currentChat.userName
        holder.textMessage.text = currentChat.message

        // Cargar la imagen del chat utilizando Glide
        Glide.with(holder.itemView)
            .load(currentChat.profileURL) // La URL de la imagen del mensaje de chat
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Opcional: Cachear la imagen para futuras cargas
            .into(holder.imageChat)
    }

    // Método que devuelve el número total de elementos en la lista de chats
    override fun getItemCount(): Int {
        return chatList.size
    }
}

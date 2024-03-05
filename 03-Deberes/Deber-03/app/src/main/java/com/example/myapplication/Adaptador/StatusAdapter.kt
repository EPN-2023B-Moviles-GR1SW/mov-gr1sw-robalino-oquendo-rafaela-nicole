package com.example.myapplication.Adaptador

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.Entity.Status
import com.example.myapplication.R

// Definición del adaptador para mostrar los estados (status)
class StatusAdapter(private val statusList: List<Status>):
    RecyclerView.Adapter<StatusAdapter.StatusViewHolder>() {

    // Definición de la clase interna ViewHolder que contiene las vistas de cada elemento de estado
    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.imageProfile)
        val textStory: TextView = itemView.findViewById(R.id.textStory)
        val textTime: TextView = itemView.findViewById(R.id.textTime)
        val statusImage: ImageView = itemView.findViewById(R.id.imageStatus)
    }

    // Método llamado cuando se necesita crear un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        // Inflar el diseño del elemento de estado y devolver un ViewHolder que lo contiene
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_status, parent, false)
        return StatusViewHolder(itemView)
    }

    // Método llamado cuando se debe asociar un ViewHolder existente con datos
    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        // Obtener el objeto Status en la posición específica de la lista
        val statusCurrent = statusList[position]

        // Establecer los datos del estado en las vistas del ViewHolder
        holder.textStory.text = statusCurrent.user
        holder.textTime.text = statusCurrent.timePublication

        // Cargar la imagen del perfil del estado utilizando Glide
        Glide.with(holder.itemView)
            .load(statusCurrent.profileImageURL) // La URL de la imagen del perfil
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Opcional: Cachear la imagen para futuras cargas
            .into(holder.profileImage)

        // Cargar la imagen del estado utilizando Glide, con esquinas redondeadas
        Glide.with(holder.itemView)
            .load(statusCurrent.imageStatusURL) // La URL de la imagen del estado
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16))) // Opcional: Redondear las esquinas de la imagen
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Opcional: Cachear la imagen para futuras cargas
            .into(holder.statusImage)
    }

    // Método que devuelve el número total de elementos en la lista de estados
    override fun getItemCount(): Int {
        return statusList.size
    }
}

package com.example.examen_ib.modelos

data class Libro(
    val nombreAutor_Libro: String,
    val titulo: String,
    val anioPublicacion: Int,
    var precio: Double,
    val genero: String,
){
    override fun toString(): String {
        return "Nombre_Autor: ${nombreAutor_Libro} \nTitulo: ${titulo} \nPrecio: ${precio}"
    }
}

package com.example.examen_ib.modelos

data class Libro(
    var nombreAutor_Libro: String,
    var titulo: String,
    var anioPublicacion: Int,
    var precio: Double,
    var genero: String,
){
    override fun toString(): String {
        return "Nombre_Autor: ${nombreAutor_Libro} \nTitulo: ${titulo} \nPrecio: ${precio}"
    }
}

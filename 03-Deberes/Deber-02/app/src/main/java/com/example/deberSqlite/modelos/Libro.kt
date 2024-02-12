package com.example.examen_ib.modelos

// Clase de datos que representa un libro
data class Libro(
    val id: Int, // Identificador único del libro
    var nombreAutor_Libro: String, // Nombre del autor del libro
    var titulo: String, // Título del libro
    var anioPublicacion: Int, // Año de publicación del libro
    var precio: Double, // Precio del libro
    var genero: String, // Género del libro
    val idAutor: Int // Identificador del autor del libro
) {
    // Sobrescritura del método toString para proporcionar una representación en cadena de caracteres del libro
    override fun toString(): String {
        return "Nombre_Autor: ${nombreAutor_Libro} \nTitulo: ${titulo} \nPrecio: ${precio}"
    }
}

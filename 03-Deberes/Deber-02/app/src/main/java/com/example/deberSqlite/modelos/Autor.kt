package com.example.examen_ib.modelos

import java.util.ArrayList

// Clase de datos que representa un autor
data class Autor (
    val id: Int, // Identificador único del autor
    var nombre: String, // Nombre del autor
    var pais: String, // País de origen del autor
    var edad: Int, // Edad del autor
    val listaLibros: ArrayList<Libro> // Lista de libros escritos por el autor
) {
    // Sobrescritura del método toString para proporcionar una representación en cadena de caracteres del autor
    override fun toString(): String {
        return "Nombre: ${nombre.uppercase()} - Pais: ${pais.uppercase()} \nEdad: ${edad}"
    }
}

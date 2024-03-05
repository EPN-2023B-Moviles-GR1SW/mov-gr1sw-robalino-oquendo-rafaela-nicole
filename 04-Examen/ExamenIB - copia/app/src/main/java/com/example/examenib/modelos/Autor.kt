package com.example.examen_ib.modelos

import java.util.ArrayList

data class Autor (
    var nombre: String,
    var pais: String,
    var edad: Int,
    val listaLibros: ArrayList<Libro>
){
    override fun toString(): String {
        return "Nombre: ${nombre.uppercase()} - Pais: ${pais.uppercase()} \nEdad: ${edad}"
    }
}
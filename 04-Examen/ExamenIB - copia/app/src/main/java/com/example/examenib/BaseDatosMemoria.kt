package com.example.examenib

import com.example.examen_ib.modelos.Autor
import com.example.examen_ib.modelos.Libro
import java.util.ArrayList
class BaseDatosMemoria {
    companion object{
        val arregloAutor = arrayListOf<Autor>()
        val listaLibros1: ArrayList<Libro> = arrayListOf()
        val listaLibros2: ArrayList<Libro> = arrayListOf()
        val listaLibros3: ArrayList<Libro> = arrayListOf()
        init {
            listaLibros1.add(Libro("GABRIEL_GARCIA_MARQUEZ", "CIEN_AÑOS_DE_SOLEDAD", 1967, 15.0, "REALISMO"))
            listaLibros1.add(Libro("GABRIEL_GARCIA_MARQUEZ", "AMOR_EN_TIEMPOS_DE_COLERA", 1985, 13.0, "ROMANCE"))
            listaLibros1.add(Libro("GABRIEL_GARCIA_MARQUEZ", "DEL_AMOR_Y_OTROS_DEMONIOS", 1994, 16.0, "FICCION"))

            listaLibros2.add(Libro("JK_ROWLING", "HARRY_POTTER_Y_LA_PIEDRA_FILOSOFAL", 1997, 20.0, "FANTASIA"))
            listaLibros2.add(Libro("JK_ROWLING", "HARRY_POTTER_Y_LAS_RELIQUIAS_DE_LA_MUERTE", 2007, 25.0, "FANTASIA"))

            listaLibros3.add(Libro("JANE_AUSTEN", "ORGULLO_Y_PREJUICIO", 1813, 15.0, "ROMANCE"))

            arregloAutor.add(Autor("GABRIEL_GARCIA_MARQUEZ", "COLOMBIA", 87, listaLibros1))
            arregloAutor.add(Autor("JK_ROWLING", "REINO_UNIDO", 58, listaLibros2))
            arregloAutor.add(Autor("JANE_AUSTEN", "REINO_UNIDO", 41, listaLibros3))
        }
    }
}
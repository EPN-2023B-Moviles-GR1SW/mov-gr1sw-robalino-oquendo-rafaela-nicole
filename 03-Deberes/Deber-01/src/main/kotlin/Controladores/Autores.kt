package Controladores

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import Modelos.Autor
import java.io.File

// Maneja la persistencia y operaciones relacionadas con autores en un archivo.
class Autores(private val archivo: File) {
    private val objectMapper = jacksonObjectMapper()  // Instancia de ObjectMapper para trabajar con JSON.
    val autors: MutableList<Autor> = mutableListOf()  // Lista mutable de autores.

    init {
        cargarAutores()  // Al instanciar la clase, carga los autores desde el archivo si existe.
    }

    // Carga autores desde el archivo al inicializar la clase.
    private fun cargarAutores() {
        // Verifica si el archivo de datos de autores existe en el sistema de archivos.
        if (archivo.exists()) {
            val autoresJson = archivo.readText()    // Lee el contenido del archivo como una cadena de texto.

            // Utiliza el ObjectMapper para convertir la cadena JSON en una lista de objetos Autor.
            autors.addAll(objectMapper.readValue<List<Autor>>(autoresJson))
        }
    }

    // Agrega un nuevo autor a la lista y guarda la lista actualizada en el archivo.
    fun crearAutor(autor: Autor) {
        autors.add(autor)
        guardarAutores()
    }

    // Devuelve la lista actual de autores.
    fun leerAutores(): List<Autor> {
        return autors.toList()
    }

    // Actualiza un autor existente por el nombre del autor.
    fun actualizarAutor(nombre: String, autorActualizado: Autor) {
        val autorIndex = autors.indexOfFirst {it.nombre == nombre }    // Busca el índice del autor en la lista basándose en el nombre.

        // Verifica si se encontró un autor con el nombre proporcionado.
        if (autorIndex != -1) {
            autors[autorIndex] = autorActualizado   // Actualiza el autor en la lista con el objeto Autor proporcionado.
            guardarAutores()    // Guarda la lista actualizada de autores en el archivo.
        } else {
            println("El autor no existe.")
        }
    }

    // Eliminar autor
    fun eliminar(autor: Autor){
        autors.remove(autor)
        guardarAutores()
    }

    // Guarda la lista actual de autores en el archivo en formato JSON.
    private fun guardarAutores() {
        // Convierte la lista de autores a una cadena JSON utilizando el ObjectMapper.
        val autoresJson = objectMapper.writeValueAsString(autors)

        // Escribe la cadena JSON en el archivo asociado a la instancia de la clase Autores.
        archivo.writeText(autoresJson)
    }

}
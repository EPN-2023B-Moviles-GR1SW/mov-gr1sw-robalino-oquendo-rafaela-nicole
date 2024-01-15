package Modelos

// Clase de datos que representa un Autor con detalles y una lista de libros asociados
data class Autor(
    val nombre: String,                  // Nombre del autor
    val pais: String,                    // Pais del autor
    val esAutorClasico: Boolean,         // El autor es considerado clásico.
    val edad: Int,                       // Edad del autor
    val listaLibros: MutableList<Libro>  // Lista mutable de libros asociados al autor
) {
    // Agregar un libro a la lista de libros del autor
    fun agregarLibro(libro: Libro) {
        listaLibros.add(libro)
    }

    // Obtener una copia inmutable de la lista de libros del autor
    fun obtenerLibros(): List<Libro> {
        return listaLibros.toList()
    }

    // Actualizar libros basándose en el titulo, reemplazando con un libro actualizado
    fun actualizarLibro(titulo: String, libroActualizado: Libro) {
        val indicesToUpdate = mutableListOf<Int>()

        // Encuentra los índices de los elementos que cumplen la condición
        for (index in listaLibros.indices) {
            val libro = listaLibros[index]
            if (libro.titulo == titulo) {
                indicesToUpdate.add(index)
            }
        }

        // Actualiza los elementos de la lista según los índices encontrados
        for (index in indicesToUpdate) {
            listaLibros[index] = libroActualizado
        }
    }

    // Función para eliminar libros basándose en el titulo
    fun eliminarLibro(titulo: String) {
        val librosAEliminar = mutableListOf<Libro>()

        // Encuentra los libros que cumplen la condición y los agrega a la lista de eliminación
        listaLibros.forEach { libro ->
            if (libro.titulo == titulo) {
                librosAEliminar.add(libro)
            }
        }

        // Elimina los libros encontrados de la lista principal
        listaLibros.removeAll(librosAEliminar)
    }
}
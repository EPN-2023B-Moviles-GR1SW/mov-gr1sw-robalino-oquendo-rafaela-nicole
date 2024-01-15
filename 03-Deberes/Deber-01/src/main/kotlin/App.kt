import Controladores.Autores
import Modelos.Libro
import Modelos.Autor
import java.io.File
import java.util.*

fun main() {
    val rutaArchivo = "src/main/kotlin/Archivos/autores.json"
    val archivoAutores = File(rutaArchivo)
    val gestorAutores = Autores(archivoAutores)
    val scanner = Scanner(System.`in`)

    while (true) {
        println("\nBienvenido")
        println("1. Ver autores")
        println("2. Agregar nuevo autor")
        println("3. Actualizar autor")
        println("4. Eliminar autor")
        println("5. Mostrar libros del autor")
        println("6. Agregar libro al autor")
        println("7. Actualizar libro del autor")
        println("8. Eliminar libro del autor")
        println("9. Salir")
        print("Por favor, ingrese el número de la opción que desea realizar: ")

        when (scanner.nextInt()) {
            1 -> {  //Mostrar autores
                val autores = gestorAutores.leerAutores()
                println("\nAutores:")
                autores.forEach {
                    println(it)
                }
                continuar()
            }

            2 -> {  //Agregar autor
                println("\nIngrese el nombre del nuevo autor:")
                val nombre = scanner.next()
                println("Ingrese el país del nuevo autor:")
                val pais = scanner.next()
                println("¿Es el nuevo autor considerado clásico? (true/false):")
                val esAutorClasico = scanner.nextBoolean()
                println("Ingrese la edad del nuevo autor:")
                val edad = scanner.nextInt()

                // Crear lista vacía de libros para el nuevo autor
                val listaLibros: MutableList<Libro> = mutableListOf()

                val nuevoAutor =
                    Autor(nombre.uppercase(), pais.uppercase(), esAutorClasico, edad, listaLibros)
                gestorAutores.crearAutor(nuevoAutor)
                println("Autor agregado con éxito.")
                continuar()
            }

            3 -> {  //Actualizar autor
                println("\nIngrese el nombre del autor que desea actualizar:")
                val nombreActualizar = scanner.next()
                val nombreUpperCase = nombreActualizar.uppercase()

                val autorExistente =
                    gestorAutores.leerAutores().find { it.nombre == nombreUpperCase }

                if (autorExistente != null) {
                    println("Ingrese el pais del autor:")
                    val nuevoPais = scanner.next()
                    println("Ingrese si el autor considerado clásico (true/false):")
                    val nuevoEsAutorClasico = scanner.nextBoolean()
                    println("Ingrese la edad del autor:")
                    val nuevaEdad = scanner.nextInt()

                    val autorActualizado = Autor(
                        autorExistente.nombre,
                        nuevoPais.uppercase(),
                        nuevoEsAutorClasico,
                        nuevaEdad,
                        autorExistente.listaLibros
                    )

                    gestorAutores.actualizarAutor(nombreUpperCase, autorActualizado)
                    println("Autor actualizado con éxito.")
                    continuar()
                } else {
                    println("El autor no existe.")
                    continuar()
                }
            }

            4 -> {  //Eliminar autor
                println("\nIngrese el nombre del autor que desea eliminar:")
                val nombreEliminar = scanner.next()
                val nombreUpperCase = nombreEliminar.uppercase()

                val autorExistente =
                    gestorAutores.leerAutores().find { it.nombre == nombreUpperCase }

                if (autorExistente != null) {
                    gestorAutores.eliminar(autorExistente)
                    println("Autor eliminado con éxito.")
                    continuar()
                } else {
                    println("El autor no existe.")
                    continuar()
                }
            }

            5 -> { // Mostrar Libros de un Autor
                println("\nIngrese el nombre del autor del que desea ver los libros:")
                val nombreAutor = scanner.next()
                val nombreUpperCase = nombreAutor.uppercase()

                val autor = gestorAutores.leerAutores()
                    .find { it.nombre == nombreUpperCase }

                if (autor != null) {
                    val librosAutor = autor.obtenerLibros()
                    println("\nLibros del autor: $nombreUpperCase:")
                    librosAutor.forEach { println(it) }
                    continuar()
                } else {
                    println("El autor no existe.")
                    continuar()
                }
            }

            6 -> { // Agregar Libro a un Autor
                println("\nIngrese el nombre del autor al que desea agregar un libro:")
                val nombreAutor = scanner.next()
                val nombreUpperCase = nombreAutor.uppercase()

                val autor = gestorAutores.leerAutores()
                    .find { it.nombre == nombreUpperCase }

                if (autor != null) {
                    println("Ingrese el titulo del libro:")
                    val titulo = scanner.next()
                    println("Ingrese el año de publicacion del libro:")
                    val anioPublicacion = scanner.nextInt()
                    println("Ingrese el precio del libro:")
                    val precio = scanner.nextDouble()
                    println("Ingrese el genero del libro:")
                    val genero = scanner.next()

                    val nuevoLibro = Libro(nombreUpperCase, titulo.uppercase(), anioPublicacion, precio, genero.uppercase())
                    autor.agregarLibro(nuevoLibro)
                    gestorAutores.actualizarAutor(nombreUpperCase, autor)
                    println("Libro agregado al autor con éxito.")
                    continuar()
                } else {
                    println("El autor no existe.")
                    continuar()
                }
            }

            7 -> { // Actualizar Libro de un Autor
                println("\nIngrese el nombre del autor al que pertenece el libro a actualizar:")
                val nombreAutor = scanner.next()
                val nombreUpperCase = nombreAutor.uppercase()

                val autor = gestorAutores.leerAutores()
                    .find { it.nombre == nombreUpperCase }

                if (autor != null) {
                    println("\nIngrese el titulo del libro que desea actualizar:")
                    val tituloLibro = scanner.next()
                    val tituloLibroUpperCase = tituloLibro.uppercase()

                    val librosAutor = autor.obtenerLibros()
                    val libro = librosAutor.find { it.titulo == tituloLibroUpperCase }
                    if (libro != null) {
                        println("Ingrese el año de publicacion del libro:")
                        val anioPublicacion = scanner.nextInt()
                        println("Ingrese el precio del libro:")
                        val precio = scanner.nextDouble()
                        println("Ingrese el genero del libro:")
                        val genero = scanner.next()

                        val libroActualizado = Libro(libro.nombreAutor_Libro, libro.titulo, anioPublicacion, precio, genero)
                        autor.actualizarLibro(tituloLibroUpperCase, libroActualizado)
                        gestorAutores.actualizarAutor(nombreUpperCase, autor)
                        println("Libro actualizado con éxito.")
                        continuar()
                    } else {
                        println("Índice de libro inválido.")
                        continuar()
                    }
                } else {
                    println("El autor no existe.")
                    continuar()
                }
            }

            8 -> { // Eliminar Libro de un Autor
                println("\nIngrese el nombre del autor al que pertenece el libro a eliminar:")
                val nombreAutor = scanner.next()
                val nombreUpperCase = nombreAutor.uppercase()

                val autor = gestorAutores.leerAutores()
                    .find { it.nombre == nombreUpperCase }

                if (autor != null) {
                    println("\nIngrese el titulo del libro que desea eliminar:")
                    val tituloLibro = scanner.next()
                    val tituloLibroUpperCase = tituloLibro.uppercase()

                    val librosAutor = autor.obtenerLibros()
                    val libro = librosAutor.find { it.titulo == tituloLibroUpperCase }
                    if (libro != null) {
                        autor.eliminarLibro(tituloLibroUpperCase)
                        gestorAutores.actualizarAutor(nombreUpperCase, autor)
                        println("Libro eliminado con éxito.")
                        continuar()
                    } else {
                        println("Índice de libro inválido.")
                        continuar()
                    }
                } else {
                    println("El autor no existe.")
                    continuar()
                }
            }

            9 -> {
                println("Adiós.")
                return
            }

            else -> {
                println("Opción inválida, ingrese un número válido.")
                continuar()
            }
        }
    }
}

fun continuar() {
    println("\nPresiona Enter para continuar...")
    readLine()
}

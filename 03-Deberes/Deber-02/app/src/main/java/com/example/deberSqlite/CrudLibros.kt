package com.example.deberSqlite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.deberSqlite.db.BaseDatos
import com.example.examen_ib.modelos.Libro

// Actividad para realizar operaciones CRUD en la tabla de libros de la base de datos
class CrudLibros : AppCompatActivity() {
    var listaLibro = arrayListOf<Libro>() // Lista de libros
    var posicionArreglo = -1 // Posición en la lista de libros
    var posicionItemSeleccionado = -1 // Posición del item seleccionado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_libros)

        // Obtener datos pasados por intent
        posicionArreglo = intent.getIntExtra("posicionArreglo", -1)
        posicionItemSeleccionado = intent.getIntExtra("posicion", -1)

        // Obtener instancias de las tablas Autor y Libro desde la clase BaseDatos
        val dbHelperAutor = BaseDatos.tablaAutor
        val dbHelperLibro = BaseDatos.tablaLibro

        // Obtener la lista de libros asociados al autor
        listaLibro = (dbHelperLibro?.obtenerTodosLibros(posicionArreglo) ?: ArrayList()) as ArrayList<Libro>

        // Si se ha seleccionado un libro para editar, llenar los campos con sus datos
        if (posicionItemSeleccionado != -1) {
            val inputnombreAutor_Libro = findViewById<EditText>(R.id.input_nombreAutor_Libro)
            val inputTitulo = findViewById<EditText>(R.id.input_titulo)
            val inputAnioPublicacion = findViewById<EditText>(R.id.input_anioPublicacion)
            val inputPrecio = findViewById<EditText>(R.id.input_precio)
            val inputGenero = findViewById<EditText>(R.id.input_genero)

            inputnombreAutor_Libro.setText(listaLibro[posicionItemSeleccionado].nombreAutor_Libro)
            inputTitulo.setText(listaLibro[posicionItemSeleccionado].titulo)
            inputAnioPublicacion.setText(listaLibro[posicionItemSeleccionado].anioPublicacion.toString())
            inputPrecio.setText(listaLibro[posicionItemSeleccionado].precio.toString())
            inputGenero.setText(listaLibro[posicionItemSeleccionado].genero)
        }

        // Configurar el botón para crear un nuevo libro
        val botonCrear = findViewById<Button>(R.id.btn_crearLibro)
        if (posicionItemSeleccionado == -1) {
            botonCrear.setOnClickListener {
                val nombreAutor_Libro = findViewById<EditText>(R.id.input_nombreAutor_Libro).text.toString()
                val titulo = findViewById<EditText>(R.id.input_titulo).text.toString()
                val anioPublicacion = findViewById<EditText>(R.id.input_anioPublicacion).text.toString().toInt()
                val precio = findViewById<EditText>(R.id.input_precio).text.toString().toDouble()
                val genero = findViewById<EditText>(R.id.input_genero).text.toString()

                // Insertar el nuevo libro en la base de datos
                try {
                    dbHelperLibro?.insertarLibro(nombreAutor_Libro, titulo, anioPublicacion, precio, genero, posicionArreglo)
                    // Devolver la respuesta
                    devolverRespuesta()
                } catch (e: Exception) {
                    // Manejar errores de inserción
                    e.printStackTrace()
                }
            }
        }

        // Configurar el botón para actualizar un libro existente
        val botonActualizar = findViewById<Button>(R.id.btn_actualizarLibro)
        if (posicionItemSeleccionado != -1) {
            botonActualizar.setOnClickListener {
                // Obtener el nuevo precio desde la vista
                val nuevoPrecio = findViewById<EditText>(R.id.input_precio).text.toString()

                // Actualizar el precio del libro en la base de datos
                try {
                    dbHelperLibro?.actualizarLibro(
                        listaLibro[posicionItemSeleccionado].nombreAutor_Libro,
                        listaLibro[posicionItemSeleccionado].titulo,
                        listaLibro[posicionItemSeleccionado].anioPublicacion,
                        nuevoPrecio.toDouble(),
                        listaLibro[posicionItemSeleccionado].genero,
                        listaLibro[posicionArreglo].idAutor,
                        listaLibro[posicionItemSeleccionado].id
                    )
                    // Devolver la respuesta
                    devolverRespuesta()
                } catch (e: Exception) {
                    // Manejar errores de actualización
                    e.printStackTrace()
                }
            }
        }
    }

    // Método para devolver la respuesta a la actividad anterior
    private fun devolverRespuesta() {
        val intentDevolverParametros = Intent()
        intentDevolverParametros.putExtra("posicion", posicionItemSeleccionado)
        setResult(
            RESULT_OK,
            intentDevolverParametros
        )
        finish()
    }

    // Método llamado al destruir la actividad
    override fun onDestroy() {
        // Cerrar la conexión a las bases de datos
        BaseDatos.tablaAutor?.close()
        BaseDatos.tablaLibro?.close()
        super.onDestroy()
    }
}

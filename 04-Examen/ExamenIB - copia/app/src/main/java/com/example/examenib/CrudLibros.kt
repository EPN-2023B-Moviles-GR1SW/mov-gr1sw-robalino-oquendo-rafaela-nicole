package com.example.examenib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.examen_ib.modelos.Libro

class CrudLibros : AppCompatActivity() {

    // Lista de autores almacenada en memoria
    val arreglo = BaseDatosMemoria.arregloAutor
    var listaLibro = arrayListOf<Libro>()
    var posicionArreglo = -1
    var posicionItemSeleccionado = -1
    var nombreAutor_Libro: String = ""
    var titulo: String = ""
    var anioPublicacion: String = ""
    var precio: String = ""
    var genero: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_libros)

        // Recupera la posición del autor seleccionado y la posición del libro dentro de la lista de libros
        posicionArreglo = intent.getIntExtra("posicionArreglo", -1)
        posicionItemSeleccionado = intent.getIntExtra("posicion", -1)

        // Obtiene la lista de libros del autor seleccionado
        listaLibro = arreglo[posicionArreglo].listaLibros

        // Si se está editando un libro existente, prellena los campos con sus datos
        if (posicionItemSeleccionado != -1) {
            val inputNombreAutor = findViewById<EditText>(R.id.input_nombreAutor_Libro)
            val inputTitulo = findViewById<EditText>(R.id.input_titulo)
            val inputAnioPublicacion = findViewById<EditText>(R.id.input_anioPublicacion)
            val inputPrecio = findViewById<EditText>(R.id.input_precio)
            val inputGenero = findViewById<EditText>(R.id.input_genero)

            inputNombreAutor.setText(listaLibro[posicionItemSeleccionado].nombreAutor_Libro)
            inputTitulo.setText(listaLibro[posicionItemSeleccionado].titulo)
            inputAnioPublicacion.setText(listaLibro[posicionItemSeleccionado].anioPublicacion.toString())
            inputPrecio.setText(listaLibro[posicionItemSeleccionado].precio.toString())
            inputGenero.setText(listaLibro[posicionItemSeleccionado].genero)
        }

        // Configuración del botón de creación
        val botonCrear = findViewById<Button>(R.id.btn_crearLibro)
        if (posicionItemSeleccionado == -1) { // Si no se está editando, asigna el listener de creación
            botonCrear.setOnClickListener {
                // Obtiene los datos de los campos de texto
                this.nombreAutor_Libro = findViewById<EditText>(R.id.input_nombreAutor_Libro).text.toString()
                this.titulo = findViewById<EditText>(R.id.input_titulo).text.toString()
                anioPublicacion = findViewById<EditText>(R.id.input_anioPublicacion).text.toString()
                precio = findViewById<EditText>(R.id.input_precio).text.toString()
                genero = findViewById<EditText>(R.id.input_genero).text.toString()

                // Agrega el nuevo libro a la lista de libros del autor seleccionado
                listaLibro.add(
                    Libro(
                        this.nombreAutor_Libro.uppercase(),
                        this.titulo.uppercase(),
                        anioPublicacion.toInt(),
                        precio.toDouble(),
                        genero.uppercase()
                    )
                )
                // Devuelve el resultado a la actividad que llamó a esta
                devolverRespuesta()
            }
        }

        // Configuración del botón de actualización
        val botonActualizar = findViewById<Button>(R.id.btn_actualizarLibro)
        if (posicionItemSeleccionado != -1) { // Si se está editando, asigna el listener de actualización
            botonActualizar.setOnClickListener {
                // Obtiene los datos de los campos de texto
                nombreAutor_Libro = findViewById<EditText>(R.id.input_nombreAutor_Libro).text.toString()
                titulo = findViewById<EditText>(R.id.input_titulo).text.toString()
                anioPublicacion = findViewById<EditText>(R.id.input_anioPublicacion).text.toString()
                precio = findViewById<EditText>(R.id.input_precio).text.toString()
                genero = findViewById<EditText>(R.id.input_genero).text.toString()

                // Actualiza los datos del libro existente en la lista de libros del autor seleccionado
                listaLibro[posicionItemSeleccionado].nombreAutor_Libro = nombreAutor_Libro
                listaLibro[posicionItemSeleccionado].titulo = titulo
                listaLibro[posicionItemSeleccionado].anioPublicacion = anioPublicacion.toInt()
                listaLibro[posicionItemSeleccionado].precio = precio.toDouble()
                listaLibro[posicionItemSeleccionado].genero = genero

                // Devuelve el resultado a la actividad que llamó a esta
                devolverRespuesta()
            }
        }
    }

    // Método que devuelve el resultado a la actividad anterior
    private fun devolverRespuesta() {
        val intentDevolverParametros = Intent()
        intentDevolverParametros.putExtra("posicion", posicionItemSeleccionado)

        // Configura el resultado y finaliza la actividad
        setResult(
            RESULT_OK,
            intentDevolverParametros
        )
        finish()
    }
}

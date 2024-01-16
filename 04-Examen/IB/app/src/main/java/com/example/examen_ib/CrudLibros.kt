package com.example.examen_ib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.examen_ib.modelos.Libro

class CrudLibros : AppCompatActivity() {

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

            posicionArreglo = intent.getIntExtra("posicionArreglo", -1)
            posicionItemSeleccionado = intent.getIntExtra("posicion", -1)

            listaLibro = arreglo[posicionArreglo].listaLibros

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

            val botonCrear = findViewById<Button>(R.id.btn_crearLibro)
            if (posicionItemSeleccionado == -1) {
                botonCrear.setOnClickListener {
                    this.nombreAutor_Libro = findViewById<EditText>(R.id.input_nombreAutor_Libro).text.toString()
                    this.titulo = findViewById<EditText>(R.id.input_titulo).text.toString()
                    anioPublicacion = findViewById<EditText>(R.id.input_anioPublicacion).text.toString()
                    precio = findViewById<EditText>(R.id.input_precio).text.toString()
                    genero = findViewById<EditText>(R.id.input_genero).text.toString()

                    listaLibro.add(
                        Libro(
                            this.nombreAutor_Libro.uppercase(),
                            this.titulo.uppercase(),
                            anioPublicacion.toInt(),
                            precio.toDouble(),
                            genero.uppercase()
                        )
                    )
                    devolverRespuesta()
                }
            }

            val botonActualizar = findViewById<Button>(R.id.btn_actualizarLibro)
            if (posicionItemSeleccionado != -1) {
                botonActualizar.setOnClickListener {
                    precio = findViewById<EditText>(R.id.input_precio).text.toString()
                    listaLibro[posicionItemSeleccionado].precio = precio.toDouble()
                    devolverRespuesta()
                }
            }
        }

        private fun devolverRespuesta() {
            val intentDevolverParametros = Intent()
            intentDevolverParametros.putExtra("posicion", posicionItemSeleccionado)

            setResult(
                RESULT_OK,
                intentDevolverParametros
            )
            finish()
        }
}
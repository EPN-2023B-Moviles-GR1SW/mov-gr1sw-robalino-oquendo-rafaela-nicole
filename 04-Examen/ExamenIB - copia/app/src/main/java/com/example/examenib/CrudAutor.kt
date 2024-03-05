package com.example.examenib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.examen_ib.modelos.Autor
import com.example.examen_ib.modelos.Libro
import java.util.ArrayList

class CrudAutor : AppCompatActivity() {

    // Lista de autores almacenada en memoria
    val arreglo = BaseDatosMemoria.arregloAutor

    // Posición del autor seleccionado para editar, inicializada en -1
    var posicionItemSeleccionado = -1
    var nombre: String = ""
    var pais: String = ""
    var edad: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_autor)

        // Recupera la posición del autor seleccionado desde la actividad anterior
        posicionItemSeleccionado = intent.getIntExtra("posicion", -1)

        // Si se está editando un autor existente, prellena los campos con sus datos
        if (posicionItemSeleccionado != -1) {
            val inputNombre = findViewById<EditText>(R.id.input_nombre)
            val inputPais = findViewById<EditText>(R.id.input_pais)
            val inputEdad = findViewById<EditText>(R.id.input_edad)

            inputNombre.setText(arreglo[posicionItemSeleccionado].nombre)
            inputPais.setText(arreglo[posicionItemSeleccionado].pais)
            inputEdad.setText(arreglo[posicionItemSeleccionado].edad.toString())
        }

        // Configuración del botón de creación
        val botonCrear = findViewById<Button>(R.id.btn_crear)
        if (posicionItemSeleccionado == -1) { // Si no se está editando, asigna el listener de creación
            botonCrear.setOnClickListener {
                // Obtiene los datos de los campos de texto
                nombre = findViewById<EditText>(R.id.input_nombre).text.toString()
                pais = findViewById<EditText>(R.id.input_pais).text.toString()
                edad = findViewById<EditText>(R.id.input_edad).text.toString()

                // Crea una lista vacía de libros para el autor nuevo
                val listaLibros: ArrayList<Libro> = arrayListOf()

                // Agrega el nuevo autor a la lista en memoria
                arreglo.add(
                    Autor(
                        nombre.uppercase(),
                        pais.uppercase(),
                        edad.toInt(),
                        listaLibros
                    )
                )
                // Devuelve el resultado a la actividad que llamó a esta
                devolverRespuesta()
            }
        }

        // Configuración del botón de actualización
        val botonActualizar = findViewById<Button>(R.id.btn_actualizar)
        if (posicionItemSeleccionado != -1) { // Si se está editando, asigna el listener de actualización
            botonActualizar.setOnClickListener {
                // Obtiene los datos de los campos de texto
                nombre = findViewById<EditText>(R.id.input_nombre).text.toString()
                pais = findViewById<EditText>(R.id.input_pais).text.toString()
                edad = findViewById<EditText>(R.id.input_edad).text.toString()

                // Actualiza los datos del autor existente en la lista en memoria
                arreglo[posicionItemSeleccionado].nombre = nombre
                arreglo[posicionItemSeleccionado].pais = pais
                arreglo[posicionItemSeleccionado].edad = edad.toInt()

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

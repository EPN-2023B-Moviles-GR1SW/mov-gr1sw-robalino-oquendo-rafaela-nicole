package com.example.deberSqlite

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.deberSqlite.db.BaseDatos

// Actividad para realizar operaciones CRUD en la tabla de autores de la base de datos
class CrudAutor : AppCompatActivity() {
    // Posición del autor seleccionado para editar, inicializada en -1
    var posicionItemSeleccionado = -1
    var nombre: String = ""
    var pais: String = ""
    var edad: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_autor)

        // Obtener la posición del autor seleccionado para editar, si hay alguna
        posicionItemSeleccionado = intent.getIntExtra("posicion", -1)

        // Obtener una instancia de la tabla de autores desde la clase BaseDatos
        val dbHelperConcesionario = BaseDatos.tablaAutor

        // Si se ha seleccionado un autor para editar, llenar los campos con sus datos
        if (posicionItemSeleccionado != -1) {
            val inputNombre = findViewById<EditText>(R.id.input_nombre)
            val inputPais = findViewById<EditText>(R.id.input_pais)
            val inputEdad = findViewById<EditText>(R.id.input_edad)

            inputNombre.setText(dbHelperConcesionario?.obtenerTodosAutores()?.get(posicionItemSeleccionado)?.nombre)
            inputPais.setText(dbHelperConcesionario?.obtenerTodosAutores()?.get(posicionItemSeleccionado)?.pais)
            inputEdad.setText(dbHelperConcesionario?.obtenerTodosAutores()?.get(posicionItemSeleccionado)?.edad.toString())
        }

        // Configurar el botón para crear un nuevo autor
        val botonCrear = findViewById<Button>(R.id.btn_crear)
        if (posicionItemSeleccionado == -1) {
            botonCrear.setOnClickListener {
                nombre = findViewById<EditText>(R.id.input_nombre).text.toString()
                pais = findViewById<EditText>(R.id.input_pais).text.toString()
                edad = findViewById<EditText>(R.id.input_edad).text.toString()

                dbHelperConcesionario?.insertarAutor(nombre, pais, edad.toInt())

                dbHelperConcesionario?.close()

                devolverRespuesta()
            }
        }

        // Configurar el botón para actualizar un autor existente
        val botonActualizar = findViewById<Button>(R.id.btn_actualizar)
        if (posicionItemSeleccionado != -1) {
            botonActualizar.setOnClickListener {
                nombre = findViewById<EditText>(R.id.input_nombre).text.toString()
                pais = findViewById<EditText>(R.id.input_pais).text.toString()
                edad = findViewById<EditText>(R.id.input_edad).text.toString()

                dbHelperConcesionario?.actualizarAutor(
                    nombre,
                    pais,
                    edad.toInt(),
                    dbHelperConcesionario.obtenerTodosAutores()?.get(posicionItemSeleccionado)?.id ?: 0
                )

                dbHelperConcesionario?.close()

                devolverRespuesta()
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
}
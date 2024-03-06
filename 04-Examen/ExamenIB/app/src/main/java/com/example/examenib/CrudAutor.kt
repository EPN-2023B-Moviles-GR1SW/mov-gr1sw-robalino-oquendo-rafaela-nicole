package com.example.examenib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

import android.content.ContentValues.TAG
import android.util.Log
import com.example.examen_ib.modelos.Autor
import com.example.examen_ib.modelos.Libro
import com.google.firebase.firestore.FirebaseFirestore

class CrudAutor : AppCompatActivity() {

    // Inicialización de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()
    private val autorCollection = db.collection("autores")
    var posicionItemSeleccionado = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_autor)

        // Obtener datos del intent
        posicionItemSeleccionado = intent.getIntExtra("posicion", -1)
        val autorNombre = intent.getStringExtra("concesionarioNombre")
        val autorPais = intent.getStringExtra("autorPais")
        val autorEdad = intent.getIntExtra("autorEdad", 0)

        // Establecer los valores en los EditText
        findViewById<EditText>(R.id.input_nombre).setText(autorNombre)
        findViewById<EditText>(R.id.input_pais).setText(autorPais)
        findViewById<EditText>(R.id.input_edad).setText(autorEdad.toString())

        // Configuración del botón de crear
        val botonCrear = findViewById<Button>(R.id.btn_crear)
        botonCrear.setOnClickListener {
            // Obtener los valores de los EditText
            val name = findViewById<EditText>(R.id.input_nombre).text.toString()
            val pais = findViewById<EditText>(R.id.input_pais).text.toString()
            val edad = findViewById<EditText>(R.id.input_edad).text.toString()
            val listaLibros = arrayListOf<Libro>()

            // Crear un objeto Autor
            val autor = Autor(name, pais, edad.toInt(), listaLibros)

            // Verificar si se está creando un nuevo autor o actualizando uno existente
            if (posicionItemSeleccionado == -1) {
                // Agregar un nuevo autor a Firestore
                autorCollection.add(autor)
                    .addOnSuccessListener {
                        devolverRespuesta() // Devolver respuesta exitosa
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al agregar un nuevo autor", e)
                    }
            } else {
                // Actualizar un autor existente en Firestore
                val nameF = intent.getStringExtra("nameF")
                autorCollection.document(nameF!!)
                    .set(autor)
                    .addOnSuccessListener {
                        devolverRespuesta() // Devolver respuesta exitosa
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al actualizar el distribuidor", e)
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

        finish() // Finalizar la actividad actual y regresar a la anterior
    }
}

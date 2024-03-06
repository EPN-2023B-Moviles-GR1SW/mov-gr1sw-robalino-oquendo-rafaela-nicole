package com.example.examenib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.examen_ib.modelos.Libro

import android.content.ContentValues.TAG
import android.util.Log
import com.example.examen_ib.modelos.Autor

import com.google.firebase.firestore.FirebaseFirestore

class CrudLibros : AppCompatActivity() {

    // Inicialización de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()
    private val autorCollection = db.collection("autores")
    private var nameF = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_libros)

        // Obtener datos del intent
        val selectedIndexItem = intent.getIntExtra("position", -1)
        val editar = intent.getIntExtra("editar", -1)
        val libNombreAutor = intent.getStringExtra("autNombre")
        val libTitulo = intent.getStringExtra("autTitulo")
        val libYear = intent.getStringExtra("autAnio")
        val libPrecio = intent.getStringExtra("autPrecio")
        val libGenero = intent.getStringExtra("autGenero")
        nameF = intent.getStringExtra("nameF").toString()

        // Comprobar si se está editando un libro existente
        if (editar == 0) {
            // Establecer los valores en los EditText
            findViewById<EditText>(R.id.input_nombreAutor_Libro).setText(libNombreAutor)
            findViewById<EditText>(R.id.input_titulo).setText(libTitulo)
            findViewById<EditText>(R.id.input_anioPublicacion).setText(libYear)
            findViewById<EditText>(R.id.input_precio).setText(libPrecio)
            findViewById<EditText>(R.id.input_genero).setText(libGenero)
        }

        // Configuración del botón de crear libro
        val botonCrear = findViewById<Button>(R.id.btn_crearLibro)
        botonCrear.setOnClickListener {
            // Obtener valores de los EditText
            val nombreAutor_Libro = findViewById<EditText>(R.id.input_nombreAutor_Libro).text.toString()
            val titulo = findViewById<EditText>(R.id.input_titulo).text.toString()
            val anioPublicacion = findViewById<EditText>(R.id.input_anioPublicacion).text.toString().toInt()
            val precio = findViewById<EditText>(R.id.input_precio).text.toString().toDouble()
            val genero = findViewById<EditText>(R.id.input_genero).text.toString()
            val libList = arrayListOf<Libro>()

            // Crear un objeto Libro
            val libro = Libro(nombreAutor_Libro, titulo, anioPublicacion, precio, genero)

            libList.add(libro)

            // Verificar si se está creando un nuevo libro o actualizando uno existente
            if (selectedIndexItem == -1) {
                // Agregar un nuevo libro a la lista de libros del autor en Firestore
                autorCollection.document(nameF)
                    .update("listaLibros", libList)
                    .addOnSuccessListener {
                        devolverRespuesta(-1) // Devolver respuesta exitosa
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al agregar un nuevo libro", e)
                    }
            } else {
                // Obtener el autor de Firestore y actualizar la lista de libros
                autorCollection.document(nameF)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        val autor = documentSnapshot.toObject(Autor::class.java)
                        autor?.let { autor ->
                            val updateLibList = autor.listaLibros?.toMutableList()
                            val libIndex = updateLibList?.indexOfFirst { it.nombreAutor_Libro == libro.nombreAutor_Libro }
                            if (libIndex != null && libIndex != -1) {
                                updateLibList[libIndex] = libro
                                autorCollection.document(nameF)
                                    .set(autor.copy(listaLibros = updateLibList))
                                    .addOnSuccessListener {
                                        devolverRespuesta(selectedIndexItem) // Devolver respuesta exitosa
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(TAG, "Error al actualizar el libro", e)
                                    }
                            } else {
                                Log.e(TAG, "Libro no encontrado en la lista de autor")
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al obtener el autor", e)
                    }
            }
        }

    }

    // Método para devolver la respuesta a la actividad anterior
    private fun devolverRespuesta(position: Int) {
        val intentDevolverParametros = Intent()
        intentDevolverParametros.putExtra("position", position)

        setResult(
            AppCompatActivity.RESULT_OK,
            intentDevolverParametros
        )

        finish() // Finalizar la actividad actual y regresar a la anterior
    }
}

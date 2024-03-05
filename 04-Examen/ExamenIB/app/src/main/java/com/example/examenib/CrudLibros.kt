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

    private val db = FirebaseFirestore.getInstance()
    private val autorCollection = db.collection("autores")
    private var nameF = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_libros)

        val selectedIndexItem = intent.getIntExtra("position", -1)
        val editar = intent.getIntExtra("editar", -1)
        val libNombreAutor = intent.getStringExtra("carMarca")
        val libTitulo = intent.getStringExtra("carModelo")
        val libYear = intent.getStringExtra("carYear")
        val libPrecio = intent.getStringExtra("carPrecio")
        val libGenero = intent.getStringExtra("carEstado")
        nameF = intent.getStringExtra("nameF").toString()

        if (editar == 0) {
            findViewById<EditText>(R.id.input_nombreAutor_Libro).setText(libNombreAutor)
            findViewById<EditText>(R.id.input_titulo).setText(libTitulo)
            findViewById<EditText>(R.id.input_anioPublicacion).setText(libYear)
            findViewById<EditText>(R.id.input_precio).setText(libPrecio)
            findViewById<EditText>(R.id.input_genero).setText(libGenero)
        }

        val botonCrear = findViewById<Button>(R.id.btn_crearLibro)
        botonCrear.setOnClickListener {
            val nombreAutor_Libro = findViewById<EditText>(R.id.input_nombreAutor_Libro).text.toString()
            val titulo = findViewById<EditText>(R.id.input_titulo).text.toString()
            val anioPublicacion = findViewById<EditText>(R.id.input_anioPublicacion).text.toString().toInt()
            val precio = findViewById<EditText>(R.id.input_precio).text.toString().toDouble()
            val genero = findViewById<EditText>(R.id.input_genero).text.toString()
            val libList = arrayListOf<Libro>()

            val car = Libro(nombreAutor_Libro, titulo, anioPublicacion, precio, genero)

            libList.add(car)

            if (selectedIndexItem == -1) {
                autorCollection.document(nameF)
                    .update("listaLibros", libList)
                    .addOnSuccessListener {
                        devolverRespuesta(-1)
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al agregar un nuevo libro", e)
                    }
            } else {
                autorCollection.document(nameF)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        val autor = documentSnapshot.toObject(Autor::class.java)
                        autor?.let { autor ->
                            val updateLibList = autor.listaLibros?.toMutableList()
                            val libIndex = updateLibList?.indexOfFirst { it.nombreAutor_Libro == car.nombreAutor_Libro }
                            if (libIndex != null && libIndex != -1) {
                                updateLibList[libIndex] = car
                                autorCollection.document(nameF)
                                    .set(autor.copy(listaLibros = updateLibList))
                                    .addOnSuccessListener {
                                        devolverRespuesta(selectedIndexItem)
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

    private fun devolverRespuesta(position: Int) {
        val intentDevolverParametros = Intent()
        intentDevolverParametros.putExtra("position", position)

        setResult(
            AppCompatActivity.RESULT_OK,
            intentDevolverParametros
        )

        finish()
    }
}

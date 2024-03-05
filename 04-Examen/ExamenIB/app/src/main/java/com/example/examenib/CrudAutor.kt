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

    private val db = FirebaseFirestore.getInstance()
    private val autorCollection = db.collection("autores")
    var posicionItemSeleccionado = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_autor)

        posicionItemSeleccionado = intent.getIntExtra("posicion", -1)
        val autorNombre = intent.getStringExtra("concesionarioNombre")
        val autorPais = intent.getStringExtra("autorPais")
        val autorEdad = intent.getIntExtra("autorEdad", 0)

        findViewById<EditText>(R.id.input_nombre).setText(autorNombre)
        findViewById<EditText>(R.id.input_pais).setText(autorPais)
        findViewById<EditText>(R.id.input_edad).setText(autorEdad.toString())

        val botonCrear = findViewById<Button>(R.id.btn_crear)
        botonCrear.setOnClickListener {
            val name = findViewById<EditText>(R.id.input_nombre).text.toString()
            val pais = findViewById<EditText>(R.id.input_pais).text.toString()
            val edad = findViewById<EditText>(R.id.input_edad).text.toString()
            val listaLibros = arrayListOf<Libro>()

            val autor =
                Autor(name, pais, edad.toInt(), listaLibros)

            if (posicionItemSeleccionado == -1) {
                autorCollection.add(autor)
                    .addOnSuccessListener {
                        devolverRespuesta()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al agregar un nuevo autor", e)
                    }
            } else {
                val nameF = intent.getStringExtra("nameF")
                autorCollection.document(nameF!!)
                    .set(autor)
                    .addOnSuccessListener {
                        devolverRespuesta()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error al actualizar el distribuidor", e)
                    }
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

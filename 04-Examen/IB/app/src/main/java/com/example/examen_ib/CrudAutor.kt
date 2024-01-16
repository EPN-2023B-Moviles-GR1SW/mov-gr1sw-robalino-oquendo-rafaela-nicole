package com.example.examen_ib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.examen_ib.modelos.Autor
import com.example.examen_ib.modelos.Libro
import java.util.ArrayList

class CrudAutor : AppCompatActivity() {

    val arreglo = BaseDatosMemoria.arregloAutor
    var posicionItemSeleccionado = -1
    var nombre: String = ""
    var pais: String = ""
    var edad: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_autor)

        posicionItemSeleccionado = intent.getIntExtra("posicion", -1)

        if (posicionItemSeleccionado != -1) {
            val inputNombre = findViewById<EditText>(R.id.input_nombre)
            val inputPais = findViewById<EditText>(R.id.input_pais)
            val inputEdad = findViewById<EditText>(R.id.input_edad)

            inputNombre.setText(arreglo[posicionItemSeleccionado].nombre)
            inputPais.setText(arreglo[posicionItemSeleccionado].pais)
            inputEdad.setText(arreglo[posicionItemSeleccionado].edad.toString())
        }

        val botonCrear = findViewById<Button>(R.id.btn_crear)
        if (posicionItemSeleccionado == -1) {
            botonCrear.setOnClickListener {
                nombre = findViewById<EditText>(R.id.input_nombre).text.toString()
                pais = findViewById<EditText>(R.id.input_pais).text.toString()
                edad = findViewById<EditText>(R.id.input_edad).text.toString()

                val listaLibros: ArrayList<Libro> = arrayListOf()

                arreglo.add(
                    Autor(
                        nombre.uppercase(),
                        pais.uppercase(),
                        edad.toInt(),
                        listaLibros
                    )
                )
                devolverRespuesta()
            }
        }


        val botonActualizar = findViewById<Button>(R.id.btn_actualizar)
        if (posicionItemSeleccionado != -1) {
            botonActualizar.setOnClickListener {
                nombre = findViewById<EditText>(R.id.input_nombre).text.toString()
                pais = findViewById<EditText>(R.id.input_pais).text.toString()
                edad = findViewById<EditText>(R.id.input_edad).text.toString()

                arreglo[posicionItemSeleccionado].nombre = nombre
                arreglo[posicionItemSeleccionado].pais = pais
                arreglo[posicionItemSeleccionado].edad = edad.toInt()

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
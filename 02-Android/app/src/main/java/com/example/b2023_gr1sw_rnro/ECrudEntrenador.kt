package com.example.b2023_gr1sw_rnro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class ECrudEntrenador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecrud_entrenador)
        // Logica Buscar Entrenador
        val botonBuscarBB = findViewById<Button>(R.id.btn_buscar_bdd)
        botonBuscarBB
            .setOnClickListener {
                //Obtener componentes visuales
                val id = findViewById<EditText>(R.id.input_id)
                val nombre = findViewById<EditText>(R.id.input_nombre)
                val descripcion = findViewById<EditText>(R.id.input_descripcion)
                //Busqueda en la BDD Sqlite
                val entrenador= EBaseDeDatos.tablaEntrenador!!
                    .consultarEntrenadorPorID(
                        id.text.toString().toInt()
                    )
                // Setear los valores en los comp visuales
                if(entrenador.id == 0){
                    mostrarSnackBar("Usu. no encontrado")
                }
                id.setText(entrenador.id.toString())
                nombre.setText(entrenador.nombre)
                descripcion.setText(entrenador.descripcion)
                mostrarSnackBar("Usu. Encontrado")
            }

        val botonCrearBDD = findViewById<Button>(R.id.btn_crear_bdd)
        botonCrearBDD
            .setOnClickListener {
                val nombre = findViewById<EditText>(R.id.input_nombre)
                val descripcion = findViewById<EditText>(R.id.input_descripcion)
                val respuesta = EBaseDeDatos
                    .tablaEntrenador!!.crearEntrenador(
                        nombre.text.toString(),
                        descripcion.text.toString()
                    )
                if(respuesta) mostrarSnackBar("Ent. Creado")
            }

        val botonActualizarBB = findViewById<Button>(R.id.btn_actualizar_bdd)
        botonActualizarBB
            .setOnClickListener {
                val id = findViewById<EditText>(R.id.input_id)
                val nombre = findViewById<EditText>(R.id.input_nombre)
                val descripcion = findViewById<EditText>(R.id.input_descripcion)
                val respuesta = EBaseDeDatos.tablaEntrenador!!.actualizarEntrenadorFormulario(
                        nombre.text.toString(),
                        descripcion.text.toString(),
                        id.text.toString().toInt()
                    )
                if(respuesta) mostrarSnackBar("Usu. Actualizado")
            }

        val botonEliminarBDD = findViewById<Button>(R.id.btn_eliminar_bdd)
        botonEliminarBDD
            .setOnClickListener {
                val id = findViewById<EditText>(R.id.input_id)
                val respuesta = EBaseDeDatos.tablaEntrenador!!.eliminarEntrenadorFormulario(
                    id.text.toString().toInt()
                )
                if(respuesta) mostrarSnackBar("Usu. Eliminado")
            }
    }   // Fin OnCreate

    fun mostrarSnackBar(texto:String){
        Snackbar
            .make(
                findViewById(R.id.id_layout_main),   //view
                texto,    //texto
                Snackbar.LENGTH_INDEFINITE  //tiempo
            )
            .show()
    }
}
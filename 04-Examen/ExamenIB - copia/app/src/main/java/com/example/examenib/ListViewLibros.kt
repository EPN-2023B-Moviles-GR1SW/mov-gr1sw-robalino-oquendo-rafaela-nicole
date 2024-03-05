package com.example.examenib

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.examen_ib.modelos.Libro
import com.google.android.material.snackbar.Snackbar

class ListViewLibros : AppCompatActivity() {

    // Lista de autores almacenada en memoria
    val arreglo = BaseDatosMemoria.arregloAutor
    var posicionArreglo = 0
    var posicionItemSeleccionado = 0
    var listaLibro = arrayListOf<Libro>()
    lateinit var adaptador: ArrayAdapter<Libro>

    // Manejo del resultado de la actividad de CRUD de libros mediante ActivityResultContracts
    val callbackContenido =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode === Activity.RESULT_OK) {
                if (result.data != null) {
                    adaptador.notifyDataSetChanged() // Actualiza la vista de la lista
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view_libros)

        // Obtiene la posición del autor seleccionado desde la actividad anterior
        posicionArreglo = intent.getIntExtra("posicion", -1)

        // Muestra el nombre del autor en un TextView
        val txtAutor = findViewById<TextView>(R.id.txt_autor)
        txtAutor.text = "AUTOR: ${arreglo[posicionArreglo].nombre}"

        // Obtiene la lista de libros del autor seleccionado
        listaLibro = arreglo[posicionArreglo].listaLibros

        // Configuración del ListView y su adaptador
        val listView = findViewById<ListView>(R.id.lv_list_libros)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaLibro
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged() // Asegura que la vista de la lista se actualice correctamente

        // Configuración del botón para añadir un nuevo libro
        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_libro)
        botonAnadirListView.setOnClickListener {
            posicionItemSeleccionado = -1 // Indica que no se está editando un libro existente
            abrirActividadConParametros(CrudLibros::class.java)
        }

        // Registro del ListView para el menú contextual (ContextMenu)
        registerForContextMenu(listView)
    }

    // Método llamado al crear el menú contextual
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menulibros, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    // Método llamado al seleccionar una opción del menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_l -> {
                abrirActividadConParametros(CrudLibros::class.java)
                return true
            }
            R.id.mi_eliminar_l -> {
                // Elimina el libro seleccionado y actualiza la vista de la lista
                mostrarSnackbar("Libro eliminado")
                listaLibro.removeAt(posicionItemSeleccionado)
                adaptador.notifyDataSetChanged()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Muestra un Snackbar con el texto proporcionado
    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(findViewById(R.id.lv_list_libros),
            texto, Snackbar.LENGTH_LONG)
        snack.show()
    }

    // Abre la actividad de CRUD de libros con los parámetros necesarios
    private fun abrirActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("posicion", posicionItemSeleccionado)
        intentExplicito.putExtra("posicionArreglo", posicionArreglo)

        // Lanza la actividad y espera el resultado utilizando el callbackContenido
        callbackContenido.launch(intentExplicito)
    }
}

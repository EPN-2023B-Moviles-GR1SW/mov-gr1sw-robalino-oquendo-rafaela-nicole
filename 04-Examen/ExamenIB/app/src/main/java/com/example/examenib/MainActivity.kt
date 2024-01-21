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
import androidx.activity.result.contract.ActivityResultContracts
import com.example.examen_ib.modelos.Autor
import com.example.examen_ib.modelos.Libro
import com.google.android.material.snackbar.Snackbar
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    // Lista de autores almacenada en memoria
    val arreglo = BaseDatosMemoria.arregloAutor
    var posicionItemSeleccionado = 0
    lateinit var adaptador: ArrayAdapter<Autor>

    // Manejo del resultado de la actividad de CRUD de autores mediante ActivityResultContracts
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
        setContentView(R.layout.activity_main)

        // Configuración del ListView y su adaptador
        val listView = findViewById<ListView>(R.id.lv_listviewC)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged() // Asegura que la vista de la lista se actualice correctamente

        // Configuración del botón para añadir un nuevo autor
        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_concesionario)
        botonAnadirListView.setOnClickListener {
            posicionItemSeleccionado = -1 // Indica que no se está editando un autor existente
            abrirActividadConParametros(CrudAutor::class.java)
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
        inflater.inflate(R.menu.menuautor, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    // Método llamado al seleccionar una opción del menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_l -> {
                abrirActividadConParametros(CrudAutor::class.java)
                return true
            }

            R.id.mi_eliminar_l -> {
                // Elimina el autor seleccionado y actualiza la vista de la lista
                mostrarSnackbar("Autor ${arreglo[posicionItemSeleccionado].nombre} eliminado")
                arreglo.removeAt(posicionItemSeleccionado)
                adaptador.notifyDataSetChanged()
                return true
            }

            R.id.mi_libros -> {
                // Abre la actividad de listado de libros asociados al autor seleccionado
                abrirActividadConParametros(ListViewLibros::class.java)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Muestra un Snackbar con el texto proporcionado
    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.lv_listviewC),
            texto, Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    // Abre la actividad de CRUD de autores con los parámetros necesarios
    private fun abrirActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("posicion", posicionItemSeleccionado)

        // Lanza la actividad y espera el resultado utilizando el callbackContenido
        callbackContenido.launch(intentExplicito)
    }
}

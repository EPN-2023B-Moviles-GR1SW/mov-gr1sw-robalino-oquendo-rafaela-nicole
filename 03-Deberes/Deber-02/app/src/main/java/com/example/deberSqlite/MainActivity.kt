package com.example.deberSqlite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.deberSqlite.db.BaseDatos
import com.example.examen_ib.modelos.Autor
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var adaptador: ArrayAdapter<Autor>
    private val dbHelperAutor = BaseDatos.tablaAutor

    val callbackContenido =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                cargarDatos() // Recargar datos al regresar de CrudAutor
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.lv_listviewA)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            mutableListOf()
        )
        listView.adapter = adaptador

        cargarDatos() // Cargar datos al iniciar la actividad

        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_autor)
        botonAnadirListView.setOnClickListener {
            abrirActividadConParametros(CrudAutor::class.java)
        }

        registerForContextMenu(listView)
    }

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
        adaptador.notifyDataSetChanged()
        mostrarSnackbar("Posición seleccionada: $posicion")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position

        when (item.itemId) {
            R.id.mi_editar_l -> {
                abrirActividadConParametros(CrudAutor::class.java, posicion)
                return true
            }

            R.id.mi_eliminar_l -> {
                val autor = adaptador.getItem(posicion)
                if (autor != null) {
                    dbHelperAutor?.eliminarAutor(autor.id)
                    cargarDatos() // Recargar datos después de eliminar un autor
                    mostrarSnackbar("Autor eliminado")
                } else {
                    Log.e("MainActivity", "Error al obtener el autor seleccionado")
                }
                return true
            }

            R.id.mi_libros -> {
                abrirActividadConParametros(ListViewLibros::class.java)
                return true
            }

            else -> return super.onContextItemSelected(item)
        }
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.lv_listviewA),
            texto, Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    private fun cargarDatos() {
        adaptador.clear()
        val autores = dbHelperAutor?.obtenerTodosAutores()
        if (autores != null && autores.isNotEmpty()) {
            adaptador.addAll(autores)
            adaptador.notifyDataSetChanged()
        } else {
            Log.e("MainActivity", "No se encontraron autores en la base de datos")
        }
    }

    private fun abrirActividadConParametros(clase: Class<*>, posicion: Int? = null) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("posicion", posicion ?: -1)
        callbackContenido.launch(intentExplicito)
    }
}
t
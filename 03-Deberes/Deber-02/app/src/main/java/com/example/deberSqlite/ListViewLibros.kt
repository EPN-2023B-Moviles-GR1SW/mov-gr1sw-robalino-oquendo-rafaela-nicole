package com.example.deberSqlite

import android.app.Activity
import android.content.Intent
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
import androidx.appcompat.app.AppCompatActivity
import com.example.deberSqlite.db.BaseDatos
import com.example.deberSqlite.db.SqliteHelperLibro
import com.example.examen_ib.modelos.Libro
import com.google.android.material.snackbar.Snackbar

// Actividad para mostrar y gestionar la lista de libros asociados a un autor
class ListViewLibros : AppCompatActivity() {
    lateinit var adaptador: ArrayAdapter<Libro>
    private val dbHelperLibro = SqliteHelperLibro(this)
    var posicionArreglo = 0
    var posicionItemSeleccionado = 0
    var listaLibro = mutableListOf<Libro>()

    // Callback para obtener resultados de la actividad
    val callbackContenido =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode === Activity.RESULT_OK) {
                if (result.data != null) {
                    // Actualiza el adaptador cuando se regresa de CrudLibros
                    adaptador.notifyDataSetChanged()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view_libros)

        // Obtiene la posición del autor seleccionado de la actividad anterior
        posicionArreglo = intent.getIntExtra("posicion", -1)

        // Muestra el nombre del autor en un TextView
        val txtAutor = findViewById<TextView>(R.id.txt_autor)
        txtAutor.text = "Concesionario: ${BaseDatos.tablaAutor?.obtenerTodosAutores()?.get(posicionArreglo)?.nombre}"

        // Obtiene la lista de libros asociados al autor
        listaLibro = dbHelperLibro.obtenerTodosLibros(BaseDatos.tablaAutor?.obtenerTodosAutores()?.get(posicionArreglo)?.id ?: -1)
            .toMutableList()
        val listView = findViewById<ListView>(R.id.lv_list_libros)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaLibro
        )
        listView.adapter = adaptador

        // Configura el botón para añadir un nuevo libro
        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_libro)
        botonAnadirListView.setOnClickListener {
            abrirActividadConParametros(CrudLibros::class.java)
        }

        // Registra el ListView para el menú contextual
        registerForContextMenu(listView)
    }

    // Crea el menú contextual para editar o eliminar un libro
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
        adaptador.notifyDataSetChanged()
    }

    // Maneja las opciones del menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_l -> {
                abrirActividadConParametros(CrudLibros::class.java)
                return true
            }
            R.id.mi_eliminar_l -> {
                // Elimina el libro seleccionado de la base de datos
                dbHelperLibro.eliminarLibro(
                    dbHelperLibro.obtenerTodosLibros(
                        BaseDatos.tablaAutor?.obtenerTodosAutores()?.get(posicionArreglo)?.id ?: -1
                    )[posicionItemSeleccionado].id
                )
                // Muestra un mensaje de confirmación
                mostrarSnackbar("Libro eliminado")
                // Actualiza la lista de libros
                adaptador.notifyDataSetChanged()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Muestra un Snackbar con el mensaje proporcionado
    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(findViewById(R.id.lv_list_libros),
            texto, Snackbar.LENGTH_LONG)
        snack.show()
    }

    // Abre la actividad especificada con los parámetros necesarios
    private fun abrirActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("posicion", posicionItemSeleccionado)
        intentExplicito.putExtra("posicionArreglo", posicionArreglo)

        callbackContenido.launch(intentExplicito)
    }
}

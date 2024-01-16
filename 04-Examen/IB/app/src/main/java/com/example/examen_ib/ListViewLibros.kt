package com.example.examen_ib

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
    val arreglo = BaseDatosMemoria.arregloAutor
    var posicionArreglo = 0
    var posicionItemSeleccionado = 0
    var listaLibro = arrayListOf<Libro>()
    lateinit var adaptador: ArrayAdapter<Libro>

    val callbackContenido =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode === Activity.RESULT_OK) {
                if (result.data != null) {
                    // logica negocio
                    val data = result.data
                    adaptador.notifyDataSetChanged()
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view_libros)

        posicionArreglo = intent.getIntExtra("posicion", -1)

        val txtAutor = findViewById<TextView>(R.id.txt_autor)
        txtAutor.text = "Autor: ${arreglo[posicionArreglo].nombre}"

        listaLibro = arreglo[posicionArreglo].listaLibros
        val listView = findViewById<ListView>(R.id.lv_list_libros)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            listaLibro
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_libro)
        botonAnadirListView.setOnClickListener {
//            anadirLibro(adaptador)
            posicionItemSeleccionado = -1
            abrirActividadConParametros(CrudLibros::class.java)
        }
        registerForContextMenu(listView)
    }

    private fun anadirLibro(adaptador: ArrayAdapter<Libro>) {
        listaLibro.add(
            Libro(
                "AUTOR",
                "TITULO",
                2023,
                20.0,
                "FICCION"
            )
        )
        adaptador.notifyDataSetChanged()
    }

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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_l -> {
                //mostrarSnackbar("${posicionItemSeleccionado}")
//                listaCarro[posicionItemSeleccionado].precio = 29999.99
//                adaptador.notifyDataSetChanged()
                abrirActividadConParametros(CrudLibros::class.java)
                return true
            }
            R.id.mi_eliminar_l -> {
                mostrarSnackbar("Libro eliminado")
                listaLibro.removeAt(posicionItemSeleccionado)
                adaptador.notifyDataSetChanged()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(findViewById(R.id.lv_list_libros),
            texto, Snackbar.LENGTH_LONG)
        snack.show()
    }

    private fun abrirActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("posicion", posicionItemSeleccionado)
        intentExplicito.putExtra("posicionArreglo", posicionArreglo)

        callbackContenido.launch(intentExplicito)
    }
}
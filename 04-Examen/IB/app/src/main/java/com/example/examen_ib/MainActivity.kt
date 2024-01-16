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
import androidx.activity.result.contract.ActivityResultContracts
import com.example.examen_ib.modelos.Autor
import com.example.examen_ib.modelos.Libro
import com.google.android.material.snackbar.Snackbar
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    val arreglo = BaseDatosMemoria.arregloAutor
    var posicionItemSeleccionado = 0
    lateinit var adaptador: ArrayAdapter<Autor>

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
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.lv_listviewC)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_concesionario)
        botonAnadirListView.setOnClickListener {
            //anadirAutor(adaptador)
            posicionItemSeleccionado = -1
            abrirActividadConParametros(CrudAutor::class.java)
//            adaptador.notifyDataSetChanged()
        }
        registerForContextMenu(listView)
    }

    private fun anadirAutor(adaptador: ArrayAdapter<Autor>) {
        val listaLibros: ArrayList<Libro> = arrayListOf()
        arreglo.add(
            Autor(
                "SUZANNE_COLLINS",
                "EEUU",
                61,
                listaLibros
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
        inflater.inflate(R.menu.menuautor, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_l -> {
                abrirActividadConParametros(CrudAutor::class.java)
                return true
            }

            R.id.mi_eliminar_l -> {
                mostrarSnackbar("Autor ${arreglo[posicionItemSeleccionado].nombre} eliminado")
                // Eliminar completamente
                arreglo.removeAt(posicionItemSeleccionado)
                adaptador.notifyDataSetChanged()
                return true
            }

            R.id.mi_libros -> {
                abrirActividadConParametros(ListViewLibros::class.java)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.lv_listviewC),
            texto, Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    private fun abrirActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("posicion", posicionItemSeleccionado)

        callbackContenido.launch(intentExplicito)
    }
}
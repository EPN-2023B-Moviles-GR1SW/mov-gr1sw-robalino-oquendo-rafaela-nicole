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
import com.google.android.material.snackbar.Snackbar

import android.content.ContentValues.TAG
import android.util.Log
import com.example.examen_ib.modelos.Autor
import com.example.examen_ib.modelos.Libro
import com.google.firebase.firestore.FirebaseFirestore

class ListViewLibros : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val autorCollection = db.collection("autores")

    private var nameF = ""
    private var indexSelectedItem = 0
    private var autorPosition = -1
    private var librosList = arrayListOf<Libro>()
    private lateinit var adaptador: ArrayAdapter<Libro>

    val callbackContenido =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode === Activity.RESULT_OK) {
                if (result.data != null) {
                    // logica negocio
                    updateLibList()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view_libros)

        val autorName = intent.getStringExtra("AutorName").toString()
        nameF = intent.getStringExtra("nameF").toString()
        autorPosition = intent.getIntExtra("AutorPosition", -1)

        val txtAutor = findViewById<TextView>(R.id.txt_autor)
        if (autorName != null) {
            txtAutor.text = "Concesionario: ${autorName}"
        }

        val listView = findViewById<ListView>(R.id.lv_list_libros)
        registerForContextMenu(listView)

        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_libro)
        botonAnadirListView.setOnClickListener {
            indexSelectedItem = -1
            abrirActividadConParametros(CrudLibros::class.java)
        }

        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            librosList
        )

        listView.adapter = adaptador
        updateLibList()
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
        indexSelectedItem = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_l -> {
                abrirActividadConParametros(CrudLibros::class.java)
                return true
            }
            R.id.mi_eliminar_l -> {
                val deletedLib = librosList.removeAt(indexSelectedItem)
                adaptador.notifyDataSetChanged()
                deleteLibFromFirestore(deletedLib)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun deleteLibFromFirestore(lib: Libro){
        autorCollection.document(nameF)
            .update("listaLibros", librosList)
            .addOnSuccessListener {
                mostrarSnackbar("Libro eliminado con exito")
                adaptador.clear()
                adaptador.addAll(librosList)
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                mostrarSnackbar("Error al eliminar el libro")
                librosList.add(indexSelectedItem, lib)
                adaptador.notifyDataSetChanged()
            }
    }

    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(findViewById(R.id.lv_list_libros),
            texto, Snackbar.LENGTH_LONG)
        snack.show()
    }

    private fun abrirActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("position", indexSelectedItem)
        intentExplicito.putExtra("autorPosition", autorPosition)
        intentExplicito.putExtra("nameF", nameF)
        if (indexSelectedItem != -1){
            val selectedLib = librosList[indexSelectedItem]
            intentExplicito.putExtra("libNombreAutor", selectedLib.nombreAutor_Libro)
            intentExplicito.putExtra("libTitulo", selectedLib.titulo)
            intentExplicito.putExtra("libAnio", selectedLib.anioPublicacion)
            intentExplicito.putExtra("libPrecio", selectedLib.precio)
            intentExplicito.putExtra("libGenero", selectedLib.genero)
            intentExplicito.putExtra("editar", 0)
        }

        callbackContenido.launch(intentExplicito)
    }

    private fun updateLibList() {
        autorCollection.document(nameF).get()
            .addOnSuccessListener { documentSnapshot ->
                val autor = documentSnapshot.toObject(Autor::class.java)
                if (autor != null) {
                    librosList = (autor.listaLibros as ArrayList<Libro>?)!!
                    adaptador.clear()
                    adaptador.addAll(librosList)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al obtener lista libros", e)
            }
    }
}

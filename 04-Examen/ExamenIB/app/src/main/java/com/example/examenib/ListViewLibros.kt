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

    // Inicialización de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()
    private val autorCollection = db.collection("autores")

    // Variables para manejar los datos de la actividad
    private var nameF = ""
    private var indexSelectedItem = 0
    private var autorPosition = -1
    private var librosList = arrayListOf<Libro>()
    private lateinit var adaptador: ArrayAdapter<Libro>

    // Callback para obtener resultados de actividades
    val callbackContenido =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode === Activity.RESULT_OK) {
                if (result.data != null) {
                    // Lógica de negocio para manejar el resultado de la actividad
                    updateLibList()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view_libros)

        // Obtener datos del intent
        val autorName = intent.getStringExtra("AutorName").toString()
        nameF = intent.getStringExtra("nameF").toString()
        autorPosition = intent.getIntExtra("AutorPosition", -1)

        // Mostrar el nombre del autor en el TextView correspondiente
        val txtAutor = findViewById<TextView>(R.id.txt_autor)
        if (autorName != null) {
            txtAutor.text = "Autor: ${autorName}"
        }

        // Configuración del ListView y su adaptador
        val listView = findViewById<ListView>(R.id.lv_list_libros)
        registerForContextMenu(listView)

        // Configuración del botón para añadir un libro
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
        updateLibList() // Actualizar la lista de libros al iniciar la actividad
    }

    // Método para crear el menú contextual al mantener presionado un elemento de la lista
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

    // Método para manejar las opciones del menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_l -> {
                abrirActividadConParametros(CrudLibros::class.java)
                return true
            }
            R.id.mi_eliminar_l -> {
                val deletedLib = librosList.removeAt(indexSelectedItem) // Eliminar el libro de la lista
                adaptador.notifyDataSetChanged() // Notificar al adaptador sobre el cambio
                deleteLibFromFirestore(deletedLib) // Eliminar el libro de Firestore
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    // Método para eliminar un libro de Firestore
    private fun deleteLibFromFirestore(lib: Libro){
        autorCollection.document(nameF)
            .update("listaLibros", librosList)
            .addOnSuccessListener {
                mostrarSnackbar("Libro eliminado con exito") // Mostrar mensaje de éxito
                adaptador.clear() // Limpiar el adaptador
                adaptador.addAll(librosList) // Añadir la lista actualizada de libros al adaptador
                adaptador.notifyDataSetChanged() // Notificar al adaptador sobre los cambios
            }
            .addOnFailureListener { e ->
                mostrarSnackbar("Error al eliminar el libro") // Mostrar mensaje de error
                librosList.add(indexSelectedItem, lib) // Añadir el libro nuevamente a la lista
                adaptador.notifyDataSetChanged() // Notificar al adaptador sobre los cambios
            }
    }

    // Método para mostrar un Snackbar
    private fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(findViewById(R.id.lv_list_libros),
            texto, Snackbar.LENGTH_LONG)
        snack.show()
    }

    // Método para abrir la actividad de CRUD de libros con parámetros
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

        callbackContenido.launch(intentExplicito) // Iniciar la actividad con el callback
    }

    // Método para actualizar la lista de libros desde Firestore
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

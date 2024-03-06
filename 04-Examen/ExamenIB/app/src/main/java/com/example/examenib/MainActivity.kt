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
import com.google.android.material.snackbar.Snackbar
import java.util.ArrayList

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    // Inicialización de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()
    private val autoresCollection = db.collection("autores")
    private val documentNames = ArrayList<String>()

    // Índice del elemento seleccionado en la lista
    private var indexSelectedItem = 0
    private var autorList = ArrayList<Autor>()
    lateinit var adaptador: ArrayAdapter<Autor>

    // Callback para obtener resultados de actividades
    val callbackContenido =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode === Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data
                    val position = data?.getIntExtra("position", -1)
                    if (position != null && position != -1) {
                        adaptador.notifyDataSetChanged() // Actualizar la lista si es necesario
                    }
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
            autorList
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged() // Notificar al adaptador sobre cambios en los datos

        // Cargar los autores desde Firestore
        loadAutores()

        // Obtener los nombres de los documentos de Firestore
        autoresCollection.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val documentId = document.id
                    documentNames.add(documentId)
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al obtener los documentos", exception)
            }

        // Manejo de clics en los elementos de la lista
        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedAutor = autorList[position]
            val explicitIntent = Intent(this, ListViewLibros::class.java)
            explicitIntent.putExtra("AutorName", selectedAutor.nombre)
            explicitIntent.putExtra("nameF", documentNames[indexSelectedItem])
            callbackContenido.launch(explicitIntent)
        }

        // Manejo de clics en el botón de añadir autor
        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_autor)
        botonAnadirListView.setOnClickListener {
            indexSelectedItem = -1 // Reiniciar el índice seleccionado
            abrirActividadConParametros(CrudAutor::class.java)
        }

        // Registrar el ListView para el menú contextual
        registerForContextMenu(listView)
    }

    override fun onResume() {
        super.onResume()
        loadAutores() // Cargar autores al reanudar la actividad
    }

    // Método para cargar los autores desde Firestore
    private fun loadAutores() {
        autoresCollection.get()
            .addOnSuccessListener { result ->
                autorList.clear() // Limpiar la lista antes de cargar nuevos datos
                for (document in result) {
                    val autor = document.toObject(Autor::class.java)
                    autorList.add(autor)
                }
                adaptador.notifyDataSetChanged() // Notificar al adaptador sobre cambios en los datos
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al obtener los documentos", exception)
            }
    }

    // Creación del menú contextual
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
        indexSelectedItem = posicion // Establecer el índice seleccionado
    }

    // Manejo de selecciones en el menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_l -> {
                abrirActividadConParametros(CrudAutor::class.java)
                return true
            }

            R.id.mi_eliminar_l -> {
                val deletedAutor = autorList[indexSelectedItem]
                deletedAutorFromFirestore(deletedAutor) // Eliminar autor de Firestore
                return true
            }

            R.id.mi_libros -> {
                abrirActividadConParametros(ListViewLibros::class.java)
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    // Método para eliminar un autor de Firestore
    private fun deletedAutorFromFirestore(autor: Autor) {
        if (autor.nombre != null) {
            autoresCollection.document(documentNames[indexSelectedItem])
                .delete()
                .addOnSuccessListener {
                    autorList.remove(autor)
                    adaptador.notifyDataSetChanged() // Notificar al adaptador sobre cambios en los datos
                    mostrarSnackbar("Autor eliminado correctamente")
                    if (indexSelectedItem >= autorList.size) {
                        indexSelectedItem = autorList.size - 1
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error al eliminar el distribuidor", e)
                    mostrarSnackbar("No se pudo eliminar el autor")
                }
        } else {
            Log.e(TAG, "No existe autor")
            mostrarSnackbar("No existe autor")
        }
    }

    // Método para mostrar un Snackbar
    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.lv_listviewC),
            texto, Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    // Método para abrir una actividad con parámetros
    private fun abrirActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("posicion", indexSelectedItem)
        if (indexSelectedItem != -1) {
            val selectedAutor = autorList[indexSelectedItem]
            intentExplicito.putExtra("autorNombre", selectedAutor.nombre)
            intentExplicito.putExtra("autorPais", selectedAutor.pais)
            intentExplicito.putExtra(
                "autorEdad",
                selectedAutor.edad
            )
            intentExplicito.putExtra("nameF", documentNames[indexSelectedItem])
        }

        callbackContenido.launch(intentExplicito)
    }
}

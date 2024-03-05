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

    private val db = FirebaseFirestore.getInstance()
    private val autoresCollection = db.collection("autores")
    private val documentNames = ArrayList<String>()

    private var indexSelectedItem = 0
    private var autorList = ArrayList<Autor>()
    lateinit var adaptador: ArrayAdapter<Autor>

    val callbackContenido =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode === Activity.RESULT_OK) {
                if (result.data != null) {
                    // logica negocio
                    val data = result.data
                    val position = data?.getIntExtra("position", -1)
                    if (position != null && position != -1) {
                        adaptador.notifyDataSetChanged()
                    }
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
            autorList
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        loadAutores()

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

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedAutor = autorList[position]
            val explicitIntent = Intent(this, ListViewLibros::class.java)
            explicitIntent.putExtra("AutorName", selectedAutor.nombre)
            explicitIntent.putExtra("nameF", documentNames[indexSelectedItem])
            callbackContenido.launch(explicitIntent)
        }

        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_autor)
        botonAnadirListView.setOnClickListener {
            indexSelectedItem = -1
            abrirActividadConParametros(CrudAutor::class.java)
        }

        registerForContextMenu(listView)
    }

    override fun onResume() {
        super.onResume()
        loadAutores()
    }

    private fun loadAutores() {
        autoresCollection.get()
            .addOnSuccessListener { result ->
                autorList.clear()
                for (document in result) {
                    val autor = document.toObject(Autor::class.java)
                    autorList.add(autor)
                }
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error al obtener los documentos", exception)
            }
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
        indexSelectedItem = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mi_editar_l -> {
                abrirActividadConParametros(CrudAutor::class.java)
                return true
            }

            R.id.mi_eliminar_l -> {
                val deletedAutor = autorList[indexSelectedItem]
                deletedAutorFromFirestore(deletedAutor)
                return true
            }

            R.id.mi_libros -> {
                abrirActividadConParametros(ListViewLibros::class.java)
                return true
            }

            else -> super.onContextItemSelected(item)
        }
    }

    private fun deletedAutorFromFirestore(autor: Autor) {
        if (autor.nombre != null) {
            autoresCollection.document(documentNames[indexSelectedItem])
                .delete()
                .addOnSuccessListener {
                    autorList.remove(autor)
                    adaptador.notifyDataSetChanged()
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

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.lv_listviewC),
            texto, Snackbar.LENGTH_LONG
        )
        snack.show()
    }

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

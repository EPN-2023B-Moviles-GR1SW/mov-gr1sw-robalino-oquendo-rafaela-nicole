package com.example.deberSqlite.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.examen_ib.modelos.Autor

// Clase que ayuda en la interacción con la tabla de Autores en la base de datos SQLite
class SqliteHelperAutor (
    contexto: Context?,
) : SQLiteOpenHelper(
    contexto,
    "sqliteDeber2", // nombre de la base de datos
    null,
    1
) {
    // Método llamado al crear la base de datos
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaAutor =
            """
            CREATE TABLE IF NOT EXISTS AUTOR(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre STRING,
            pais STRING,
            edad INT
            )
        """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaAutor)
    }

    // Método llamado al actualizar la base de datos (no implementado en este caso)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // ("Not yet implemented")
    }

    // Función para insertar un nuevo autor en la tabla AUTOR
    fun insertarAutor(
        nombre: String,
        pais: String,
        edad: Int
    ): Long {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombre", nombre)
            put("pais", pais)
            put("edad", edad)
        }

        return try {
            db.insert("AUTOR", null, valores)
        } catch (e: SQLException) {
            // Manejar errores de inserción si es necesario
            -1
        } finally {
            db.close()
        }
    }

    // Función para actualizar un autor existente en la tabla AUTOR
    fun actualizarAutor(
        nombre: String,
        pais: String,
        edad: Int,
        id: Int
    ): Boolean {
        val db = writableDatabase
        val valoresAActualizar = ContentValues().apply {
            put("nombre", nombre)
            put("pais", pais)
            put("edad", edad)
        }

        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizacion = db.update(
            "AUTOR",
            valoresAActualizar,
            "id=?",
            parametrosConsultaActualizar
        )
        db.close()
        return resultadoActualizacion != -1
    }

    // Función para eliminar un autor de la tabla AUTOR por su ID
    fun eliminarAutor(id: Int): Boolean {
        val db = writableDatabase
        val resultadoEliminacion = db.delete("AUTOR", "id = ?", arrayOf(id.toString()))
        db.close()

        return resultadoEliminacion != -1
    }

    // Función para obtener todos los autores de la tabla AUTOR
    @SuppressLint("Range")
    fun obtenerTodosAutores(): List<Autor> {
        val autores = mutableListOf<Autor>()
        val db = readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT * FROM AUTOR", null)
        } catch (e: SQLException) {
            // Manejar errores de consulta si es necesario
            return emptyList()
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                val pais = cursor.getString(cursor.getColumnIndex("pais"))
                val edad = cursor.getInt(cursor.getColumnIndex("edad"))

                autores.add(Autor(id, nombre, pais, edad, ArrayList()))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        db.close()

        return autores
    }
}
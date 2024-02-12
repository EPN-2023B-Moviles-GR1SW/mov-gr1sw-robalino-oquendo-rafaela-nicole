package com.example.deberSqlite.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.examen_ib.modelos.Libro

// Clase que ayuda en la interacción con la tabla de Libros en la base de datos SQLite
class SqliteHelperLibro(
    contexto: Context?,
) : SQLiteOpenHelper(
    contexto,
    "sqliteDeber2", // nombre de la base de datos
    null,
    1
) {
    // Método llamado al crear la base de datos
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaLibro =
            """
                CREATE TABLE IF NOT EXISTS CARRO(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombreAutor_Libro STRING,
                titulo STRING,
                anioPublicacion INT,
                precio DOUBLE,
                genero STRING,
                idAutor INT,
                FOREIGN KEY (idAutor) REFERENCES AUTOR(id)
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaLibro)
    }

    // Método llamado al actualizar la base de datos (no implementado en este caso)
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // ("Not yet implemented")
    }

    // Función para insertar un nuevo libro en la tabla LIBRO
    fun insertarLibro(
        nombreAutor_Libro: String,
        titulo: String,
        anioPublicacion: Int,
        precio: Double,
        genero: String,
        idAutor: Int
    ): Long {
        val db = writableDatabase
        val valores = ContentValues().apply {
            put("nombreAutor_Libro", nombreAutor_Libro)
            put("titulo", titulo)
            put("anioPublicacion", anioPublicacion)
            put("precio", precio)
            put("genero", genero)
            put("idAutor", idAutor)
        }

        return try {
            db.insert("CARRO", null, valores)
        } catch (e: SQLException) {
            // Manejar errores de inserción si es necesario
            -1
        } finally {
            db.close()
        }
    }

    // Función para actualizar un libro existente en la tabla LIBRO
    fun actualizarLibro(
        nombreAutor_Libro: String,
        titulo: String,
        anioPublicacion: Int,
        precio: Double,
        genero: String,
        idAutor: Int,
        id: Int
    ): Boolean {
        val db = writableDatabase
        val valoresAActualizar = ContentValues().apply {
            put("nombreAutor_Libro", nombreAutor_Libro)
            put("titulo", titulo)
            put("anioPublicacion", anioPublicacion)
            put("precio", precio)
            put("genero", genero)
            put("idAutor", idAutor)
        }

        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizacion = db.update(
            "LIBRO",
            valoresAActualizar,
            "id=?",
            parametrosConsultaActualizar
        )

        db.close()
        return resultadoActualizacion != -1
    }

    // Función para eliminar un libro de la tabla LIBRO por su ID
    fun eliminarLibro(id: Int): Boolean {
        val db = writableDatabase
        val resultadoEliminacion = db.delete("LIBRO", "id = ?", arrayOf(id.toString()))
        db.close()

        return resultadoEliminacion != -1
    }

    // Función para obtener todos los libros de un autor de la tabla LIBRO
    @SuppressLint("Range")
    fun obtenerTodosLibros(idAutor: Int): List<Libro> {
        val libros = mutableListOf<Libro>()
        val db = readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery("SELECT * FROM LIBRO WHERE idAutor = ?", arrayOf(idAutor.toString()))
        } catch (e: SQLException) {
            // Manejar errores de consulta si es necesario
            return emptyList()
        }

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val nombreAutor_Libro = cursor.getString(cursor.getColumnIndex("nombreAutor_Libro"))
                val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
                val anioPublicacion = cursor.getInt(cursor.getColumnIndex("anioPublicacion"))
                val precio = cursor.getDouble(cursor.getColumnIndex("precio"))
                val genero = cursor.getString(cursor.getColumnIndex("genero"))

                libros.add(Libro(id, nombreAutor_Libro, titulo, anioPublicacion, precio, genero, idAutor))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        db.close()

        return libros
    }
}
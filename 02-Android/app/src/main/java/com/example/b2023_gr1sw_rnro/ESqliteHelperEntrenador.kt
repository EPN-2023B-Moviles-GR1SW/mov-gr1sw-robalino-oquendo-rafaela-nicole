package com.example.b2023_gr1sw_rnro

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelperEntrenador(
    contexto: Context?, //THIS
) : SQLiteOpenHelper(
    contexto,
    "moviles",   //Nombre BDD
    null,
    1
){
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaEntrenador =
            """
                CREATE TABLE ENTRENADOR(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                descripcion VARCHAT(50)
                )
            """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaEntrenador)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun crearEntrenador(
        nombre: String,
        descripcion: String
    ) : Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("descripcion", descripcion)
        val resultadoGuardar = basedatosEscritura
            .insert(
                "ENTRENADOR",   //Nombre tabla
                null,
                valoresAGuardar //Valores
            )
        basedatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarEntrenadorFormulario(id:Int):Boolean{
        val conexionEscritura = writableDatabase
        // where ID = ?
        val parametrosConsultaDelete = arrayOf(id.toString() )
        val resultadoEliminacion = conexionEscritura
            // Delete from id = 1 ===> [1]
            .delete(
                "ENTRENADOR",   //Nombre tabla
                "id=?",     //Consulta Where
                parametrosConsultaDelete
            )
        conexionEscritura.close()
        return if(resultadoEliminacion == -1) false else true
    }

    fun actualizarEntrenadorFormulario(
        nombre: String,
        descripcion: String,
        id: Int,
    ):Boolean{
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("descripcion", descripcion)
        // where 10 = ?
        val parametroConsultaActualizar = arrayOf( id.toString() )
        val resultadoActualizacion = conexionEscritura
            .update(
                "ENTRENADOR", //Nombre tabla
                valoresAActualizar, //Valores
                "id=?", //Consulta where
                parametroConsultaActualizar
            )
        conexionEscritura.close()
        return if(resultadoActualizacion == -1) false else true
    }

    fun consultarEntrenadorPorID(id: Int): BEntrenador{
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM ENTRENADOR WHERE ID = ?
        """.trimIndent()
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadoConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura,  //Consulta
            parametrosConsultaLectura,  //Parametros
        )

        //logica busqueda
        val existeUsuario = resultadoConsultaLectura.moveToFirst()
        val usuarioEncontrado = BEntrenador(0, "", "")
        val arreglo = arrayListOf<BEntrenador>()
        if(existeUsuario){
            do{
                val id = resultadoConsultaLectura.getInt(0)
                val nombre = resultadoConsultaLectura.getString(1)
                val descripcion = resultadoConsultaLectura.getString(2)
                if(id != null){
                    // llenar el arreglo con un nuevo BEntrenador
                    usuarioEncontrado.id = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.descripcion = descripcion
                }
            } while (resultadoConsultaLectura.moveToNext())
        }
        resultadoConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado
    }
}
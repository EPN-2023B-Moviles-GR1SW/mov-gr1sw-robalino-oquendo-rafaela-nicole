package com.example.deberSqlite.db

// Clase BaseDatos que actúa como contenedor para objetos de ayuda para interactuar con SQLite
class BaseDatos {

    // Objeto compañero (companion object) que proporciona miembros estáticos a la clase BaseDatos
    companion object{
        // Variable estática que almacenará la instancia de la clase SqliteHelperAutor
        var tablaAutor: SqliteHelperAutor? = null

        // Variable estática que almacenará la instancia de la clase SqliteHelperLibro
        var tablaLibro: SqliteHelperLibro? = null
    }
}

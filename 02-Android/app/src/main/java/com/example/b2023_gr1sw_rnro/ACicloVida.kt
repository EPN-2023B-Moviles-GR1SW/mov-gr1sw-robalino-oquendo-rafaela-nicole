package com.example.b2023_gr1sw_rnro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar

class ACicloVida : AppCompatActivity() {

    var textoGlobal = ""

    fun mostrarSnackBar(texto:String){
        textoGlobal = textoGlobal + " " + texto
        Snackbar
            .make(
                findViewById(R.id.cl_ciclo_vida),   //view
                textoGlobal,    //texto
                Snackbar.LENGTH_INDEFINITE  //tiempo
            )
            .show()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aciclo_vida)
        mostrarSnackBar("OnCreate")
    }

    override fun onStart() {
        super.onStart()
        mostrarSnackBar("OnStart")
    }

    override fun onResume() {
        super.onResume()
        mostrarSnackBar("OnResume")
    }

    override fun onRestart() {
        super.onRestart()
        mostrarSnackBar("OnRestart")
    }

    override fun onPause() {
        super.onPause()
        mostrarSnackBar("OnPause")
    }

    override fun onStop() {
        super.onStop()
        mostrarSnackBar("OnStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        mostrarSnackBar("OnDestroy")
    }
}
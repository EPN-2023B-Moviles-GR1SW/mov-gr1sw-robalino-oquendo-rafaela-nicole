import java.util.Date

fun main() {
    println("Hello World!");

    //**********************************TIPOS DE VARIABLES****************************
    // Inmutables: (NO se reaignan "=")
    val inmutable: String = "Adrian";
    // inmutable = "Vicente";

    //Mutables (Re asignar)
    var mutable: String = "Vicente";
    mutable = "Adrian";

    //val > var
    //Duck Typing
    var ejemploVariable = " Adrian Eguez "  //El compilador sabe que es de tipo String
    val edadEjemplo: Int = 12
    ejemploVariable.trim()
    //ejemploVariable = edadEjemplo;

    //Variable primitiva
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double = 1.2
    val estadoCivil: Char = 'C'
    val mayorEdad: Boolean = true

    //***************************************CLASES Java**********************************
    val fechaNacimiento: Date = Date()

    //***************************************SWITCH****************************************
    val estadoCivilWhen = "C"
    when (estadoCivilWhen) {
        ("C") -> {
            println("Casado")
        }

        "S" -> {
            println("Soltero")
        }

        else -> {
            println("No sabemos")
        }
    }
    val coqueteo = if (estadoCivilWhen == "S") "Si" else "No"


    calcularSueldo(10.00)
    calcularSueldo(10.00, 15.00)
    calcularSueldo(10.00, 12.00, 20.00)
    calcularSueldo(sueldo = 10.00, tasa = 12.00, bonoEspecial = 20.00)      //Parametros nombrados
    calcularSueldo(10.00, bonoEspecial = 20.00)     //Named Parameters
    calcularSueldo(bonoEspecial = 20.00, sueldo = 10.00, tasa = 14.00)  //Parametros nombrados

    //Instancia nueva de la clase
    val sumaUno = Suma(1,1)
    val SumaDos = Suma(null, 1)
    val sumaTres = Suma(1, null)
}
//***************************************FUNCIONES***************************************
//void -> Unit
    fun imprimirNombre(nombre: String): Unit{   //No es necesario poner el Unit.
        //templates strings
        // "Bienvenido: + nombre + " "
        println("Nombre : ${nombre}")
    }

    //Funcion con nulleable
    fun calcularSueldo(
        sueldo: Double, //Requerido
        tasa: Double = 12.00, //Opcional (defecto)
        bonoEspecial: Double? = null, //Opcion null -> nullable    // No se sabe si es double o nulo
    ): Double{
        //Int -> Int? (nullable)
        //String -> String? (nullable)
        //Date -> Date? (nulleable)
        if(bonoEspecial == null){
            return sueldo * (100/tasa)
        }else{
            bonoEspecial.dec()
            return sueldo * (100/tasa) + bonoEspecial
        }
    }

abstract class NumerosJava{
    protected val numeroUno: Int
    protected val numeroDos: Int

    constructor(
        uno: Int,
        dos: Int
    ){ //Bloque del codigo de constructor
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inicializando")
    }
}

abstract class Numeros(//Constructor PRIMARIO
    //Ejemplo:
    // uno: Int,  //(Parametro) (sin modificador de acceso))
    // private var uno: int // Propiedad publica Clase numeros, uno
    // var uno: Int // Propiedad de la clase (ver defecto es PUBLICO
    // public var uno: Int
    // Propiedad de la clase protected numeros.numeroUno
    protected val numeroUno: Int,
    // Propiedad de la clase protected numeros.numeroDno
    protected val numeroDos: Int,
){
    //var cedula: string = "" (public es por defecto)
    // private valorCalculado: int - 0 (private)

    init {
        this.numeroUno; this.numeroDos; //this es opcional
        numeroUno; numeroDos;   // sin el "this" es lo mismo
        println("Inicializando")
    }
}

class Suma( //Constructor Primario Suma
    uno: Int, // Parametro
    dos: Int // Parametro
) : Numeros(uno, dos) {  // <- Constructor del padre
    init {  //Bloque constructor primario
        this.numeroUno; numeroUno;
        this.numeroDos; numeroDos;
    }

    constructor( //Segundo constructor
        uno: Int?, //parametros
        dos: Int // parametros
    ) : this(
        if (uno == null) 0 else uno,
        dos
    ){  //si necesitamos bloque de codigo lo usamos
        numeroUno;
    }

    constructor(    //tercer constructor
        uno: Int,   //parametros
        dos: Int?   //parametros
    ) : this(   //llamada constructor primario
        uno,
        if (dos == null) 0 else uno
    )   //si no lo necesitamos al bloque de codigo  "{}" lo omitimos
}
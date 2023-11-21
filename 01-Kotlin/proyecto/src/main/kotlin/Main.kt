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
    val sumaDos = Suma(null, 1)
    val sumaTres = Suma(1, null)
    val sumaCuatro = Suma(null, null)
    sumaUno.sumar()
    sumaDos.sumar()
    sumaTres.sumar()
    sumaCuatro.sumar()
    println(Suma.pi)
    println(Suma.elevarAlCuadradro(2))
    println(Suma.historialSumas)


    // *************************** ARREGLOS *************************
    // Tipos de arreglos

    //Arreglo Estatico
    val arregloEstatico: Array<Int> = arrayOf<Int>(1, 2, 3)
    println(arregloEstatico)

    //Arreglo Dinamicos
    val arregloDinamico: ArrayList<Int> = arrayListOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)

    // FOR EACH -> Unit
    // Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico
     .forEach { valorActual: Int ->
         println("Valor actual: ${valorActual}")
     }
    // it (en ingles eso) significa el elemento iterado
    arregloDinamico.forEach { println(it)}

    arregloEstatico
        .forEachIndexed { indice: Int, valorActual: Int ->
            println("Valor ${valorActual} Indice: ${indice}")
        }
    println(respuestaForEach)

    // MAP -> Muta el arreglo (Cambia el arreglo)
    // 1) Enviemos el nuevo valor de la iteracion
    // 2) Nos devuelve es un NUEVO ARREGLO con los valores modificados

    val respuestaMap: List<Double> = arregloDinamico
        .map {valorActual : Int ->
            return@map valorActual.toDouble() + 100.00
        }

    println(respuestaMap)
    val respuestaMapDos = arregloDinamico.map { it + 15}

    // Filter -> FILTRAR EL ARREGLO
    // 1) Devolver una expresion (TRUE o FALSE)
    // 2) Nuevo arreglo filtrado
    val respuestaFilter: List<Int> = arregloDinamico
        .filter {valorActual: Int ->
            // Expresion Condicion
            val mayoresACinco: Boolean = valorActual > 5
            return@filter mayoresACinco
        }
    val respuestaFilterDos = arregloDinamico.filter { it <= 5 }
    println(respuestaFilter)
    println(respuestaFilterDos)



    // OR AND
    // OR -> ANY (Alguno cumple?)
    // AND -> ALL (Todos cumplen?)

    val respuestaAny: Boolean = arregloDinamico
        .any {valorActual: Int ->
            return@any (valorActual > 5)
        }
    println(respuestaAny)  //true

    val respuestaAll: Boolean = arregloDinamico
        .all {valorActual: Int ->
            return@all (valorActual > 5)
        }
    println(respuestaAll) //false

    // REDUCE - > Valor acumulado (Acumulador)
    // Valor acumulado = 0 (Siempre 0 en lenguaje Kotlin)
    // [1, 2, 3, 4, 5] -> Sumeme todos los valores del arreglo
    // valorIteracion1 = valorEmpieza + 1 = 0 + 1 = 1 -> Iteracion 1
    // valorIteracion2 = valorIteracion1 + 2 = 1 + 2 = 3 -> Iteracion 2
    // valorIteracion3 = valorIteracion2 + 3 = 3 + 3 = 6 -> Iteracion 3
    // valorIteracion4 = valorIteracion3 + 4 = 6 + 4 = 10 -> Iteracion 4
    // valorIteracion5 = valorIteracion4 + 5 = 10 + 5 = 15 -> Iteracion 5

    val respuestaReduce: Int = arregloDinamico
        .reduce { //acumulado = 0 -> SIEMPRE EMPIEZA EN 0
            acumulado: Int, valorActual: Int ->
            return@reduce (acumulado + valorActual)  // -> Logica negocio
        }
    println(respuestaReduce) //78

    //Ejemplo de operador Reduce
    // valorCarritoActual.cantidad * valorCarritoActual.valor
    // 2 x 195
    // 1 x 10
    // 1 x 10

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

    constructor( //Cuarto constructor
        uno: Int?, //parametros
        dos: Int? //parametros
    ) : this( // llamada al constructor primario
        if (uno == null) 0 else uno,
        if (dos == null) 0 else uno
    )
    // public por defecto, o usar private o protected
    public fun sumar(): Int {
        val total = numeroUno + numeroDos
        // Suma agregarHistorial(total)
        agregarHistorial(total)
        return total
    }
    companion object {
        // Atributos y Metodos "Compartidos" entre las instancias
        val pi = 3.14
        fun elevarAlCuadradro(num: Int): Int {
            return num * num
        }
        val historialSumas = arrayListOf<Int>()
        fun agregarHistorial(valorNuevaSuma: Int){
            historialSumas.add(valorNuevaSuma)
        }
    }


}
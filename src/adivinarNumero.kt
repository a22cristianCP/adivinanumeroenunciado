import java.io.File
import kotlin.random.Random

// Función para generar un número aleatorio de 4 dígitos entre 1 y 9 sin dígitos repetidos
fun numeroAdivinar(): List<Int> {
    val cifras = mutableListOf<Int>()
    while (cifras.size < 4) {
        val digito = Random.nextInt(1, 10)
        if (digito !in cifras) {
            cifras.add(digito)
        }
    }
    return cifras
}

// Función para calcular el número de aciertos y coincidencias comparando el número secreto con el introducido
fun AciertosYcoincidencias(numeroAdivinar: List<Int>, numeroIntroducido: Int): Pair<Int, Int> {
    val numeroIntroducidoDigitos = numeroIntroducido.toString().map { it.toString().toInt() }
    var aciertos = 0
    var coincidencias = 0
    for (i in numeroIntroducidoDigitos.indices) {
        if (numeroIntroducidoDigitos[i] == numeroAdivinar[i]) aciertos++
        if (numeroIntroducidoDigitos[i] in numeroAdivinar) coincidencias++
    }
    return Pair(aciertos, coincidencias)
}

// Función principal del juego
fun main() {
    //Herramientas de formato:
    val BOLD = "\u001B[1m"
    val RED = "\u001B[31m"
    val GREEN = "\u001B[32m"
    val BLUE = "\u001B[34m"
    val RESET = "\u001B[0m"
    val WHITE = "\u001B[37m"

    //Juego:
    val archivo = File("memoria_juego.txt") // Define el archivo una vez

    while (true) {
        var intentos = 3
        val memoria = mutableListOf<String>()
        val numeroAdivinar = numeroAdivinar() // Genera el número secreto para cada juego
        memoria.add("Número secreto: ${numeroAdivinar.joinToString("")}")

        println("${BOLD}${BLUE}Vamos a realizar el juego de adivinar un número.")
        println("${RED}Selecciona una opción:")
        println("${RED}1.${GREEN}Jugar")
        println("${RED}2.${GREEN}Ver último intento")
        println("${RED}3.${GREEN}Salir")

        val opcion = readln().toInt()
        println("${BOLD}${GREEN}Opción $opcion activada")

        when (opcion) {
            3 -> {
                println("${BOLD}${WHITE}Gracias por jugar. Salimos del juego.")
                break
            }
            2 -> {
                if (!archivo.exists()) {
                    println("${BOLD}${WHITE}Aún no hay memoria de partidas, volvemos a empezar.")
                } else {
                    val contenido = archivo.readLines()
                    println("${BOLD}${BLUE}Última partida:${RESET}${BOLD}${WHITE}\n${contenido.joinToString("\n")}")
                    println()
                }
            }
            1 -> {
                println("${BLUE}Tienes 3 intentos para adivinar un número de 4 dígitos entre 1111 y 9999, sin dígitos repetidos.")

                for (i in 1..intentos) {
                    println("${BLUE}Introduce un número:")
                    val numeroIntroducido = readln().toInt()

                    // Validación del número ingresado
                    if (numeroIntroducido > 9999 || numeroIntroducido < 1111 || numeroIntroducido.toString().toSet().size < 4) {
                        intentos--
                        if (intentos == 0) {
                            println("${BOLD}${WHITE}Te has quedado sin intentos. Volvemos al inicio.")
                            break
                        }
                        println("${BOLD}${WHITE}Número no válido. Te quedan $intentos intentos.")
                        continue
                    }

                    // Cálculo de aciertos y coincidencias
                    val (aciertos, coincidencias) = AciertosYcoincidencias(numeroAdivinar, numeroIntroducido)
                    memoria.add("Intento ${4 - intentos}: $numeroIntroducido -> Aciertos: $aciertos, Coincidencias: $coincidencias")

                    if (aciertos == 4) {
                        println("${BOLD}${WHITE}¡¡¡¡ENHORABUENA!!!! Has acertado el número.")
                        break
                    } else {
                        intentos--
                        if (intentos == 0) {
                            println("${BOLD}${WHITE}Te has quedado sin intentos. El número secreto era ${numeroAdivinar.joinToString("")}.Volvemos al inicio.")
                            println()
                            break
                        }
                        println("${BOLD}${WHITE}Tienes $aciertos aciertos y $coincidencias coincidencias.")
                        println("${BOLD}${WHITE}Te quedan $intentos intentos.")
                    }
                }

                // Guardar el historial de la partida en el archivo
                archivo.writeText(memoria.joinToString("\n"))
            }
            else -> println("${BOLD}${WHITE}Opción no válida. Inténtalo de nuevo.")
        }
    }
}
package com.dorjee.terminalhacker.ui.theme

import android.content.Context
import android.util.Base64

class TerminalEngine(
    private val context: Context,
    private val print: (String) -> Unit
) {
    private val files = mutableMapOf(
        "README.txt" to """
            📄 RETO: GhostLog
            Nivel: Fácil
            Tiempo estimado: 3 minutos
            Tags: logs, archivos ocultos, descubrimiento
            
            Un nodo olvidado contiene registros antiguos.
            Localiza una FLAG oculta entre los archivos.
        """.trimIndent(),
        "hint.txt" to "La flag no está donde esperas. Busca en los registros...",
        "logs.txt" to "01:02 [INFO] root login\n01:05 [INFO] archivo oculto: .flag",
        ".flag" to Base64.encodeToString("FLAG{you_found_me_inside_logs}".toByteArray(), Base64.NO_WRAP)
    )

    private val accessed = mutableSetOf<String>()
    private var flagSubmitted = false

    fun processCommand(input: String) {
        val parts = input.trim().split(" ")

        when (parts[0].lowercase()) {
            "help" -> {
                print("Comandos disponibles:\n" +
                        "  ls                 - Lista los archivos\n" +
                        "  cat <archivo>      - Muestra el contenido\n" +
                        "  decode <base64>    - Descifra cadenas base64\n" +
                        "  submit FLAG{...}   - Enviar flag encontrada")
            }

            "ls" -> {
                val showHidden = accessed.contains("logs.txt")
                files.keys.filter {
                    showHidden || !it.startsWith(".")
                }.forEach { print(it) }
            }

            "cat" -> {
                if (parts.size < 2) {
                    print("Uso: cat <archivo>")
                    return
                }
                val file = parts[1]
                if (files.containsKey(file)) {
                    accessed.add(file)
                    if (file == ".flag" && !accessed.contains("logs.txt")) {
                        print(">> Archivo protegido. Revisa los registros primero.")
                    } else {
                        print("--- $file ---")
                        print(files[file]!!)
                    }
                } else {
                    print("Archivo no encontrado: $file")
                }
            }

            "decode" -> {
                if (parts.size < 2) {
                    print("Uso: decode <cadena_base64>")
                    return
                }
                try {
                    val encoded = parts.subList(1, parts.size).joinToString(" ")
                    val decoded = String(Base64.decode(encoded, Base64.NO_WRAP))
                    print("Decodificado: $decoded")
                } catch (e: Exception) {
                    print("❌ Error al decodificar base64")
                }
            }

            "submit" -> {
                if (parts.size < 2) {
                    print("Uso: submit FLAG{...}")
                    return
                }
                val submittedFlag = parts.subList(1, parts.size).joinToString(" ")
                val realFlag = "FLAG{you_found_me_inside_logs}"
                if (submittedFlag == realFlag) {
                    if (!flagSubmitted) {
                        print("✅ FLAG CORRECTA. Nodo completado.")
                        flagSubmitted = true
                    } else {
                        print("Ya has enviado esta flag.")
                    }
                } else {
                    print("❌ FLAG incorrecta.")
                }
            }

            else -> print("Comando no reconocido. Usa `help`.")
        }
    }
}
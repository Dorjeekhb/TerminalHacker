package com.dorjee.terminalhacker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var terminalOutput: TextView
    private lateinit var terminalInput: EditText
    private lateinit var currentNode: String
    private lateinit var fakeFileSystem: Map<String, String>
    private lateinit var prefs: SharedPreferencesHelper
    private var puzzleSolved = false
    private var startTime: Long = 0L

    data class CTFNode(val id: String, val name: String, val difficulty: String)

    private val allCTFNodes = listOf(
        CTFNode("node-001", "GhostLog", "Fácil"),
        CTFNode("node-047", "EncryptedEcho", "Medio"),
        CTFNode("node-admin", "AdminTrap", "Medio"),
        CTFNode("node-vault", "TheVault", "Difícil"),
        CTFNode("node-metadata", "MetaEcho", "Medio"),
        CTFNode("node-ransom", "RansomDrop", "Difícil"),
        CTFNode("node-forensic", "DiskHunter", "Difícil"),
        CTFNode("node-web", "WebCrawler", "Fácil"),
        CTFNode("node-stegano", "ImageTrap", "Difícil")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )

        setContentView(R.layout.activity_main)

        prefs = SharedPreferencesHelper(this)

        terminalOutput = findViewById(R.id.terminal_output)
        terminalInput = findViewById(R.id.terminal_input)
        terminalOutput.movementMethod = ScrollingMovementMethod()

        terminalInput.setOnEditorActionListener { _, _, _ ->
            val input = terminalInput.text.toString()
            terminalInput.setText("")
            processCommand(input)
            true
        }

        currentNode = intent.getStringExtra("NODE_NAME") ?: "node-001"
        fakeFileSystem = getFakeFileSystemForNode(currentNode)
        startTime = System.currentTimeMillis()

        printLine(">> TERMINAL HACKER v1.3")
        printLine(">> connect $currentNode")

        if (prefs.isNodeHacked(currentNode)) {
            printLine(">> STATUS: ✅ Ya hackeado")
            puzzleSolved = true
            printLine(">> Usa `status` para ver tu progreso")
            showLoreForNode(currentNode)
        } else {
            printLine(">> PASSWORD REQUIRED")
            printLine(">> Hint: La clave es '${currentNode.reversed()}' (reverso del nodo)")
            printLine(">> Este nodo contiene un reto CTF. Usa `cat README.txt` para comenzar.")
        }
    }

    private fun processCommand(command: String) {
        printLine("> $command")
        val parts = command.trim().split(" ")

        if (!puzzleSolved) {
            if (command.lowercase() == currentNode.reversed().lowercase()) {
                glitchEffect()
                printLine(">> ACCESS GRANTED to $currentNode")
                printLine(">> Escribe `help` para ver los comandos disponibles.")
                puzzleSolved = true
                prefs.setNodeAsHacked(currentNode)
                vibrate(true)
                showLoreForNode(currentNode)
            } else {
                printLine(">> ACCESS DENIED")
                vibrate(false)
            }
            return
        }

        when (parts[0].lowercase()) {
            "help" -> {
                printLine("Comandos disponibles:")
                printLine("  ls                 - Lista los archivos")
                printLine("  cat <archivo>      - Muestra el contenido")
                printLine("  decrypt <archivo>  - Simula desencriptado")
                printLine("  connect <nodo>     - Simula salto a otro nodo")
                printLine("  submit FLAG{...}   - Verifica la flag del nodo")
                printLine("  status             - Muestra tu progreso global")
                printLine("  graph              - Muestra red de nodos")
                printLine("  clear              - Limpia la terminal")
            }
            "ls" -> {
                fakeFileSystem.keys.forEach { printLine(it) }
            }
            "cat" -> {
                if (parts.size < 2) {
                    printLine("Uso: cat <archivo>")
                } else {
                    val filename = parts[1]
                    val content = fakeFileSystem[filename]
                    if (content != null) {
                        printLine("--- $filename ---")
                        printLine(content)
                    } else {
                        printLine("Archivo no encontrado: $filename")
                    }
                }
            }
            "decrypt" -> {
                if (parts.size < 2) {
                    printLine("Uso: decrypt <archivo>")
                } else {
                    simulateDecryption(parts[1])
                }
            }
            "connect" -> {
                if (parts.size < 2) {
                    printLine("Uso: connect <nodo>")
                } else {
                    val target = parts[1]
                    if (target == currentNode) {
                        printLine(">> Ya estás conectado a $currentNode.")
                    } else {
                        printLine(">> Saltando a $target...")
                        printLine(">> Redireccionando tráfico... OK")
                        printLine(">> Nuevo objetivo: $target")
                        printLine(">> No se pudo establecer conexión real. Simulación terminada.")
                    }
                }
            }
            "submit" -> {
                if (parts.size < 2) {
                    printLine("Uso: submit FLAG{...}")
                } else {
                    val userFlag = parts.subList(1, parts.size).joinToString(" ")
                    val correctFlag = getCorrectFlagForNode(currentNode)
                    if (userFlag == correctFlag) {
                        printLine("🎉 FLAG CORRECTA!")
                        printLine("✅ Nodo completado correctamente.")
                        prefs.setNodeAsHacked(currentNode)
                        val elapsedMillis = System.currentTimeMillis() - startTime
                        prefs.setNodeTime(currentNode, elapsedMillis)
                        printLine("🕒 Tiempo: ${elapsedMillis / 1000} segundos")
                        vibrate(true)
                    } else {
                        printLine("❌ FLAG incorrecta.")
                        vibrate(false)
                    }
                }
            }
            "status" -> {
                printLine("=== PROGRESO CTF ===")
                for (node in allCTFNodes) {
                    val isDone = prefs.isNodeHacked(node.id)
                    val icon = if (isDone) "✔️" else "❌"
                    val time = prefs.getNodeTime(node.id)
                    val seconds = if (time > 0) "${time / 1000}s" else "--"
                    printLine("$icon ${node.name} [${node.difficulty}] → ${node.id} | Tiempo: $seconds")
                }
            }
            "graph" -> {
                startActivity(Intent(this, MapActivity::class.java))
            }
            "clear" -> terminalOutput.text = ""
            else -> printLine("Comando no reconocido. Usa `help`.")
        }
    }

    private fun simulateDecryption(file: String) {
        val content = fakeFileSystem[file]
        if (content == null) {
            printLine("Archivo no encontrado: $file")
            return
        }

        printLine(">> Ejecutando DECRYPT en $file...")
        val animationSteps = listOf(
            "[#####.............] 25%",
            "[##########.......] 50%",
            "[###############..] 75%",
            "[##################] 100%",
            "✓ Decryption complete\n"
        )

        var delay = 300L
        for (step in animationSteps) {
            terminalOutput.postDelayed({ printLine(step) }, delay)
            delay += 300L
        }

        terminalOutput.postDelayed({
            printLine("--- $file (descifrado) ---")
            printLine(content)
        }, delay + 200)
    }

    private fun glitchEffect() {
        val glitchFrames = listOf(
            "█▓▒░█░▒▓█",
            ">>> ### SYSTEM OVERRIDE ### <<<",
            "$%&##//--__--##//&%$",
            ">>> ACCESS GRANTED <<<",
            ""
        )

        var delay = 0L
        for (frame in glitchFrames) {
            terminalOutput.postDelayed({ printLine(frame) }, delay)
            delay += 100L
        }
    }

    private fun vibrate(success: Boolean) {
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = if (success) longArrayOf(0, 100, 50, 100) else longArrayOf(0, 300)
    }

    private fun showLoreForNode(node: String) {
        val lore = when (node) {
            "node-001" -> "Este nodo pertenecía a un operador extinto. Solo quedan ecos de logs antiguos."
            "node-047" -> "Servidor abandonado. El eco cifrado oculta más de lo que parece..."
            "node-admin" -> "Corazón del sistema. La trampa está tendida."
            "node-vault" -> "La bóveda guarda secretos codificados. Solo descifradores entran."
            "node-metadata" -> "Una imagen vale mil datos... o al menos unos metadatos valiosos."
            "node-ransom" -> "Los datos fueron tomados. ¿Podrás recuperar la nota de rescate?"
            "node-forensic" -> "Fragmentos de un disco viejo esconden la pista final."
            "node-web" -> "Un servidor web sin protección. ¿Qué revela su robots.txt?"
            "node-stegano" -> "Una imagen cualquiera. Pero algo dentro no cuadra..."
            else -> "Nada relevante registrado."
        }
        printLine("\n>>> LORE:")
        printLine(lore)
    }

    private fun getCorrectFlagForNode(node: String): String {
        return when (node) {
            "node-001" -> "FLAG{you_found_me_inside_logs}"
            "node-047" -> "FLAG{decrypted_flag_success_047}"
            "node-admin" -> "FLAG{real_admin_flag_hidden}"
            "node-vault" -> "FLAG{base64_flag_VAULTMASTER}"
            "node-metadata" -> "FLAG{metadata_may_reveal_truth}"
            "node-ransom" -> "FLAG{decrypt_the_ransom_note}"
            "node-forensic" -> "FLAG{hidden_in_disk_dump}"
            "node-web" -> "FLAG{robots_txt_leak_exploit}"
            "node-stegano" -> "FLAG{image_hides_truth}"
            else -> ""
        }
    }

    private fun getFakeFileSystemForNode(node: String): Map<String, String> {
        return when (node) {
            "node-001" -> mapOf(
                "README.txt" to "Busca logs.txt. Algo escondido está.",
                "logs.txt" to "01:02 [INFO] root login\n01:05 [INFO] archivo oculto: .flag",
                ".flag" to "FLAG{you_found_me_inside_logs}"
            )
            "node-047" -> mapOf(
                "README.txt" to "Archivo clave: key.enc. Usa decrypt.",
                "key.enc" to "FLAG{decrypted_flag_success_047}"
            )
            // Añade aquí más nodos personalizados como `node-admin`, `node-vault`, etc.
            else -> mapOf("README.txt" to "Nodo vacío o inaccesible.")
        }
    }

    private fun printLine(text: String) {
        terminalOutput.append("$text\n")
    }
}

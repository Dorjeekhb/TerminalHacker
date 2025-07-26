package com.dorjee.terminalhacker

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class NodeSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.BLACK)
            gravity = Gravity.CENTER
            setPadding(40, 100, 40, 100)
        }

        val title = TextView(this).apply {
            text = "🧠 Elige un nodo para hackear"
            setTextColor(Color.GREEN)
            textSize = 22f
            typeface = Typeface.MONOSPACE
            gravity = Gravity.CENTER
            setShadowLayer(10f, 0f, 0f, Color.GREEN)
        }

        layout.addView(title)

        val nodes = listOf(
            Triple("GHOSTLOG [FÁCIL]", "node-001", "#00FF00"),
            Triple("ENCRYPTEDECHO [MEDIO]", "node-047", "#FFFF00"),
            Triple("ADMINTRAP [MEDIO]", "node-admin", "#FFFF00"),
            Triple("THEVAULT [DIFÍCIL]", "node-vault", "#FF0000")
        )

        nodes.forEach { (label, nodeId, colorHex) ->
            val button = Button(this).apply {
                text = label
                setTextColor(Color.parseColor(colorHex))
                textSize = 18f
                typeface = Typeface.MONOSPACE
                setPadding(30, 30, 30, 30)
                gravity = Gravity.CENTER
                background = getGlitchBackground()
                setOnClickListener {
                    val intent = Intent(this@NodeSelectionActivity, MainActivity::class.java)
                    intent.putExtra("NODE_NAME", nodeId)
                    startActivity(intent)
                }
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 30, 0, 0)
            }
            layout.addView(button, params)
        }

        val progressBtn = TextView(this).apply {
            text = "\uD83D\uDCCA VER PROGRESO CTF"
            setTextColor(Color.CYAN)
            textSize = 16f
            gravity = Gravity.CENTER
            setPadding(0, 50, 0, 0)
            typeface = Typeface.MONOSPACE
            setShadowLayer(8f, 0f, 0f, Color.CYAN)
            setOnClickListener {
                startActivity(Intent(this@NodeSelectionActivity, MapActivity::class.java))
            }
        }

        layout.addView(progressBtn)

        setContentView(layout)
    }

    private fun getGlitchBackground(): Drawable {
        val colors = intArrayOf(
            Color.parseColor("#003300"),
            Color.parseColor("#005500"),
            Color.parseColor("#007700")
        )
        return GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors).apply {
            cornerRadius = 20f
        }
    }
}

package com.dorjee.terminalhacker

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class MapActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs = SharedPreferencesHelper(this)
        val layout = ConstraintLayout(this)
        layout.setBackgroundColor(Color.BLACK)

        val nodes = listOf("node-001", "node-047", "node-admin", "node-vault")
        val nodeViews = mutableListOf<TextView>()

        nodes.forEachIndexed { index, nodeId ->
            val tv = TextView(this).apply {
                id = View.generateViewId()
                val hacked = prefs.isNodeHacked(nodeId)
                text = if (hacked) "✔️ $nodeId" else "❌ $nodeId"
                setTextColor(if (hacked) Color.GREEN else Color.RED)
                textSize = 18f
                setPadding(20, 20, 20, 20)
            }
            layout.addView(tv)
            nodeViews.add(tv)
        }

        val set = ConstraintSet()
        set.clone(layout)

        for (i in nodeViews.indices) {
            set.connect(nodeViews[i].id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            set.connect(nodeViews[i].id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            if (i == 0) {
                set.connect(nodeViews[i].id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 100)
            } else {
                set.connect(nodeViews[i].id, ConstraintSet.TOP, nodeViews[i - 1].id, ConstraintSet.BOTTOM, 60)
            }
        }

        set.applyTo(layout)
        setContentView(layout)
    }
}

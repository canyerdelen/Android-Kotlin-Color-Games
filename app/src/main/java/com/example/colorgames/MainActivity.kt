package com.example.colorgames

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    var availableColors = arrayOf(
            Color.RED,
            Color.BLUE,
            Color.GREEN,
            Color.CYAN,
            Color.MAGENTA,
            Color.YELLOW
    )

    val viewIds = arrayOf(
            R.id.view_1,
            R.id.view_2,
            R.id.view_3,
            R.id.view_4,
            R.id.view_5,
            R.id.view_6
    )

    var viewColors = ArrayList<Int>(6)
    var predictionColor = Color.WHITE
    var correctPredictionInRow = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gridLayout = findViewById<GridLayout>(R.id.grid_layout)
        gridLayout.useDefaultMargins = true
        gridLayout.alignmentMode = GridLayout.ALIGN_BOUNDS

        randomizeViewColors()
        setListeners()
    }

    private fun randomizeViewColors() {
        viewColors.clear()

        for (i in 0..5) {
            viewColors.add(availableColors[i])
        }

        viewColors.shuffle()

        for ((i, id) in viewIds.withIndex()) {
            val view = findViewById<View>(id)
            view.setBackgroundColor(viewColors[i])
            view.setTag(i)
        }
    }

    private fun setListeners() {
        for (id in viewIds) {
            val view = findViewById<View>(id)
            view.setOnClickListener { makePrediction(view) }
        }
    }

    fun onStartClicked(view: View) {
        for (id in viewIds) {
            val view = findViewById<View>(id)
            view.setBackgroundColor(Color.BLACK)
        }

        prepareNextPrediction()
    }

    private fun prepareNextPrediction() {
        var availableViewList = ArrayList<View>();
        for (id in viewIds) {
            val view = findViewById<View>(id)
            if ((view.background as ColorDrawable).color == Color.BLACK) {
                availableViewList.add(view)
            }
        }

        var view = availableViewList[Random.nextInt(0, availableViewList.size - 1)]
        predictionColor = viewColors[view.tag.toString().toInt()]
        (findViewById<View>(R.id.view_guess)).setBackgroundColor(predictionColor)
    }

    private fun makePrediction(view: View) {
        val index = view.getTag().toString().toInt()
        val viewColor = viewColors[index]
        view.setBackgroundColor(viewColor)

        if (viewColor == predictionColor) {
            correctPredictionInRow++
            prepareNextPrediction()

        } else {
            gameLost()
            return
        }

        if (correctPredictionInRow == 3) {
            gameWon()
        }
    }

    private fun gameLost() {
        val btn = findViewById<Button>(R.id.Start)
        btn.text = "YOU LOST!"
        btn.setBackgroundColor(Color.RED)
    }

    private fun gameWon() {
        val btn = findViewById<Button>(R.id.Start)
        btn.text = "YOU WON!"
        btn.setBackgroundColor(Color.GREEN)
    }
}

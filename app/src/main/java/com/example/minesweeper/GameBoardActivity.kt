package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins

class GameBoardActivity : AppCompatActivity() {
    lateinit var board: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)

        board = findViewById(R.id.board)
        val minesCount: TextView = findViewById(R.id.mines)
        val time: TextView = findViewById(R.id.time)
        val restartGame: Button = findViewById(R.id.restart)

        val rows: Int = intent.getIntExtra("row", 0)
        val cols: Int = intent.getIntExtra("col", 0)
        val mines: Int = intent.getIntExtra("mines", 0)

        createGameBoard(rows, cols, mines)
    }

    private fun createGameBoard(rows: Int, cols: Int, mines: Int){
        var counter = 1

        val params1 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            0
        )

        val params2 = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT
        ).apply {
            setMargins(2)
        }

        for(i in 1..rows){
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.layoutParams = params1
            params1.weight = 1.0F

            for(j in 1..cols){
                val button = Button(this)
                button.id = counter
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.lavender))
                button.layoutParams = params2
                params2.weight = 1.0F
                button.setOnClickListener{
                    button.isEnabled = false
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.mauve))
                    println("THIS IS BUTTON CLICKED")
                }
                button.setOnLongClickListener{
                    Toast.makeText(this, "Long Button Event Clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                linearLayout.addView(button)
                counter++
            }
            board.addView(linearLayout)
        }
    }
}
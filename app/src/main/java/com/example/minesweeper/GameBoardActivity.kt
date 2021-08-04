package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
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
        val gameBoard = Minesweeper(rows, cols, mines)

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
                val button = ImageButton(this)
                button.id = counter
                button.tag = "${i-1}${j-1}"
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.lavender))
                button.layoutParams = params2
                params2.weight = 1.0F
                button.setOnClickListener{
                    button.isEnabled = false
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.mauve))
                    val currentRow: Int = button.tag.toString().get(0).minus('0')
                    val currentCol: Int = button.tag.toString().get(1).minus('0')
                    gameBoard.move(1, currentRow, currentCol)
                }
                button.setOnLongClickListener{
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.mauve))
                    button.setImageResource(R.drawable.red_flag)
                    button.scaleType = ImageView.ScaleType.CENTER
                    button.adjustViewBounds = true
                    true
                }
                linearLayout.addView(button)
                counter++
            }
            board.addView(linearLayout)
        }
    }
}
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
        gameBoard.setMines()

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

        for(i in 0 until rows){
            val linearLayout = LinearLayout(this)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.layoutParams = params1
            params1.weight = 1.0F

            for(j in 0 until cols){
                val button = Button(this)
                button.id = counter
                button.tag = "not marked"
                button.setBackgroundColor(ContextCompat.getColor(this, R.color.lavender))
                button.layoutParams = params2
                params2.weight = 1.0F
                button.setOnClickListener{
                    if(button.tag != "marked"){
                        gameBoard.move(1, i, j)
                        updateBoard(rows, cols, gameBoard)
                    }
                }
                button.setOnLongClickListener{
                    if(button.tag == "not marked") button.tag = "marked" else button.tag = "not marked"
                    gameBoard.move(2, i, j)
                    updateBoard(rows, cols, gameBoard)
                    true
                }
                linearLayout.addView(button)
                counter++
            }
            board.addView(linearLayout)
        }
    }

    private fun updateBoard(row: Int, col: Int, gameBoard: Minesweeper){
        var button: Button
        val board = gameBoard.board
        val status = gameBoard.status
        var counter = 1;
        for(i in 0 until row){
            for(j in 0 until col){
                button = findViewById(counter++)

                if(status == Status.ONGOING){
                    if(board[i][j].isRevealed){
                        if(board[i][j].value != 0){
                            button.text = board[i][j].value.toString()
                        }
                        button.isEnabled = false
                        button.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                    }

                    if(!board[i][j].isRevealed){
                        button.isEnabled = true
                        button.setBackgroundColor(ContextCompat.getColor(this, R.color.lavender))
                    }

                    if(board[i][j].isMarked){
//                        button.isEnabled = false
                        button.setBackgroundResource(R.drawable.red_flag)
                    }

                    if(!board[i][j].isMarked){
//                        button.isEnabled = true
                        button.setBackgroundResource(0)
                        button.setBackgroundColor(ContextCompat.getColor(this, R.color.lavender))
                    }
                }
                if(status == Status.LOST){
                    button.isEnabled = false
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.mauve))
                    if(board[i][j].value == -1){
                        button.setBackgroundResource(R.drawable.bomb)
                    }
                }
            }
        }

        if(status == Status.LOST){
            Toast.makeText(this,"You've lost the game", Toast.LENGTH_SHORT).show()
        }

        if(status == Status.WON){
            Toast.makeText(this,"You've won the game", Toast.LENGTH_SHORT).show()
        }
    }
}
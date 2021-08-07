package com.example.minesweeper

import android.content.Context
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import com.google.gson.Gson
import java.lang.Math.ceil

class GameBoardActivity : AppCompatActivity() {
    lateinit var board: LinearLayout
    lateinit var minesCount: TextView
    lateinit var gameBoard: Minesweeper
    lateinit var restartGame: Button
    lateinit var timer: Chronometer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)

        board = findViewById(R.id.board)
        minesCount = findViewById(R.id.mines)
        timer = findViewById(R.id.time)
        restartGame = findViewById(R.id.restart)

        val rows: Int = intent.getIntExtra("row", 0)
        val cols: Int = intent.getIntExtra("col", 0)
        val mines: Int = intent.getIntExtra("mines", 0)

        restartGame.setOnClickListener{
            val intent = intent
            finish()
            overridePendingTransition(0,0)
            startActivity(intent)
        }

        createGameBoard(rows, cols, mines)
        timer.start()
    }

    private fun createGameBoard(rows: Int, cols: Int, mines: Int){
        gameBoard = Minesweeper(rows, cols, mines)
        gameBoard.setMines()
        minesCount.text = gameBoard.minesLeft.toString()

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
                button.setTextColor(ContextCompat.getColor(this, R.color.white))
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
        var counter = 1
        for(i in 0 until row){
            for(j in 0 until col){
                button = findViewById(counter++)

                if(status == Status.ONGOING){
                    if(board[i][j].isRevealed){
                        if(board[i][j].value != 0){
                            button.text = board[i][j].value.toString()
                        }
                        button.setBackgroundColor(ContextCompat.getColor(this, R.color.mauve))
                        button.isEnabled = false
                    }

                    if(!board[i][j].isRevealed){
                        button.isEnabled = true
                        button.setBackgroundColor(ContextCompat.getColor(this, R.color.lavender))
                    }

                    if(board[i][j].isMarked){
                        button.setBackgroundResource(R.drawable.red_flag)
                    }
                }
                else if(status == Status.LOST){
                    button.isEnabled = false
                    button.setBackgroundColor(ContextCompat.getColor(this, R.color.mauve))
                    if(board[i][j].value == -1){
                        button.setBackgroundResource(R.drawable.bomb)
                    }
                }
                else{
                    button.isEnabled = false
                    if(!board[i][j].isMarked){
                        button.setBackgroundColor(ContextCompat.getColor(this, R.color.mauve))
                    }
                }
            }
        }

        minesCount.text = gameBoard.minesLeft.toString()

        if(status == Status.LOST){
            timer.stop()
            Toast.makeText(this,"You've lost the game", Toast.LENGTH_SHORT).show()
            restartGame.isVisible = true
        }

        if(status == Status.WON){
            timer.stop()
            Toast.makeText(this,"You've won the game", Toast.LENGTH_SHORT).show()
            restartGame.isVisible = true
            saveTime()
        }
    }

    private fun saveTime(){
        val time = ceil((SystemClock.elapsedRealtime() - timer.base - 1000) /1000.0).toInt()
        val sharedPref = getSharedPreferences(GAME_PREF, Context.MODE_PRIVATE)
        val gson = Gson()
        var bestTime: String = time.toString()

        val timeData = sharedPref.getString(GAME_DATA, null)
        val prevGameData: GameData? = gson.fromJson(timeData, GameData::class.java)

        if(prevGameData?.bestTime != null){
            if(time > prevGameData.bestTime.toInt()){
                bestTime = prevGameData.bestTime
            }
        }

        val gameData = gson.toJson(GameData(bestTime, time.toString()))

        with(sharedPref.edit()){
            putString(GAME_DATA, gameData)
            commit()
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        with(builder){
            setTitle(getString(R.string.exit_game))
            setMessage(getString(R.string.exit_message))

            setPositiveButton(getString(R.string.yes)){
                _, _ ->
                super.onBackPressed()
            }

            setNegativeButton(getString((R.string.no))){
                _, _ ->
            }
            show()
        }
    }
}
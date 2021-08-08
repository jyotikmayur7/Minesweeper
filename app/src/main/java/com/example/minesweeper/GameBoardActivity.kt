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

        // Taking reference from UI and connecting the views in the Activity
        board = findViewById(R.id.board)
        minesCount = findViewById(R.id.mines)
        // Here timer is of type Chronometer view, which is more suitable for Timer type operations
        timer = findViewById(R.id.time)
        restartGame = findViewById(R.id.restart)

        // Loading parameters (Row, Cols, Mines) from intent
        val rows: Int = intent.getIntExtra("row", 0)
        val cols: Int = intent.getIntExtra("col", 0)
        val mines: Int = intent.getIntExtra("mines", 0)

        // Using Intent to restart the current activity on restartGame button click event when the game finishes
        restartGame.setOnClickListener{
            val intent = intent
            finish()
            overridePendingTransition(0,0)
            startActivity(intent)
        }

        // Creating the game board based on the argument received from intent
        createGameBoard(rows, cols, mines)

        // Once the game board is created, this starts the game timer
        timer.start()
    }

    // Creating dynamic game board with respect to the arguments passed in. Using 2 Linear Layouts for creating Button Matrix
    private fun createGameBoard(rows: Int, cols: Int, mines: Int){
        // Creating Minesweeper object and setting mines on the board
        gameBoard = Minesweeper(rows, cols, mines)
        gameBoard.setMines()
        minesCount.text = gameBoard.minesLeft.toString()

        // Initialising and later using this variable for button's id
        var counter = 1

        // Setting up parameters beforehand for the Linear Layouts
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

        // Creating the game board dynamically
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
                // Setting the click events on the button
                // Here only allowing non marked or non flagged button to be clicked in the board
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

    // This function is getting called after every button click in the matrix and this function is responsible for updating the UI (game board).
    // It compares the board matrix present in the memory with the buttons matrix which is shown in the UI and updates the UI accordingly
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

        when(status){
            Status.LOST -> {
                timer.stop()
                Toast.makeText(this,"You've lost the game", Toast.LENGTH_SHORT).show()
                restartGame.isVisible = true
            }
            Status.WON -> {
                timer.stop()
                Toast.makeText(this,"You've won the game", Toast.LENGTH_SHORT).show()
                restartGame.isVisible = true
                saveTime()
            }
        }
    }

    // This function is called when the Users wins the game
    // This function is responsible for updating the Shared Preference, it checks first if the current time is better than previous saved best times and updates accordingly
    private fun saveTime(){
        // Getting time from the timer (Chronometer View) and converting it to seconds
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

    // This function is called when back button is pressed by user, it asks user in form of AlertDialog if they actually want exit the game at that level or not and perform operation accordingly
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
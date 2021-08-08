package com.example.minesweeper

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson

// Using these constants for shared preference
const val GAME_DATA = "GAME_DATA"
const val GAME_PREF = "GAME_PREF"

class MainActivity : AppCompatActivity() {
        lateinit var bestTime: TextView
        lateinit var lastGameTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Taking reference from UI and connecting the views in the Activity
        bestTime = findViewById(R.id.best_time)
        lastGameTime = findViewById(R.id.last_game_time)
        val easy = findViewById<Button>(R.id.easy)
        val medium = findViewById<Button>(R.id.medium)
        val hard = findViewById<Button>(R.id.hard)
        val createCustomBoard = findViewById<Button>(R.id.create_custom_board)

        // Loading game data from the shared preference
        loadGameData()

        // Showing game board with respect to the game level when the respected button is clicked
        easy.setOnClickListener{
            showGameBoard(8, 8, 7)
        }

        medium.setOnClickListener{
            showGameBoard(10, 8, 15)
        }

        hard.setOnClickListener{
            showGameBoard(10, 10, 20)
        }

        // Showing AlertDialog and asking User for number of Rows, Cols and Mines for their custom board
        createCustomBoard.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val dialogView: View = inflater.inflate(R.layout.custom_game_add, null)
            val rowsView: EditText = dialogView.findViewById(R.id.rows)
            val colsView: EditText = dialogView.findViewById(R.id.cols)
            val minesView: EditText = dialogView.findViewById(R.id.mines)
            setErrorListener(rowsView)
            setErrorListener(colsView)
            setErrorListener(minesView)
            with(builder){
                setView(dialogView)
                setTitle(getString(R.string.add_details))

                setPositiveButton(getString(R.string.add)){
                    _, _ ->
                    val row = rowsView.text.toString()
                    val col = colsView.text.toString()
                    val mines = minesView.text.toString()
                    if(row.isNotEmpty() && col.isNotEmpty() && mines.isNotEmpty()){
                        showGameBoard(row.toInt(), col.toInt(), mines.toInt())
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Invalid Input", Toast.LENGTH_SHORT).show()
                    }
                }

                setNegativeButton(getString(R.string.cancel)){
                    _, _ ->
                }
                show()
            }
        }

    }

    // When user return from GameBoardActivity, this onResume() function will reload the data from shared preference and updates the UI
    override fun onResume() {
        super.onResume()
        loadGameData()

    }

    // This function is called when level(easy, medium, hard or custom) button gets clicked, this function uses intent and passes the row, cols and mines to the GameBoardActivity
    private fun showGameBoard(row: Int, col: Int, mines: Int){
        val intent = Intent(this, GameBoardActivity::class.java).apply {
            putExtra("row", row)
            putExtra("col",col)
            putExtra("mines", mines)
        }
        startActivity(intent)
    }

    // This function loads game data ('Best Time', 'Last Game Time') from the memory and updates the UI accordingly
    private fun loadGameData(){
        val sharedPref = getSharedPreferences(GAME_PREF, Context.MODE_PRIVATE)
        val gameDataJson = sharedPref.getString(GAME_DATA, null)
        val gson = Gson()
        val gameData: GameData? = gson.fromJson(gameDataJson, GameData::class.java)
        bestTime.text = getString(R.string.best_time, gameData?.bestTime?:" - -")
        lastGameTime.text = getString(R.string.last_game_time, gameData?.lastGameTime?:" - -")
    }

    // This function is used when AlertDialog for custom board creation pops up. This function provides error handling for the EditText present in the AlertDialog
    private fun setErrorListener(editText: EditText){
        editText.error = if(editText.text.toString().isNotEmpty()) null else "Field Cannot be Empty"
        with(editText) {
            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    error = if(text.toString().isNotEmpty()) null else "Field Cannot be Empty"
                }
            })
        }
    }
}
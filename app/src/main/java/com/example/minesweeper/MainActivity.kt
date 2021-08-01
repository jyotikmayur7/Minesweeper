package com.example.minesweeper

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson

const val GAME_DATA = "GAME_DATA"

class MainActivity : AppCompatActivity() {
        lateinit var bestTime: TextView
        lateinit var lastGameTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bestTime = findViewById(R.id.best_time)
        lastGameTime = findViewById(R.id.last_game_time)
        val easy = findViewById<Button>(R.id.easy)
        val medium = findViewById<Button>(R.id.medium)
        val hard = findViewById<Button>(R.id.hard)
        val createCustomBoard = findViewById<Button>(R.id.create_custom_board)

        loadGameData()
    }

    private fun loadGameData(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val gameDataJson = sharedPref.getString(GAME_DATA, null)
        val gson = Gson()
        val gameData: GameData = gson.fromJson(gameDataJson, GameData::class.java)
        bestTime.text = getString(R.string.best_time, gameData.bestTime)
        lastGameTime.text = getString(R.string.last_game_time, gameData.lastGameTime)
    }
}
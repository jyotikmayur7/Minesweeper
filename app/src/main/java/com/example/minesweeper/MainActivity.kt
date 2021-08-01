package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
        lateinit var gameData: GameData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bestTime = findViewById<TextView>(R.id.best_time)
        val lastGameTime = findViewById<TextView>(R.id.last_game_time)
        val easy = findViewById<Button>(R.id.easy)
        val medium = findViewById<Button>(R.id.medium)
        val hard = findViewById<Button>(R.id.hard)
        val createCustomBoard = findViewById<Button>(R.id.create_custom_board)
    }
}
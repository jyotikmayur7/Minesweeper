package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {
    lateinit var bestTime: EditText
    lateinit var lastGameTime: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bestTime = findViewById(R.id.best_time)
        lastGameTime = findViewById(R.id.last_game_time)
        val easy = findViewById<Button>(R.id.easy)
        val medium = findViewById<Button>(R.id.medium)
        val hard = findViewById<Button>(R.id.hard)
        val createCustomBoard = findViewById<Button>(R.id.create_custom_board)
    }
}
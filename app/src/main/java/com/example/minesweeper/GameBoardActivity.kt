package com.example.minesweeper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class GameBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_board)

        val minesCount: TextView = findViewById(R.id.mines)
        val time: TextView = findViewById(R.id.time)
        val restartGame: Button = findViewById(R.id.restart)

        val rows: Int = intent.getIntExtra("row", 0)
        val cols: Int = intent.getIntExtra("col", 0)
        val mines: Int = intent.getIntExtra("mines", 0)




    }
}
package com.example.minesweeper

import kotlin.random.Random

class Minesweeper(private val row: Int, private val col: Int, private val mines: Int) {
    val board = Array(row){Array(col){MineCell()}}
    val status = Status.ONGOING
    private var moves = 0
    var minesCount = 0
    private val MINE = -1

    fun setMines(){
        var count = 0
        while(count != minesCount){
            val randomRow = Random.nextInt(0,row)
            val randomCol = Random.nextInt(0, col)

            if(board[randomRow][randomRow].value != MINE){
                board[randomRow][randomCol].value = MINE
                count++
                updateNeighbours()
            }
        }
    }

    fun updateNeighbours(){

    }
}
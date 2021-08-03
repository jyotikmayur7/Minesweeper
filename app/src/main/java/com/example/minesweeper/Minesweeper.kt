package com.example.minesweeper

import android.widget.Button
import androidx.core.content.ContextCompat
import kotlin.random.Random

class Minesweeper(private val row: Int, private val col: Int, private val mines: Int) {
    val board = Array(row){Array(col){MineCell()}}
    var status = Status.ONGOING
    private var moves = 0
    var minesCount = 0
    private val MINE = -1
    private val xDir = arrayOf(-1,-1,0,1,1,1,0,-1)
    private val yDir = arrayOf(0,1,1,1,0,-1,-1,-1)

    fun setMines(){
        var count = 0
        while(count != minesCount){
            val randomRow = Random.nextInt(0,row)
            val randomCol = Random.nextInt(0, col)

            if(board[randomRow][randomRow].value != MINE){
                board[randomRow][randomCol].value = MINE
                count++
                updateNeighbours(randomRow, randomCol)
            }
        }
    }

    private fun updateNeighbours(currentRow: Int, currentCol: Int){
        for(i in xDir.indices){
            val xStep = currentRow + xDir[i]
            val yStep = currentCol + yDir[i]

            if(xStep in 0 until row && yStep in 0 until col && board[xStep][yStep].value != MINE){
                board[xStep][yStep].value++
            }
        }
    }

    fun move(choice: Int, currentRow: Int, currentCol: Int): Boolean{
        if(choice == 1){
            if(board[currentRow][currentCol].isMarked || board[currentRow][currentCol].isRevealed){
                return false
            }

            if(board[currentRow][currentCol].value == MINE){
                status = Status.LOST
                return true
            }

            if(board[currentRow][currentCol].value == 0){
                //exploreCells - Do DFS
            }
            else{
                board[currentRow][currentCol].isRevealed = true
                moves++
            }
        }
        else{
            board[currentRow][currentCol].isMarked = !board[currentRow][currentCol].isMarked
        }

        if(moves + minesCount == row*col){
            status = Status.WON
        }
        return true
    }
}
package com.example.minesweeper

import android.widget.Button
import androidx.core.content.ContextCompat
import kotlin.random.Random

class Minesweeper(private val row: Int, private val col: Int, private val mines: Int) {
    var board = Array(row){Array(col){MineCell()}}
    var status = Status.ONGOING
    var minesLeft = mines
    private var moves = 0
    private val MINE = -1
    private val xDir = arrayOf(-1,-1,0,1,1,1,0,-1)
    private val yDir = arrayOf(0,1,1,1,0,-1,-1,-1)

    fun setMines(){
        var count = 0
        while(count != mines){
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
                exploreCells(currentRow, currentCol)
            }
            else{
                board[currentRow][currentCol].isRevealed = true
                moves++
            }
        }
        else{
            board[currentRow][currentCol].isMarked = !board[currentRow][currentCol].isMarked
            if(board[currentRow][currentCol].isMarked) minesLeft-- else minesLeft++
        }

        if(moves + mines == row*col){
            status = Status.WON
        }
        return true
    }

    private fun exploreCells(x: Int, y: Int){
        if(x >= row || x < 0 || y >= col || y < 0 || board[x][y].value == MINE || board[x][y].isRevealed || board[x][y].isMarked){
            return
        }

        moves++

        if(board[x][y].value != 0){
            board[x][y].isRevealed = true
            return
        }

        board[x][y].isRevealed = true

        for(i in xDir.indices){
            val xStep = xDir[i]
            val yStep = yDir[i]

            exploreCells(x+xStep, y+yStep)
        }
    }

    fun resetBoard(){
        board = Array(row){Array(col){MineCell()}}
        status = Status.ONGOING
        moves = 0
        this.setMines()
    }

}
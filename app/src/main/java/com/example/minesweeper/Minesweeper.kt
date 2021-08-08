package com.example.minesweeper

import android.widget.Button
import androidx.core.content.ContextCompat
import kotlin.random.Random
// This class is responsible for Minesweeper game operations on the board created here and UI uses this as reference to update itself
class Minesweeper(private val row: Int, private val col: Int, private val mines: Int) {
    var board = Array(row){Array(col){MineCell()}}
    var status = Status.ONGOING
    var minesLeft = mines
    private var moves = 0
    private val MINE = -1
    private val xDir = arrayOf(-1,-1,0,1,1,1,0,-1)
    private val yDir = arrayOf(0,1,1,1,0,-1,-1,-1)

    // Setting up mines on random location and putting correct values on neighbour cells
    fun setMines(){
        var count = 0
        while(count != mines){
            val randomRow = Random.nextInt(0,row)
            val randomCol = Random.nextInt(0, col)

            if(board[randomRow][randomCol].value != MINE){
                board[randomRow][randomCol].value = MINE
                count++
                updateNeighbours(randomRow, randomCol)
            }
        }
    }

    // This function is called when mines were setting up
    // It is responsible for updating the cell's value which is neighbour of mine
    private fun updateNeighbours(currentRow: Int, currentCol: Int){
        for(i in xDir.indices){
            val xStep = currentRow + xDir[i]
            val yStep = currentCol + yDir[i]

            if(xStep in 0 until row && yStep in 0 until col && board[xStep][yStep].value != MINE){
                board[xStep][yStep].value++
            }
        }
    }

    // Making move on the board with respect to the user's choice
    fun move(choice: Int, currentRow: Int, currentCol: Int): Boolean{
        // When the choice is 1, it checks it the move is possible and then make moves accordingly
        if(choice == 1){
            if(board[currentRow][currentCol].isMarked || board[currentRow][currentCol].isRevealed){
                return false
            }

            if(board[currentRow][currentCol].value == MINE){
                status = Status.LOST
                return true
            }

            // When the move is possible if the current cell's value is 0 then it explores all the cells and updates them and if cells value is 1 it updates the cells accordingly
            if(board[currentRow][currentCol].value == 0){
                exploreCells(currentRow, currentCol)
            }
            else{
                board[currentRow][currentCol].isRevealed = true
                moves++
            }
        }
        // When the choice is 2, it checks if marking the cell is possible or not
        else{
            if(minesLeft == 0){
                if(board[currentRow][currentCol].isMarked){
                    board[currentRow][currentCol].isMarked = false
                    minesLeft++;
                }
            }
            else{
                board[currentRow][currentCol].isMarked = !board[currentRow][currentCol].isMarked
                if(board[currentRow][currentCol].isMarked) minesLeft-- else minesLeft++
            }
        }

        // Checking for Win, basically when all the cells are explored/used without making a wrong move, the user win's the game
        if(moves + mines == row*col){
            status = Status.WON
        }
        return true
    }

    // Exploring the cells in all directions and stopping until we hit mines neighbour
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
}
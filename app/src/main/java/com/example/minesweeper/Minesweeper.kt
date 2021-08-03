package com.example.minesweeper

class Minesweeper(private val row: Int, private val col: Int, private val mines: Int) {
    val board = Array(row){Array(col){MineCell()}}
    val status = Status.ONGOING
    private var moves = 0
    var minesCount = 0
    private val MINES = -1

}
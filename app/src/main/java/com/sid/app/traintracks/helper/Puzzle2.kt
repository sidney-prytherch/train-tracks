package com.sid.app.traintracks.helper

import android.util.Log
import kotlin.random.Random

class Puzzle2 {

    // maze including walls - walls are 1, path pieces are 0
    fun generateMaze(size: Int): Array<IntArray> {

        // Initialize
        val maze = Array(size) { IntArray(size) }
        for (row in maze)
            for (i in 0 until size)
                row[i] = 1

        // find random starting cell
        val firstRow: Int = Random.nextInt(size / 2) * 2 + 1
        val firstCol: Int = Random.nextInt(size / 2) * 2 + 1

        //　Allocate the maze with recursive method
        recursion(firstRow, firstCol, 0, 0, maze)

        for (row in maze) {
            var rowString = ""
            for (i in 0 until size)
                rowString += if (row[i] == 1) "██ " else "░░ "
            Log.v("puzzling", rowString)
        }
        return maze
    }

    private fun recursion(r: Int, c: Int, prevR: Int, prevC:Int, maze: Array<IntArray>) {
        // set connecting path pieces to path (0)
        maze[r][c] = 0
        maze[r + prevR][c + prevC] = 0
        // for each random direction, if the potential path in that direction isn't out of bounds
        // and isn't already part of the path, add it to the path
        val randDirs: IntArray = List(4) { it }.shuffled().toIntArray()
        for (dir in randDirs) {
            when (dir) {
                0 -> if (r - 2 > 0 && maze[r - 2][c] != 0)
                    recursion(r - 2, c, 1, 0, maze)
                1 -> if ( c + 2 < maze.size - 1 && maze[r][c + 2] != 0)
                    recursion(r, c + 2, 0, -1, maze)
                2 -> if (r + 2 < maze.size - 1 && maze[r + 2][c] != 0)
                    recursion(r + 2, c, -1, 0, maze)
                3 -> if (c - 2 > 0 && maze[r][c - 2] != 0)
                    recursion(r, c - 2, 0, 1, maze)
            }
        }
    }
}
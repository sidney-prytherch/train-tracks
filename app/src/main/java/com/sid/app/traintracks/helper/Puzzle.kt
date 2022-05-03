package com.sid.app.traintracks.helper

import android.util.Log
import java.util.*
import kotlin.random.Random

class Puzzle() {
//    While (# Walls Down < Total # Cells - 1)
//      Choose random cell current
//      Choose random neighbor of current that has a wall up between it and current
//      If such a neighbor exists
//          Find the labels of current and neighbor
//          If they are different,
//              union them, knock down the wall, and add to # Walls Down

    fun generateMaze(size: Int): Array<Triple<Int, Int, Int>> {
        var id = 0
        val sets = Array(size) { Array(size) { Tree(id++) } }
        val edges = Array(2 * (size * (size - 1))) { Triple(0, 0, true) }
        var finalEdges = Array((size - 1) * (size - 1)) { Triple(0, 0, true) }
        var index = 0
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (row < size - 1) // if it's not the final row, you can go down, so
                    edges[index++] = Triple(row, col, false) // add bottom edge
                if (col < size - 1) // if it's not the final col, you can go right, so
                    edges[index++] = Triple(row, col, true) // add right edge
            }
        }
        edges.shuffle(Random(Calendar.getInstance().timeInMillis))
        var finalEdgeIndex = 0
        for ((row, col, isRightEdge) in edges) {
            val cell1 = sets[row][col]
            val cell2 = if (isRightEdge) sets[row][col + 1] else sets[row + 1][col]
            if (cell1.getRoot() !== cell2.getRoot()) {
                //Log.v("puzzling", "cell A: ${cell1.getRoot().id}, cell B: ${cell2.getRoot().id}")
//                Log.v("puzzling", "[$row][$col][${if (isRightEdge) "right" else "down"}] -> " + if (isRightEdge) "[${row * 2}][${col * 2 + 1}]" else "[${row * 2 + 1}][${col * 2}]")
                cell1.addChild(cell2)
                //Log.v("puzzling", "-    A: ${cell1.getRoot().id},      B: ${cell2.getRoot().id}")
            } else {
                //Log.v("puzzling", "$finalEdgeIndex")
                finalEdges[finalEdgeIndex++] = Triple(row, col, isRightEdge)
            }
        }
        val maze =
            Array(size * 2 - 1) { row -> Array(size * 2 - 1) { col -> if (row % 2 == 0 || col % 2 == 0) 0 else 1 } }
        for ((row, col, isRightEdge) in finalEdges) {
            if (isRightEdge) {
                maze[row * 2][col * 2 + 1] = 1
            } else {
                maze[row * 2 + 1][col * 2] = 1
            }
        }
        printMaze(maze)

        return createLongestRoute(maze).map { i -> Triple(i.first / 2, i.second / 2, i.third) }
            .toTypedArray()
    }

    private fun printMaze(maze: Array<Array<Int>>): String {
        var string = ""
        var blankRowString = "██ ██ "
        for (row in maze) {
            blankRowString += "██ "
        }
            //Log.v("puzzling", blankRowString)
        string += blankRowString
        for (row in maze) {
            var rowString = "██ "
            for (i in row.indices)
                rowString += when (row[i]) {
                    1 -> "██ "
                    2 -> " ▲ "
                    3 -> " ► "
                    4 -> " ▼ "
                    5 -> " ◄ "
                    else -> "░░ "
                }// if (row[i] == 1) "██ " else "░░ "
            rowString += "██ "
//            Log.v("puzzling", rowString)
            string += "\n" + rowString
        }
//        Log.v("puzzling", blankRowString)
        string += "\n" + blankRowString
//        Log.v("puzzling", " hi ")
        return string
    }

    private fun printPath(maze: Array<Array<Int>>) {
        var blankRowString = "██ ██ "
        for (row in maze) {
            blankRowString += "██ "
        }
//        Log.v("puzzling", blankRowString)
        for (row in maze) {
            var rowString = "██ "
            for (i in row.indices)

                rowString += when (row[i]) {
                    0 -> "   "
                    1 -> "██ "
                    in 2..11 -> " " + (row[i] - 2) + " "
                    else -> "" + (row[i] - 2) + " "
                }
            //if (row[i] == 1) "██ " else if (row[i] - 2 < 10) " " + (row[i] - 2) + " " else "" + (row[i] - 2) + " "
            rowString += "██ "
//            Log.v("puzzling", rowString)
        }
//        Log.v("puzzling", blankRowString)
//        Log.v("puzzling", " hi ")
    }

    private fun createLongestRoute(maze: Array<Array<Int>>): Array<Triple<Int, Int, Int>> {
        val oppositeOf = mapOf(2 to 4, 3 to 5, 4 to 2, 5 to 3)

        val randomCoord =
            Random.nextInt((maze.size + 1) / 2) * 2 // if (Random.nextBoolean()) 0 else maze.size - 1
        val side = Random.nextInt(4) + 2
        val startCoord = when (side) {
            2 -> 0 to randomCoord // top side
            3 -> randomCoord to maze.size - 1// right side
            4 -> maze.size - 1 to randomCoord // bottom side
            else -> randomCoord to 0 // left side
        }

        maze[startCoord.first][startCoord.second] = side
        val pathLengths = Array(maze.size) { Array(maze.size) { 0 } }
        for (i in maze.indices) {
            for (j in maze.indices) {
                if (maze[i][j] == 1) {
                    pathLengths[i][j] = 1
                }
            }
        }
        setDirs(maze, pathLengths, startCoord.first, startCoord.second, 2)
        var longestPathCoords = Triple(0, 0, 2)
        for (i in pathLengths.indices) {
            if (pathLengths[0][i] > pathLengths[longestPathCoords.first][longestPathCoords.second]) {
                longestPathCoords = Triple(0, i, 2)
            }
            if (pathLengths[i][0] > pathLengths[longestPathCoords.first][longestPathCoords.second]) {
                longestPathCoords = Triple(i, 0, 5)
            }
            if (pathLengths[pathLengths.size - 1][i] > pathLengths[longestPathCoords.first][longestPathCoords.second]) {
                longestPathCoords = Triple(pathLengths.size - 1, i, 4)
            }
            if (pathLengths[i][pathLengths.size - 1] > pathLengths[longestPathCoords.first][longestPathCoords.second]) {
                longestPathCoords = Triple(i, pathLengths.size - 1, 3)
            }
        }
        val pathPieces = mapOf(
            (2 to 3) to -3,
            (2 to 4) to -1,
            (2 to 5) to -6,
            (3 to 2) to -3,
            (3 to 4) to -4,
            (3 to 5) to -2,
            (4 to 2) to -1,
            (4 to 3) to -4,
            (4 to 5) to -5,
            (5 to 2) to -6,
            (5 to 3) to -2,
            (5 to 4) to -5
        )
        val path = Array(pathLengths[longestPathCoords.first][longestPathCoords.second] + 1) {
            Triple(
                0,
                0,
                0
            )
        }
        var previousData = longestPathCoords
        path[0] = when (previousData.third) {
            2 -> Triple(previousData.first - 2, previousData.second, -10)
            3 -> Triple(previousData.first, previousData.second + 2, -9)
            4 -> Triple(previousData.first + 2, previousData.second, -8)
            else -> Triple(previousData.first, previousData.second - 2, -7)
        }
        var nextDir = 0
        for (i in 1 until path.size - 1) {
            nextDir = maze[previousData.first][previousData.second]
            val track = pathPieces[nextDir to previousData.third] ?: -1
            path[i] = Triple(previousData.first, previousData.second, track)
            val nextPrevDir = oppositeOf[nextDir] ?: nextDir
            previousData = when (nextDir) {
                2 -> Triple(previousData.first - 2, previousData.second, nextPrevDir)
                3 -> Triple(previousData.first, previousData.second + 2, nextPrevDir)
                4 -> Triple(previousData.first + 2, previousData.second, nextPrevDir)
                else -> Triple(previousData.first, previousData.second - 2, nextPrevDir)
            }
        }
        path[path.size - 1] = Triple(previousData.first, previousData.second, nextDir - 12)
        for (i in path.indices) {
//            Log.v("puzzling", "" + path[i].first + ", " + path[i].second + ": " + path[i].third)
        }
        // path[0] = Triple(longestPathCoords.first, longestPathCoords.second, 0)
        //printMaze(maze)
        //printPath(pathLengths)
        return path
    }

    private fun setDirs(
        maze: Array<Array<Int>>,
        path: Array<Array<Int>>,
        y: Int,
        x: Int,
        length: Int
    ) {
        path[y][x] = length
        //up
        if (x > 0 && maze[y][x - 1] == 0) {
            maze[y][x - 1] = 3
            maze[y][x - 2] = 3
            setDirs(maze, path, y, x - 2, length + 1)
        }
        //right
        if (y < maze.size - 1 && maze[y + 1][x] == 0) {
            maze[y + 1][x] = 2
            maze[y + 2][x] = 2
            setDirs(maze, path, y + 2, x, length + 1)
        }
        //down
        if (x < maze.size - 1 && maze[y][x + 1] == 0) {
            maze[y][x + 1] = 5
            maze[y][x + 2] = 5
            setDirs(maze, path, y, x + 2, length + 1)
        }
        //left
        if (y > 0 && maze[y - 1][x] == 0) {
            maze[y - 1][x] = 4
            maze[y - 2][x] = 4
            setDirs(maze, path, y - 2, x, length + 1)
        }
    }


    private class Tree(val id: Int) {
        var parent = this
        fun addChild(child: Tree) {
            child.getRoot().parent = this
        }

        fun getRoot(): Tree {
            if (parent === this) return this
            return parent.getRoot()
        }
    }
}
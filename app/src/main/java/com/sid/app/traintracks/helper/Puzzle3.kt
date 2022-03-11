package com.sid.app.traintracks.helper

import android.util.Log

class Puzzle3 {
//    While (# Walls Down < Total # Cells - 1)
//      Choose random cell current
//      Choose random neighbor of current that has a wall up between it and current
//      If such a neighbor exists
//          Find the labels of current and neighbor
//          If they are different,
//              union them, knock down the wall, and add to # Walls Down

    fun generateMaze(size: Int) {
        var id = 0
        val sets = Array(size) { Array(size) { Tree(id++) } }
        val edges = Array(2 * (size * (size - 1))) { Triple(0,0, true) }
        var finalEdges = Array((size - 1) * (size - 1)) { Triple(0,0, true) }
        var index = 0
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (row < size - 1) // if it's not the final row, you can go down, so
                    edges[index++] = Triple(row, col, false) // add bottom edge
                if (col < size - 1) // if it's not the final col, you can go right, so
                    edges[index++] = Triple(row, col, true) // add right edge
            }
        }
        edges.shuffle()
        var finalEdgeIndex = 0
        for ((row, col, isRightEdge) in edges) {
            val cell1 = sets[row][col]
            val cell2 = if (isRightEdge) sets[row][col + 1] else sets[row + 1][col]
            if (cell1.getRoot() !== cell2.getRoot()) {
                Log.v("puzzling", "cell A: ${cell1.getRoot().id}, cell B: ${cell2.getRoot().id}")
//                Log.v("puzzling", "[$row][$col][${if (isRightEdge) "right" else "down"}] -> " + if (isRightEdge) "[${row * 2}][${col * 2 + 1}]" else "[${row * 2 + 1}][${col * 2}]")
                cell1.addChild(cell2)
                Log.v("puzzling", "-    A: ${cell1.getRoot().id},      B: ${cell2.getRoot().id}")
            } else {
                 Log.v("puzzling", "$finalEdgeIndex")
                finalEdges[finalEdgeIndex++] = Triple(row, col, isRightEdge)
            }
        }
        val maze = Array(size * 2 - 1) { row -> Array(size * 2 - 1) { col ->  if (row % 2 == 0 || col % 2 == 0) 0 else 1 } }
        for ((row, col, isRightEdge) in finalEdges) {
            if (isRightEdge) {
                maze[row * 2][col * 2 + 1] = 1
            } else {
                maze[row * 2 + 1][col * 2] = 1
            }
        }
        for (row in maze) {
            var rowString = ""
            for (i in row.indices)
                rowString += if (row[i] == 1) "██ " else "░░ "
            Log.v("puzzling", rowString)
        }
        Log.v("puzzling", " hi ")
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
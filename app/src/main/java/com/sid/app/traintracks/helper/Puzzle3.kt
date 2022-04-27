package com.sid.app.traintracks.helper
//
//import com.sid.app.traintracks.util.nextInt
//import kotlin.math.abs
//import kotlin.random.Random
//
class Puzzle3(size: Int) {}
//
//    var grid: Array<Array<Path>>
//
//    init {
//        grid = Array(size) {
//            Array(size) {
//                Path()
//            }
//        }
//    }
//
//    fun fill() {
//        //Puzzle3().generateMaze(8)
//    }
//
//    fun fill5() {
//        val size = grid.size
//        val nodeGrid = createNodeGrid(size)
//        val xCoords = listOf(0) + (1 until size).toMutableList().shuffled()
//        val yCoords = (0 until size).toMutableList().shuffled()
//        var xyCoords = xCoords.mapIndexed { index, xCoord -> xCoord to yCoords[index] }.toTypedArray()
//        xyCoords = arrayOf(0 to 0, 4 to 2, 1 to 3, 3 to 1, 2 to 4)
//
//        loopy@for (i in 1 until size) {
//            when {
//                xyCoords[i].first == size - 1 -> nodeGrid[xyCoords[i].first][xyCoords[i].second].exit = Dir.EAST
//                xyCoords[i].second == 0 -> nodeGrid[xyCoords[i].first][xyCoords[i].second].exit = Dir.NORTH
//                xyCoords[i].second == size - 1 -> nodeGrid[xyCoords[i].first][xyCoords[i].second].exit = Dir.SOUTH
//                else -> continue@loopy
//            }
//            val tempPair = xyCoords[size - 1]
//            xyCoords[size - 1] = xyCoords[i]
//            xyCoords[i] = tempPair
//            break
//        }
//        var path = xyCoords.map { nodeGrid[it.first][it.second] }.toMutableList()
//        //val pathTestX = mutableListOf(nodeGrid[0][0], nodeGrid[1][4])
//        path[0].entr = Dir.WEST
//        //path = pathTestX
//        print(path, size)
//
//        //(1,1) (4,1)
//        //  0     0
//        // i = 0
//        //
//
//
//        // path is 7 long, so rootNode.next will be 7
//        // - .
//        //   |
//        //   - - .
//        //       |
//
//        // -   .
//        //     |
//        //     - .
//        //       -
//
//        var pathIsComplete = false
//        incompletePath@while (!pathIsComplete) {
//            //connect consecutive path nodes that are neighbors
//            updatePathNodes(path)
//            print(path, size)
//            //check for consecutive path nodes that would form a line
//            var straightsRemaining = true
//            while (straightsRemaining) {
//                Log.v("puzzling", "straight")
//                var i = 0
//                while (i < path.size) {
//                    val currentNode = path[i]
//                    if (currentNode.next == 1 && i < path.size - 1) { // (if the current node doesn't yet have a next path piece and isn't the last node)
//                        val nextNode = path[i + 1]
//                        if (currentNode.x == nextNode.x) {
//                            val range = if (currentNode.y < nextNode.y) currentNode.y + 1 until nextNode.y else currentNode.y - 1 downTo nextNode.y + 1
//                            val nodesToAdd = range.map { nodeGrid[currentNode.x][it] }
//                            path.addAll(i + 1, nodesToAdd)
//                            continue@incompletePath
//                        } else if (currentNode.y == nextNode.y) {
//                            val range = if (currentNode.x < nextNode.x) currentNode.x + 1 until nextNode.x else currentNode.x - 1 downTo nextNode.x + 1
//                            val nodesToAdd = range.map { nodeGrid[it][currentNode.y] }
//                            path.addAll(i + 1, nodesToAdd)
//                            continue@incompletePath
//                        }
//                    }
//                    i += path[i].next
//                }
//                straightsRemaining = false
//            }
//            //check for nodes in the path that only have 2 possible valid directions for entr and exit
//            var cornersRemaining = true
//            while (cornersRemaining) {
//                Log.v("puzzling", "corner")
//                var i = path[0].next
//                while (i < path.size - 1) {
//                    val currentNode = path[i]
//                    val connectionCount = (if (currentNode.entr != Dir.NULL) 1 else 0) + (if (currentNode.exit != Dir.NULL) 1 else 0)
//                    val emptyNeighbors = currentNode.getEmptyNodeNeighbors()
//                    if (emptyNeighbors.size + connectionCount == 2) {
//                        if (currentNode.entr == Dir.NULL) {
//                            if (currentNode.exit == Dir.NULL) {
//                                val previousNode = path[i - 1]
//                                val nextNode = path[i + 1]
//                                var newPrevNode = previousNode
//                                var newNextNode = nextNode
//                                emptyNeighbors.keys.forEach { dir ->
//                                    val dirIsPrevNode = when (dir) {
//                                        Dir.NORTH -> previousNode.y < nextNode.y
//                                        Dir.EAST -> previousNode.x > nextNode.x
//                                        Dir.SOUTH -> previousNode.y > nextNode.y
//                                        else -> previousNode.x < nextNode.x
//                                    }
//                                    if (dirIsPrevNode) {
//                                        newPrevNode = emptyNeighbors[dir]!!
//                                    } else {
//                                        newNextNode = emptyNeighbors[dir]!!
//                                    }
//                                }
//                                path.add(i, newPrevNode)
//                                path.add(i, newNextNode)
//                            } else {
//                                val nextNode = emptyNeighbors[currentNode.exit]!!
//                                path.add(i + 1, nextNode)
//                            }
//                        } else {
//                            val previousNode = emptyNeighbors[currentNode.entr]!!
//                            path.add(i, previousNode)
//                        }
//                        continue@incompletePath
//                    }
//                    i += path[i].next
//                }
//                cornersRemaining = false
//            }
//            // randomly add to the path somewhere
//            Log.v("puzzling", "random")
//            val possiblePathNodeIndices = mutableListOf<Int>()
//            for (i in 0 until path.size - 1) {
//                if (path[i].exit == Dir.NULL) {
//                    possiblePathNodeIndices.add(i)
//                }
//            }
//            val randomPathNodeIndex = possiblePathNodeIndices.random()
//            val currentNode = path[randomPathNodeIndex]
//            val nextNode = path[randomPathNodeIndex + 1]
//            val validDirs = arrayOf(
//                if (currentNode.y > nextNode.y) Dir.NORTH else Dir.SOUTH,
//                if (currentNode.x < nextNode.x) Dir.EAST else Dir.WEST
//            )
//            val randomDir = validDirs
//                .filter { currentNode.neighbors.containsKey(it) && currentNode.neighbors[it] != null }
//                .random()
//            path.add(randomPathNodeIndex + 1, currentNode.neighbors[randomDir]!!)
//            pathIsComplete = path[0].next == path.size
//        }
//        updatePathNodes(path)
//
//        print(path, size)
//    }
//
//    private fun updatePathNodes(path: MutableList<Node>) {
//        for (i in path.size - 2 downTo 0) {
//            val currentNode = path[i]
//            val nextNode = path[i + 1]
//            if (currentNode.next == 1 && abs(currentNode.x - nextNode.x) + abs(currentNode.y - nextNode.y) == 1) {
//                val nextDirection = when {
//                    currentNode.y > nextNode.y -> Dir.NORTH
//                    currentNode.x < nextNode.x -> Dir.EAST
//                    currentNode.y < nextNode.y -> Dir.SOUTH
//                    else -> Dir.WEST
//                }
//                currentNode.exit = nextDirection
//                nextNode.entr = nextDirection.opposite()
//                if (currentNode.entr != Dir.NULL) {
//                    Dir.getOtherDirections(currentNode.entr, currentNode.exit).forEach { dir ->
//                        if (currentNode.neighbors.containsKey(dir)) {
//                            currentNode.neighbors[dir]!!.removeDirection(dir.opposite())
//                        }
//                    }
//                }
//                if (nextNode.exit != Dir.NULL) {
//                    Dir.getOtherDirections(nextNode.entr, nextNode.exit).forEach { dir ->
//                        if (nextNode.neighbors.containsKey(dir)) {
//                            nextNode.neighbors[dir]!!.removeDirection(dir.opposite())
//                        }
//                    }
//                }
//                currentNode.next += nextNode.next
//            }
//        }
//    }
//
//    private fun print(path: MutableList<Puzzle.Node>, size: Int) {
//        var str = "."
//        for (y in 0 until size) {
//            str += "\n"
//            for (x in 0 until size) {
//                var contains = -1
//                path.forEachIndexed { index, it -> if (it.x == x && it.y == y) {contains = index} }
//                str += if (contains > -1) contains else "-"
//            }
//        }
//        Log.v("puzzling", str)
//    }
//
//    fun createNodeGrid(size: Int): Array<Array<Node>> {
//        val nodeGrid = Array(size) { x ->
//            Array(size) { y ->
//                Node(x, y)
//            }
//        }
//        nodeGrid.forEach { col -> col.forEach { it.setNeighbors(nodeGrid) } }
//        return nodeGrid
//    }
//
//    fun fillAlt2() {
//        val size = grid.size
//        val nodeGrid = createNodeGrid(size)
//
//        var currNode = nodeGrid[0][Random.nextInt(size)]
//        currNode.entr = Dir.WEST
//        var nodeNeighbors = currNode.getEmptyNodeNeighbors()
//        Log.v("puzzleDebug", nodeNeighbors.toString())
//
//        while (nodeNeighbors.isNotEmpty()) {
//            val randomDir = nodeNeighbors.keys.random()
//            currNode.exit = randomDir
//            nodeNeighbors[randomDir]?.entr = randomDir.opposite()
//            currNode = nodeNeighbors[randomDir]!!
//            nodeNeighbors = currNode.getEmptyNodeNeighbors()
//        }
//
//        currNode.exit = when {
//            currNode.x == size - 1 -> Dir.EAST
//            currNode.y == 0 -> Dir.NORTH
//            currNode.y == size - 1 -> Dir.SOUTH
//            else -> Dir.WEST
//        }
//
//        var gridString = "-\n"
//        for (y in nodeGrid.indices) {
//            for (element in nodeGrid) {
//                gridString += element[y].getCode()
//            }
//            gridString += "\n"
//        }
//
//        Log.v("puzzleTest", gridString)
//
//        val entrance = 0 to Random.nextInt(grid.size) // column 0, row random
//
//    }
//
//    fun fillAlt() {
//        val entrance = 0 to Random.nextInt(grid.size) // column 0, row random
//        val exit = Random.nextInt(4).let { side ->
//            when (side) {
//                0 -> 0 to Random.nextInt(0, grid.size, entrance.second)
//                1 -> grid.size - 1 to Random.nextInt(grid.size)
//                2 -> Random.nextInt(grid.size) to 0
//                else -> Random.nextInt(grid.size) to grid.size - 1
//            }
//        }
//        var cols = Array(grid.size) {
//            if (it == 0) { //if first column
//                if (exit.first == 0) { // and entrance and exit are both in this column
//                    Random.nextInt(2, grid.size)
//                } else {
//                    Random.nextInt(1, grid.size)
//                }
//            }
//            Random.nextInt(grid.size - if (it == 0) 0 else 1) + 1
//            Random.nextInt(2, grid.size)
//        }
//    }
//
//    enum class Dir {NORTH, EAST, SOUTH, WEST, NULL;
//        fun opposite(): Dir {
//            return when (this) {
//                NULL -> NULL
//                NORTH -> SOUTH
//                SOUTH -> NORTH
//                EAST -> WEST
//                WEST -> EAST
//            }
//        }
//        companion object {
//            fun getOtherDirections(dir1: Dir, dir2: Dir): Array<Dir> {
//                return arrayOf(NORTH, EAST, SOUTH, WEST).filter { dir -> dir != dir1 && dir != dir2 }.toTypedArray()
//            }
//        }
//    }
//
//    inner class Path {
//        var inways = arrayOf(Dir.NULL, Dir.NULL)
//    }
//
//    inner class Node(val x: Int, val y: Int) {
//        var entr = Dir.NULL
//        var exit = Dir.NULL
//        var next = 1
//        private fun isEmpty(): Boolean { return entr == Dir.NULL && exit == Dir.NULL }
//        fun getEmptyNodeNeighbors():Map<Dir, Puzzle.Node?> {
//            return if (this::neighbors.isInitialized) {
//                neighbors.filterValues { neighborNode -> neighborNode?.isEmpty() ?: false }
//            } else {
//                mapOf()
//            }
//        }
//        fun getEmptyNodeNeighborsArray():Array<Puzzle.Node> {
//            return if (this::neighbors.isInitialized) {
//                neighbors
//                    .filterValues { neighborNode -> neighborNode?.isEmpty() ?: false }
//                    .values
//                    .filterNotNull()
//                    .toTypedArray()
//            } else {
//                arrayOf()
//            }
//        }
//        lateinit var neighbors: Map<Dir, Puzzle.Node?>
//        fun removeDirection(dir: Dir) {
//            neighbors = neighbors.filter { it.key != dir }.toMap()
//        }
//        fun setNeighbors(grid: Array<Array<Puzzle.Node>>) {
//            neighbors = mapOf(
//                Dir.NORTH to (if (y == 0) null else grid[x][y - 1]),
//                Dir.EAST to (if (x == grid.size - 1) null else grid[x + 1][y]),
//                Dir.SOUTH to (if (y == grid.size - 1) null else grid[x][y + 1]),
//                Dir.WEST to (if (x == 0) null else grid[x - 1][y])
//            ).filter { it.value != null }
//        }
//        fun getCode(): String {
//            when (entr) {
//                Dir.NORTH -> return when (exit) {
//                    Dir.EAST -> "╚"
//                    Dir.SOUTH -> "║"
//                    Dir.WEST -> "╝"
//                    else -> " "
//                }
//                Dir.EAST -> return when (exit) {
//                    Dir.NORTH -> "╚"
//                    Dir.SOUTH -> "╔"
//                    Dir.WEST -> "═"
//                    else -> " "
//                }
//                Dir.SOUTH -> return when (exit) {
//                    Dir.NORTH -> "║"
//                    Dir.EAST -> "╔"
//                    Dir.WEST -> "╗"
//                    else -> " "
//                }
//                Dir.WEST -> return when (exit) {
//                    Dir.NORTH -> "╝"
//                    Dir.EAST -> "═"
//                    Dir.SOUTH -> "╗"
//                    else -> " "
//                }
//                else -> return " "
//            }
//        }
//
//    }
//}
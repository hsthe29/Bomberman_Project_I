package core.algorithm

data class Square(val col: Int, val row: Int)
data class SquareCoordinate(val col: Double, val row: Double)
data class QueueNode(val pos: Square, val parent: QueueNode?, val dist: Int)
object PathFinder {
    private var rowOffset = intArrayOf(-1, 0, 0, 1)
    private var colOffset = intArrayOf(0, -1, 1, 0)

    fun evalShortestPath(arrayMap: Array<IntArray>, start: Square, target: Square): ArrayDeque<SquareCoordinate> {
        if (arrayMap[start.col][start.row] == 0 || arrayMap[target.col][target.row] == 0) {
            return ArrayDeque()
        }

        val visited = Array(37) { BooleanArray(17) { false } }
        visited[start.col][start.row] = true
        val queue = ArrayDeque<QueueNode>()
        queue.addFirst(QueueNode(start, null, 0))

        while (queue.isNotEmpty()) {
            val curr = queue.removeLast()
            if(curr.pos == target) {
                val temp = ArrayDeque<SquareCoordinate>(curr.dist)
                var tempNode: QueueNode? = curr
                while(tempNode != null) {
                    temp.addFirst(SquareCoordinate(tempNode.pos.col*45+23.0, tempNode.pos.row*45+23.0))
                    tempNode = tempNode.parent
                }
                return temp
            }
            val pt = curr.pos

            for (i in 0..3) {
                val row = pt.row + rowOffset[i]
                val col = pt.col + colOffset[i]

                if (isValid(col, row) && arrayMap[col][row] == 1 && !visited[col][row]) {
                    visited[col][row] = true
                    val adj = QueueNode(
                        Square(col, row),
                        curr,
                        curr.dist + 1
                    )
                    queue.addFirst(adj)
                }
            }
        }
        println("Stalemate")
        return ArrayDeque()
    }

    private fun isValid(col: Int, row: Int): Boolean {
        return row >= 0 && row < 17 && col >= 0 && col < 37
    }

}

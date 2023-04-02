package main.kotlin.graphsearch.세번이내에사과를먹자_26169번

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

private val br = BufferedReader(InputStreamReader(System.`in`))
private val bw = BufferedWriter(OutputStreamWriter(System.out))
private var startRow = 0
private var startColumn = 0
private lateinit var board: Array<Array<Int>>
private lateinit var visited: Array<Array<Boolean>>
private val directionX = listOf(0, 0, -1, 1)
private val directionY = listOf(1, -1, 0, 0)
private var movingCount = 0
private var appleCount = 0

fun main() {
    board = Array(5) { Array(5) { 0 } }
    visited = Array(5) { Array(5) { false } }

    repeat(5) { index ->
        board[index] = br.readLine().split(" ").map { it.toInt() }.toTypedArray()
    }

    val start = br.readLine().split(" ").map { it.toInt() }
    startRow = start[0]
    startColumn = start[1]

}


fun bfs(startRow: Int, startColumn: Int) {
    val queue: Queue<IntArray> = LinkedList()
    queue.add(intArrayOf(startRow, startColumn))
    visited[startRow][startColumn] = true

    while (queue.isNotEmpty()) {
        val (currentX, currentY) = queue.poll()
        repeat(4) { index ->
            if (movingCount > 3) {
                if (appleCount >= 2) {
                    bw.write("1")
                    bw.flush()
                    return
                }
                bw.write("0")
                bw.flush()
                return
            }

            val nextX = currentX + directionX[index]
            val nextY = currentY + directionY[index]

            if (nextX in 0..4 && nextY in 0..4) {
                if (board[nextX][nextY] == 1) {
                    appleCount += 1
                    queue.add(intArrayOf(nextX, nextY))
                    visited[nextX][nextY] = true
                } else if (board[nextX][nextY] == -1) {
                    visited[nextX][nextY] = true
                } else if (board[nextX][nextY] == 0) {
                    queue.add(intArrayOf(nextX, nextY))
                    visited[nextX][nextY] = true
                }
            }
        }
    }
}

fun dfs(startRow: Int, startColumn: Int) {
    visited[startRow][startColumn] = true


}
package main.kotlin.graphsearch.점프왕젤리_16173번

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

private val br = BufferedReader(InputStreamReader(System.`in`))
private val bw = BufferedWriter(OutputStreamWriter(System.out))

private lateinit var graph: Array<Array<Int>>
private lateinit var visited: Array<Array<Boolean>>
private val directionX = listOf(1, 0)
private val directionY = listOf(0, 1)

private val endFlag = "HaruHaru"
private val failFlag = "Hing"

fun main() {
    val n = br.readLine().toInt()
    graph = Array(n) { Array(n) { 0 } }
    visited = Array(n) { Array(n) { false } }

    repeat(n) { index ->
        graph[index] = br.readLine().split(" ").map { it.toInt() }.toTypedArray()
    }

    bfs(graph, visited, n)
}

fun bfs(graph: Array<Array<Int>>, visited: Array<Array<Boolean>>, n: Int) {
    val queue: Queue<IntArray> = LinkedList()
    queue.add(intArrayOf(0, 0))
    visited[0][0] = true

    while (queue.isNotEmpty()) {
        val (currentX, currentY) = queue.poll()

        if (currentX == n - 1 && currentY == n - 1) {
            bw.write(endFlag)
            bw.flush()
            return
        }

        repeat(2) { index ->
            val jump = graph[currentX][currentY]
            val nextX = currentX + directionX[index] * jump
            val nextY = currentX + directionY[index] * jump

            if (!visited[nextX][nextY]) {
                if (nextX !in 0..n || nextY !in 0..n) {
                    bw.write(failFlag)
                    bw.flush()
                    return
                } else {
                    queue.add(intArrayOf(nextX, nextY))
                }
            }
        }
    }
}
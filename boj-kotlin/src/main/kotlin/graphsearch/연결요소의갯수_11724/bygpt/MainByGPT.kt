package main.kotlin.graphsearch.연결요소의갯수_11724

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

private val br: BufferedReader = BufferedReader(InputStreamReader(System.`in`))
private val bw: BufferedWriter = BufferedWriter(OutputStreamWriter(System.out))
private lateinit var graph: Array<Array<Int>>
private lateinit var visited: Array<Boolean>
private lateinit var queue: Queue<Int>

fun main() {
    val (n: Int, m: Int) = br.readLine().split(" ").map { it.toInt() }

    graph = Array(n + 1) { Array(n + 1) { 0 } }
    visited = Array(n + 1) { false }
    queue = LinkedList()

    repeat(m) {
        val (a: Int, b: Int) = br.readLine().split(" ").map { it.toInt() }
        graph[a][b] = 1
        graph[b][a] = 1
    }

    var count = 0
    for (i in 1..n) {
        if (!visited[i]) {
            bfs(i, n)
            count++
        }
    }
    bw.write("$count")
    bw.flush()
}

private fun bfs(start: Int, n: Int) {
    queue.add(start)
    visited[start] = true

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        for (i in 1..n) {
            if (graph[current][i] == 1 && !visited[i]) {
                queue.add(i)
                visited[i] = true
            }
        }
    }
}
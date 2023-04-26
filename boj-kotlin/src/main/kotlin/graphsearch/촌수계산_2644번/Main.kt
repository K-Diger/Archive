package main.kotlin.graphsearch.촌수계산_2644번

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

private val br: BufferedReader = BufferedReader(InputStreamReader(System.`in`))
private val bw: BufferedWriter = BufferedWriter(OutputStreamWriter(System.out))

private lateinit var graph: Array<Array<Int>>
private lateinit var visited: Array<Boolean>
private lateinit var counts: Array<Int>

fun main() {

    val n = br.readLine().toInt()
    val (a: Int, b: Int) = br.readLine().split(" ").map { it.toInt() }
    val m = br.readLine().toInt()

    graph = Array(n + 1) { Array(n + 1) { 0 } }
    visited = Array(n + 1) { false }
    counts = Array(n + 1) { 0 }

    repeat(m) {
        val (x: Int, y: Int) = br.readLine().split(" ").map { it.toInt() }
        graph[x][y] = 1
        graph[y][x] = 1
    }
    bfs(a, b)
}

fun bfs(start: Int, end: Int) {
    val queue: Queue<Int> = LinkedList()
    queue.add(start)
    //visited[start] = true

    while (queue.isNotEmpty()) {
        val current = queue.poll()

        for (value in graph[current]) {
            if (!visited[value]) {
                queue.add(value)
                visited[value] = true
                counts[value] = counts[current] + 1
            }
        }
    }

    if (counts[end] == 0) {
        bw.write("-1")
        bw.flush()
        return
    }
    bw.write("${counts[end]}")
    bw.flush()
}
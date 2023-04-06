package main.kotlin.graphsearch.연결요소의갯수_11724

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

private val br = BufferedReader(InputStreamReader(System.`in`))
private val bw = BufferedWriter(OutputStreamWriter(System.out))

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

    // 1부터 n까지 방문하지 않은 노드를 찾았다면
    // 그 노드를 시작으로 잡고 순회한다.
    for (i in 1..n) {
        if (!visited[i]) {
            bfs(i, n)
            count++
        }
    }
    br.close()
    bw.write(count.toString())
    bw.flush()
    bw.close()
}

private fun bfs(start: Int, n: Int) {
    queue.add(start)
    visited[start] = true

    while (queue.isNotEmpty()) {
        val current = queue.poll()

        // 현재 방문한 노드와 연결된 모든 노드를 찾아낸다.
        // 따라서 1부터 마지막 노드의 번호까지 반복한다.
        for (i in 1..n) {
            if (!visited[i] && graph[current][i] == 1) {
                queue.add(i)
                visited[i] = true
            }
        }
    }
}

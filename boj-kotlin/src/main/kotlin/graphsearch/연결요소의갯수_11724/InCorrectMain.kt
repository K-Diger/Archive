package main.kotlin.graphsearch.연결요소의갯수_11724

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

private val br: BufferedReader = BufferedReader(InputStreamReader(System.`in`))
private val bw: BufferedWriter = BufferedWriter(OutputStreamWriter(System.out))
private lateinit var graph: Array<Array<Int>>

// 2차원 배열 불필요 -> 1차원 배열로 변경한다.
private lateinit var visited: Array<Array<Boolean>>

// direction 불필요, 모든 정점을 방문하는 요구사항이 아니기 때문
private val directionX = arrayOf(0, 0, -1, 1)
private val directionY = arrayOf(1, -1, 0, 0)
// direction 불필요, 모든 정점을 방문하는 요구사항이 아니기 때문

fun main() {
    val (n: Int, m: Int) = br.readLine().split(" ").map { it.toInt() }

    graph = Array(n + 1) { Array(n + 1) { 0 } }

    // 2차원 배열 불필요 -> 1차원 배열로 변경한다.
    visited = Array(n + 1) { Array(n + 1) { false } }
    repeat(m) {
        val (a: Int, b: Int) = br.readLine().split(" ").map { it.toInt() }
        graph[a][b] = 1
        graph[b][a] = 1
    }

    // count 변수를 추가한다.

    // 1부터 n까지 반복하여 방문하지 않은 노드일 시 bfs탐색을 시작한다.
    // 탐색을 이미 했다면 count가 증가하지 않는다.
    // bfs의 매개변수는 시작노드 번호와, 마지막 노드의 번호이다.
    bfs(n)
}

private fun bfs(n: Int) {
    val queue: Queue<IntArray> = LinkedList()
    queue.add(intArrayOf(1, 1))
    visited[1][1] = true

    while (queue.isNotEmpty()) {
        val poll = queue.poll()
        val currentX = poll.get(0)
        val currentY = poll.get(1)

        // direction으로 탐색하는 구문은 불필요하다.
        // 방문하지 않은 노드이고, 연결된 노드일 때만 방문한다.
        repeat(4) { index ->
            val nextX = currentX + directionX[index]
            val nextY = currentY + directionY[index]
            if (nextX in 0..n && nextY in 0..n) {
                queue.add(intArrayOf(nextX, nextY))
                visited[nextX][nextY] = true
            }
        }
    }
}
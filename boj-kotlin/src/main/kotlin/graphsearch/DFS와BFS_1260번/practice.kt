package graphsearch.DFS와BFS_1260번

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

private val br = BufferedReader(InputStreamReader(System.`in`))
private val bw = BufferedWriter(OutputStreamWriter(System.out))

private fun main() {
    val (n, m, v) = readLine().toString().split(" ").map { it.toInt() }
    val graph = Array(n + 1) { mutableListOf<Int>() }
    val visited = BooleanArray(n + 1)

    repeat(m) {
        val (a, b) = readLine().toString().split(" ").map { it.toInt() }
        graph[a].add(b)
        graph[b].add(a)
    }
    br.close()

    graph.forEach { it.sort() }

    dfs(graph, v, visited)
    bw.write("\n")
    bw.flush()

    Arrays.fill(visited, false)
    bfs(graph, v, visited)
    bw.close()
}

private fun dfs(graph: Array<MutableList<Int>>, node: Int, visited: BooleanArray) {
    visited[node] = true
    bw.write("$node ")
    bw.flush()

    for (nextNode in graph[node]) {
        if (!visited[nextNode]) {
            dfs(graph, nextNode, visited)
        }
    }
}

private fun bfs(graph: Array<MutableList<Int>>, start: Int, visited: BooleanArray) {
    val queue = LinkedList<Int>()
    queue.add(start)
    visited[start] = true

    while (queue.isNotEmpty()) {
        val currentNode = queue.poll()
        bw.write("$currentNode ")
        for (nextNode in graph[currentNode]) {
            if (!visited[nextNode]) {
                visited[nextNode] = true
                queue.add(nextNode)
            }
        }
    }
}
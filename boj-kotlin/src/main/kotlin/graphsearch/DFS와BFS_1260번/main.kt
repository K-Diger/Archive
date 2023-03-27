package graphsearch.DFS와BFS_1260번

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*


private val br = BufferedReader(InputStreamReader(System.`in`))
private val bw = BufferedWriter(OutputStreamWriter(System.out))
private var n = 0
private var m = 0
private var start = 0
private lateinit var graph: Array<MutableList<Int>>
private lateinit var visited: BooleanArray

fun main(args: Array<String>) {
    val (n, m, v) = readLine()!!.split(" ").map { it.toInt() }
    val graph = Array(n + 1) { mutableListOf<Int>() }
    repeat(m) {
        val (a, b) = readLine()!!.split(" ").map { it.toInt() }
        graph[a].add(b)
        graph[b].add(a)
    }
    graph.forEach { it.sort() }

    val visited = BooleanArray(n + 1)

    dfs(graph, v, visited)
    println()

    Arrays.fill(visited, false)
    bfs(graph, v, visited)
}

private fun dfs(graph: Array<MutableList<Int>>, node: Int, visited: BooleanArray) {
    visited[node] = true
    print("$node ")
    for (nextNode in graph[node]) {
        if (!visited[nextNode]) {
            dfs(graph, nextNode, visited)
        }
    }
}

private fun bfs(graph: Array<MutableList<Int>>, node: Int, visited: BooleanArray) {
    val queue = LinkedList<Int>()
    queue.add(node)
    visited[node] = true
    while (queue.isNotEmpty()) {
        val curr = queue.poll()
        print("$curr ")
        for (nextNode in graph[curr]) {
            if (!visited[nextNode]) {
                visited[nextNode] = true
                queue.add(nextNode)
            }
        }
    }
}

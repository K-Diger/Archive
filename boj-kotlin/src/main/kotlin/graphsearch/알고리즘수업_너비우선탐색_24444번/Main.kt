package main.kotlin.graphsearch.알고리즘수업_너비우선탐색_24444번

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

private val br: BufferedReader = BufferedReader(InputStreamReader(System.`in`))
private val bw: BufferedWriter = BufferedWriter(OutputStreamWriter(System.out))
private lateinit var graph: Array<MutableList<Int>>
private lateinit var visited: Array<Boolean>

private lateinit var result: MutableList<String>
private lateinit var tempResult: HashMap<Int, Boolean>

fun main() {
    val (n: Int, m: Int, r: Int) = br.readLine().split(" ").map { it.toInt() }
    graph = Array(n + 1) { mutableListOf() }
    visited = Array(n + 1) { false }
    result = ArrayList(m)
    tempResult = HashMap<Int, Boolean>()

    repeat(m) { index ->
        tempResult.put(index + 1, false)
    }

    repeat(n) {
        val (a: Int, b: Int) = br.readLine().split(" ").map { it.toInt() }
        graph[a].add(b)
        graph[b].add(a)
    }

    tempResult.put(r, true)
    result.add(r.toString())
    bfs(r)

    println("------")


    repeat(m) { index ->
        if (tempResult.get(index + 1) == false) {
            result.add(index, "0")
        }
    }

    for (i: Int in 0..result.size) {
        if (i == result.size - 1) {
            bw.write(result[i])
            bw.flush()
            return
        }
        bw.write(result[i] + "\n")
        bw.flush()
    }
}

fun bfs(start: Int) {
    val queue: Queue<Int> = LinkedList()
    queue.add(start)
    visited[start] = true

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        println(current)

        for (adjustValue in graph[current]) {
            if (!visited[adjustValue]) {
                queue.add(adjustValue)
                visited[adjustValue] = true
                tempResult.put(adjustValue, true)
                result.add(adjustValue.toString())
            }
        }
    }
}